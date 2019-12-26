package br.com.herio.arqmsmobile.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.DispositivoRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.dto.EnumTipoSO;

@Service
public class DispositivoService {
	@Autowired
	protected DispositivoRepository dispositivoRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected AutenticacaoService autenticacaoService;

	@Autowired
	protected ConfiguracaoNotificacaoService configuracaoNotificacaoService;

	@Autowired
	private PrincipalService principalService;

	public Dispositivo criarAtualizarDispositivoUsuario(Usuario usuario, String numRegistro, EnumTipoSO so) {
		Dispositivo dispositivo = null;
		Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findByNumRegistroAndSo(numRegistro, so);
		if (dispositivoOpt.isPresent()) {
			// dispositivo existente - atualiza
			dispositivo = dispositivoOpt.get();
			dispositivo.setUsuario(usuario);
			dispositivo.setDataExclusao(null);
		} else {
			// dispositivo novo - cria
			dispositivo = new Dispositivo();
			dispositivo.setUsuario(usuario);
			dispositivo.setNumRegistro(numRegistro);
			dispositivo.setSo(so);
		}
		dispositivo.valida();
		return dispositivoRepository.save(dispositivo);
	}

	public Dispositivo salvarDispositivo(Long idUsuario, Dispositivo dispositivo) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		usuario.setToken(autenticacaoService.criaTokenJwt(usuario));
		if (dispositivo.getId() == null) {
			Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findByNumRegistroAndSo(dispositivo.getNumRegistro(), dispositivo.getSo());
			if (dispositivoOpt.isPresent()) {
				// dispositivo excluido anteriormente
				dispositivo = dispositivoOpt.get();
				dispositivo.setDataExclusao(null);
			}
		} else {
			// atualiza registrationID e retira data exclusao
			String novoNumRegistro = dispositivo.getNumRegistro();
			dispositivo = dispositivoRepository.findById(dispositivo.getId()).get();
			dispositivo.setNumRegistro(novoNumRegistro);
			dispositivo.setDataExclusao(null);
		}
		dispositivo.setUsuario(usuario);
		dispositivo.valida();
		return dispositivoRepository.save(dispositivo);
	}

	public boolean removerDispositivo(Long idUsuario, Long id) {
		// somente usuário e admin podem realizar essa operação
		principalService.validaPermissaoUsuario(idUsuario);

		Dispositivo dispositivo = dispositivoRepository.findById(id).get();
		dispositivo.setDataExclusao(LocalDateTime.now(ZoneId.of("UTC-3")));
		dispositivoRepository.save(dispositivo);
		return true;
	}

	public Dispositivo criaUsuarioeSalvaDispositivo(Dispositivo dispositivo, EnumSistema sistema) {
		Optional<Dispositivo> dispOpt = dispositivoRepository.findByNumRegistroAndSo(dispositivo.getNumRegistro(), dispositivo.getSo());
		Usuario usuario = null;
		Dispositivo dispositivoBd = null;
		if (dispOpt.isPresent()) {
			dispositivoBd = dispOpt.get();
			usuario = dispositivoBd.getUsuario();
		} else {
			usuario = recuperaUsuario(dispositivo.getNumRegistro(), sistema);
			dispositivoBd = salvarDispositivo(usuario.getId(), dispositivo);
		}
		usuario.setToken(autenticacaoService.criaTokenJwt(usuario));
		dispositivoBd.setUsuario(usuario);
		return dispositivoBd;
	}

	private Usuario recuperaUsuario(String numRegistro, EnumSistema sistema) {
		Usuario usuario;
		Optional<Usuario> usuarioOpt = usuarioRepository.findByLoginAndSistema(numRegistro, sistema.name());
		if (usuarioOpt.isPresent()) {
			usuario = usuarioOpt.get();
		} else {
			usuario = new Usuario();
			usuario.setSistema(sistema.name());
			usuario.setLogin(numRegistro);
			usuario = usuarioRepository.save(usuario);

			configuracaoNotificacaoService.criaConfiguracaoNotificacaoDefault(usuario.getId(), sistema);
		}
		return usuario;
	}
}
