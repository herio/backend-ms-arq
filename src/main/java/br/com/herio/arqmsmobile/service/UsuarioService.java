package br.com.herio.arqmsmobile.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.herio.arqmsmobile.dominio.ArquivoUsuario;
import br.com.herio.arqmsmobile.dominio.ArquivoUsuarioRepository;
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

	@Autowired
	private PrincipalService principalService;

	public Usuario criarUsuario(Usuario usuario) {

		// TODO
		if (EnumSistema.MEU_COACH_OAB.equals(EnumSistema.valueOf(usuario.getSistema()))) {
			throw new ExcecaoNegocio("Sistema em fase de testes, em breve o cadatro de novos usuários estará liberado!");
		}
		if (usuario.getId() != null) {
			throw new IllegalArgumentException("Informe um novo usuário (sem id)!");
		}
		// verifica se usuário já existe
		Optional<Usuario> usuarioOpt = usuarioRepository.findByLoginIgnoreCaseAndSistema(usuario.getLogin().toLowerCase(), usuario.getSistema());
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

		// valida usuário
		validaUsuario(usuarioBd);

		// cria/atualiza excluído
		usuarioBd = usuarioRepository.save(usuarioBd);

		// atualiza token
		usuarioBd.setToken(autenticacaoService.criaTokenJwt(usuarioBd));

		// gera ativação
		ativacaoUsuarioService.gerarAtivacaoUsuario(usuario.getId());

		// criaConfiguracaoNotificacaoDefault
		EnumSistema sistema = EnumSistema.valueOf(usuario.getSistema());
		configuracaoNotificacaoService.criaConfiguracaoNotificacaoDefault(usuario.getId(), sistema);

		// enviaEmail
		enviadorEmailService.enviaEmailBoasVindas(usuario, sistema);
		return usuario;
	}

	public Usuario atualizarUsuario(Long idUsuario, Usuario usuario) {
		// somente usuário e admin podem realizar essa operação
		principalService.validaPermissaoUsuario(idUsuario);

		// atualiza
		Usuario usuarioBd = usuarioRepository.findById(idUsuario).get();
		atualizaUsuario(usuarioBd, usuario);

		// valida usuário
		validaUsuario(usuarioBd);

		usuarioBd = usuarioRepository.save(usuarioBd);
		usuarioBd.setToken(autenticacaoService.criaTokenJwt(usuarioBd));

		// enviaEmail
		enviadorEmailService.enviaEmailAtualizacaoDados(usuarioBd);
		return usuarioBd;
	}

	public boolean tornarAdmin(Long idUsuario) {
		// somente admin podem realizar essa operação
		principalService.validaPermissaoUsuario(null);

		Usuario usuarioBd = usuarioRepository.findById(idUsuario).get();
		usuarioBd.setAdmin(true);
		usuarioRepository.save(usuarioBd);
		return true;
	}

	public String recuperarSenha(String email, EnumSistema sistema) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndSistema(email, sistema.name());
		if (!usuarioOpt.isPresent()) {
			throw new ExcecaoNegocio(String.format("Usuário de email '%s' inexistente!", email));
		}
		Usuario usuario = usuarioOpt.get();
		if (!usuario.isAtivado()) {
			throw new ExcecaoNegocio(String.format("Usuário '%s' não está ativado!", usuario.getLogin()));
		}

		// enviaEmail
		return enviadorEmailService.enviaEmailRecuperaSenha(usuario, sistema);
	}

	public void removerUsuario(Long idUsuario) {
		// somente usuário e admin podem realizar essa operação
		principalService.validaPermissaoUsuario(idUsuario);

		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		usuario.setAtivado(false);
		usuario.setDataExclusao(LocalDateTime.now(ZoneId.of("UTC-3")));
		usuarioRepository.save(usuario);
	}

	public Collection<Usuario> listarUsuarios(boolean ativos) {
		// somente admin pode realizar essa operação
		principalService.validaPermissaoUsuario(null);

		Stream<Usuario> stream = StreamSupport.stream(usuarioRepository.findAll().spliterator(), false);
		if (ativos) {
			stream = stream.filter(usuario -> usuario.isAtivado());
		}
		return stream.collect(Collectors.toList());
	}

	public java.io.File downloadFoto(Long idUsuario, boolean thumb) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		return googleDriveFachada.downloadFile(thumb ? usuario.getIdDriveFotoThumb() : usuario.getIdDriveFoto(), "foto.jpg");
	}

	public Usuario uploadFoto(Long idUsuario, MultipartFile mfile) {
		// somente usuário e admin podem realizar essa operação
		principalService.validaPermissaoUsuario(idUsuario);

		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		String idDriveFotoAnterior = usuario.getIdDriveFoto();
		String idDriveFotoThumbAnterior = usuario.getIdDriveFotoThumb();
		EnumSistema sistema = EnumSistema.valueOf(usuario.getSistema());

		// upload
		googleDriveFachada.uploadFile(idUsuario, mfile, sistema.getUploadFolder(), usuario, sistema);

		// delete anteriores
		if (idDriveFotoAnterior != null) {
			googleDriveFachada.deleteFile(idDriveFotoAnterior);
		}
		if (idDriveFotoThumbAnterior != null) {
			googleDriveFachada.deleteFile(idDriveFotoThumbAnterior);
		}

		usuarioRepository.save(usuario);

		// atualiza token
		usuario.setToken(autenticacaoService.criaTokenJwt(usuario));

		// enviaEmail
		enviadorEmailService.enviaEmailAtualizacaoDados(usuario);
		return usuario;
	}

	public boolean deleteFoto(Long idUsuario) {
		// somente usuário e admin podem realizar essa operação
		principalService.validaPermissaoUsuario(idUsuario);

		boolean removeu = false;
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		if (usuario.getIdDriveFoto() != null) {
			removeu = googleDriveFachada.deleteFile(usuario.getIdDriveFoto());
			if (usuario.getIdDriveFotoThumb() != null) {
				removeu = googleDriveFachada.deleteFile(usuario.getIdDriveFotoThumb());
			}
			if (removeu) {
				usuario.setUrlFoto(null);
				usuario.setUrlFotoThumb(null);
				usuarioRepository.save(usuario);
			}
		}
		return removeu;
	}

	public ArquivoUsuario uploadArquivo(Long idUsuario, MultipartFile mfile, String atributos) {
		// somente usuário e admin podem realizar essa operação
		principalService.validaPermissaoUsuario(idUsuario);

		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		EnumSistema sistema = EnumSistema.valueOf(usuario.getSistema());
		ArquivoUsuario arquivo = new ArquivoUsuario();
		arquivo.setUsuario(usuario);
		arquivo.setAtributos(atributos);

		// upload
		googleDriveFachada.uploadFile(idUsuario, mfile, sistema.getUploadFolder(), arquivo, sistema);

		return arquivoUsuarioRepository.save(arquivo);
	}

	public java.io.File downloadArquivo(String idDrive, boolean thumb) {
		ArquivoUsuario arquivo = arquivoUsuarioRepository.findByIdDrive(idDrive).get();
		return googleDriveFachada.downloadFile(thumb ? arquivo.getIdDriveThumb() : idDrive, arquivo.getNome());
	}

	public boolean deleteArquivo(Long idUsuario, String idDrive) {
		// somente usuário e admin podem realizar essa operação
		principalService.validaPermissaoUsuario(idUsuario);

		ArquivoUsuario arquivo = arquivoUsuarioRepository.findByIdDrive(idDrive).get();
		if (!arquivo.getUsuario().getId().equals(idUsuario)) {
			throw new ExcecaoNegocio("Apenas o próprio Usuário pode remover esse arquivo!");
		}
		boolean removeu = googleDriveFachada.deleteFile(arquivo.getIdDrive());
		if (removeu) {
			if (arquivo.getIdDriveThumb() != null) {
				removeu = googleDriveFachada.deleteFile(arquivo.getIdDriveThumb());
			}
			arquivoUsuarioRepository.delete(arquivo);
		}
		return removeu;
	}

	public Collection<ArquivoUsuario> recuperaArquivosComAtributos(Long idUsuario, String atributos) {
		return arquivoUsuarioRepository.findAllByUsuarioIdAndAtributosContaining(idUsuario, atributos);
	}

	private void atualizaUsuario(Usuario usuarioBd, Usuario usuario) {
		usuarioBd.setSistema(usuario.getSistema());
		usuarioBd.setLogin(usuario.getLogin());
		usuarioBd.setEmail(usuario.getEmail());
		usuarioBd.setNome(usuario.getNome());
		usuarioBd.setSenha(Base64.getEncoder().encodeToString(usuario.getSenha().getBytes()));
		usuarioBd.setAtivado(usuario.isAtivado());
		usuarioBd.setAdmin(usuario.isAdmin());

		usuarioBd.setTelefone(usuario.getTelefone());
		usuarioBd.setCelular(usuario.getCelular());
		usuarioBd.setInstagram(usuario.getInstagram());
		usuarioBd.setFacebook(usuario.getFacebook());
		usuarioBd.setCpf(usuario.getCpf());
	}

	private void validaUsuario(Usuario usuario) {
		if (usuario.getCpf() != null && !this.isCPFValido(usuario.getCpf())) {
			throw new ExcecaoNegocio("CPF inválido!");
		}
	}

	private boolean isCPFValido(String CPF) {
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
				CPF.equals("22222222222") || CPF.equals("33333333333") ||
				CPF.equals("44444444444") || CPF.equals("55555555555") ||
				CPF.equals("66666666666") || CPF.equals("77777777777") ||
				CPF.equals("88888888888") || CPF.equals("99999999999") ||
				(CPF.length() != 11)) {
			return false;
		}

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) {
				dig10 = '0';
			} else {
				dig10 = (char) (r + 48); // converte no respectivo caractere numerico
			}

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) {
				dig11 = '0';
			} else {
				dig11 = (char) (r + 48);
			}

			// Verifica se os digitos calculados conferem com os digitos informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
				return true;
			} else {
				return false;
			}
		} catch (InputMismatchException erro) {
			return false;
		}
	}

}
