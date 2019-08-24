package br.com.herio.arqmsmobile.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.model.File;

import br.com.herio.arqmsmobile.dominio.ArquivoUsuario;
import br.com.herio.arqmsmobile.dominio.ArquivoUsuarioRepository;
import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacao;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.infra.drive.GoogleDriveFachada;
import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;

@Service
public class UsuarioService {

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected ArquivoUsuarioRepository arquivoUsuarioRepository;

	@Autowired
	protected AtivacaoUsuarioService ativacaoUsuarioService;

	@Autowired
	protected AutenticacaoService autenticacaoService;

	@Autowired
	protected EnviadorEmailService enviadorEmailService;

	@Autowired
	protected GoogleDriveFachada googleDriveFachada;

	@Autowired
	protected ConfiguracaoNotificacaoService configuracaoNotificacaoService;

	public Usuario criarUsuario(Usuario usuario) {
		if (usuario.getId() != null) {
			throw new IllegalArgumentException("Informe um novo usuário (sem id)!");
		}
		// verifica se usuário já existe
		Optional<Usuario> usuarioOpt = usuarioRepository.findByLoginAndSistema(usuario.getLogin(), usuario.getSistema());
		Usuario usuarioBd = usuario;
		if (usuarioOpt.isPresent()) {
			usuarioBd = usuarioOpt.get();
			if (usuarioBd.getDataExclusao() == null) {
				// usuário existente
				throw new ExcecaoNegocio("Usuário já cadastrado! Solicite a recuperação de senha.");
			} else {
				// usuário excluído anteriormente, atualiza
				usuarioBd.setDataExclusao(null);
				atualizaUsuario(usuarioBd, usuario);
			}
		} else {
			usuarioBd.setSenha(Base64.getEncoder().encodeToString(usuario.getSenha().getBytes()));
		}

		// cria/atualiza excluído
		usuarioBd = usuarioRepository.save(usuarioBd);
		usuarioBd.setToken(autenticacaoService.criaTokenJwt(usuarioBd));

		// gera ativação
		ativacaoUsuarioService.gerarAtivacaoUsuario(usuario.getId());

		// criaconfignotificacao default
		EnumSistema sistema = EnumSistema.valueOf(usuario.getSistema());
		criaConfigNotificacaoDefault(usuario.getId(), sistema);

		// enviaEmail
		enviadorEmailService.enviaEmailBoasVindas(usuario, sistema);
		return usuario;
	}

	public Usuario atualizarUsuario(Long idUsuario, Usuario usuario) {
		if (idUsuario == null) {
			throw new IllegalArgumentException("Informe um usuário já existente (com id)!");
		}
		// atualiza
		Usuario usuarioBd = usuarioRepository.findById(idUsuario).get();
		atualizaUsuario(usuarioBd, usuario);
		usuarioBd = usuarioRepository.save(usuarioBd);

		// enviaEmail
		enviadorEmailService.enviaEmailAtualizacaoDados(usuarioBd);
		return usuarioBd;
	}

	public java.io.File downloadFoto(String idFile, String fileName) {
		return googleDriveFachada.downloadFile(idFile, fileName);
	}

	public Usuario uploadFoto(Long idUsuario, MultipartFile mfile) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		EnumSistema sistema = EnumSistema.valueOf(usuario.getSistema());
		File file = googleDriveFachada.uploadFile(idUsuario, mfile, sistema.getUploadFolder(), true);
		String urlDownloadArquivo = sistema.getUrlBase() + "/publico/usuarios/%s/files/fotos/%s";
		String fileUri = String.format(urlDownloadArquivo, idUsuario, file.getId());
		usuario.setUrlFoto(fileUri);
		usuarioRepository.save(usuario);

		// enviaEmail
		enviadorEmailService.enviaEmailAtualizacaoDados(usuario);
		return usuario;
	}

	public String recuperarSenha(String login, EnumSistema sistema) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByLoginAndSistema(login, sistema.name());
		if (!usuarioOpt.isPresent()) {
			throw new ExcecaoNegocio(String.format("Usuário '%s' inexistente!", login));
		}
		Usuario usuario = usuarioOpt.get();
		if (!usuario.isAtivado()) {
			throw new ExcecaoNegocio(String.format("Usuário '%s' não está ativado!", login));
		}
		if (usuario.getEmail() != null) {
			throw new ExcecaoNegocio(String.format("Usuário '%s' não possui e-mail cadastrado!", login));
		}

		// enviaEmail
		return enviadorEmailService.enviaEmailRecuperaSenha(usuario, sistema);
	}

	public void removerUsuario(Long idUsuario) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		usuario.setAtivado(false);
		usuario.setDataExclusao(LocalDateTime.now(ZoneId.of("UTC-3")));
		usuarioRepository.save(usuario);
	}

	public Collection<Usuario> listarUsuarios(boolean ativos) {
		Stream<Usuario> stream = StreamSupport.stream(usuarioRepository.findAll().spliterator(), false);
		if (ativos) {
			stream = stream.filter(usuario -> usuario.isAtivado());
		}
		return stream.collect(Collectors.toList());
	}

	public ArquivoUsuario uploadArquivo(Long idUsuario, MultipartFile mfile, String atributos) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		EnumSistema sistema = EnumSistema.valueOf(usuario.getSistema());
		File file = googleDriveFachada.uploadFile(idUsuario, mfile, sistema.getUploadFolder(), false);
		String urlDownloadArquivo = sistema.getUrlBase() + "/publico/usuarios/%s/files/arquivos/%s";
		String fileUri = String.format(urlDownloadArquivo, idUsuario, file.getId());

		ArquivoUsuario arquivo = new ArquivoUsuario();
		arquivo.setIdDrive(file.getId());
		arquivo.setNome(file.getName());
		arquivo.setLink(fileUri);
		arquivo.setUsuario(usuario);
		arquivo.setAtributos(atributos);
		return arquivoUsuarioRepository.save(arquivo);
	}

	public java.io.File downloadArquivo(String idArquivo) {
		ArquivoUsuario arquivo = arquivoUsuarioRepository.findByIdDrive(idArquivo).get();
		return googleDriveFachada.downloadFile(arquivo.getIdDrive(), arquivo.getNome());
	}

	private void atualizaUsuario(Usuario usuarioBd, Usuario usuario) {
		usuarioBd.setSistema(usuario.getSistema());
		usuarioBd.setLogin(usuario.getLogin());
		usuarioBd.setEmail(usuario.getEmail());
		usuarioBd.setNome(usuario.getNome());
		usuarioBd.setSenha(Base64.getEncoder().encodeToString(usuario.getSenha().getBytes()));
		usuarioBd.setAtivado(usuario.isAtivado());

		usuarioBd.setTelefone(usuario.getTelefone());
		usuarioBd.setCelular(usuario.getCelular());
		usuarioBd.setInstagram(usuario.getInstagram());
		usuarioBd.setFacebook(usuario.getFacebook());
		usuarioBd.setCpf(usuario.getCpf());
	}

	private void criaConfigNotificacaoDefault(Long idUsuario, EnumSistema sistema) {
		ConfiguracaoNotificacao configuracaoNotificacao = new ConfiguracaoNotificacao();
		configuracaoNotificacao.setReceberNotificacao(true);
		configuracaoNotificacao.getItens().add(EnumSistema.getConfigItemDefault(sistema));
		configuracaoNotificacaoService.salvarConfiguracao(idUsuario, configuracaoNotificacao);
	}

	public boolean deleteArquivo(Long idUsuario, String idArquivo) {
		return googleDriveFachada.deleteFile(idArquivo);
	}

}
