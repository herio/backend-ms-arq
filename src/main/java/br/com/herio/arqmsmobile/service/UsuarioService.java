package br.com.herio.arqmsmobile.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.model.File;

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
		// cria
		usuario.valida();
		usuario.setSenha(Base64.getEncoder().encodeToString(usuario.getSenha().getBytes()));
		usuario = usuarioRepository.save(usuario);
		usuario.setToken(autenticacaoService.criaTokenJwt(usuario));

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
		usuarioBd.setLogin(usuario.getLogin());
		usuarioBd.setNome(usuario.getNome());
		usuarioBd.setSenha(Base64.getEncoder().encodeToString(usuario.getSenha().getBytes()));
		usuarioBd.setEmail(usuario.getEmail());
		usuarioBd.setAtivado(usuario.isAtivado());
		usuarioBd.valida();
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
		File file = googleDriveFachada.uploadFile(mfile, sistema.getUploadFolder());
		String fileUri = String.format(sistema.getDownloadUrl(), idUsuario, file.getId());
		usuario.setUrlFoto(fileUri);
		usuarioRepository.save(usuario);

		// enviaEmail
		enviadorEmailService.enviaEmailAtualizacaoDados(usuario);
		return usuario;
	}

	public String recuperarSenha(String login, EnumSistema sistema) {
		Usuario usuario = usuarioRepository.findByLoginAndSistema(login, sistema.name()).get();
		if (usuario == null) {
			throw new ExcecaoNegocio(String.format("Usuário de login %s inexistente", login));
		}

		// enviaEmail
		return enviadorEmailService.enviaEmailRecuperaSenha(usuario, sistema);
	}

	private void criaConfigNotificacaoDefault(Long idUsuario, EnumSistema sistema) {
		ConfiguracaoNotificacao configuracaoNotificacao = new ConfiguracaoNotificacao();
		configuracaoNotificacao.setReceberNotificacao(true);
		configuracaoNotificacao.getItens().add(EnumSistema.getConfigItemDefault(sistema));
		configuracaoNotificacaoService.salvarConfiguracao(idUsuario, configuracaoNotificacao);
	}

}
