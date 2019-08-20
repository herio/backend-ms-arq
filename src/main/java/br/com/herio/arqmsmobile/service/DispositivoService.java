package br.com.herio.arqmsmobile.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.DispositivoRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dto.EnumSistema;

@Service
public class DispositivoService {
	@Autowired
	protected DispositivoRepository dispositivoRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected AutenticacaoService autenticacaoService;

	public Dispositivo salvarDispositivo(Long idUsuario, Dispositivo dispositivo) {
		if (dispositivo.getId() == null) {
			Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findByNumRegistroAndSo(dispositivo.getNumRegistro(), dispositivo.getSo());
			if (dispositivoOpt.isPresent()) {
				// dispositivo excluido anteriormente
				dispositivo = dispositivoOpt.get();
				dispositivo.setDataExclusao(null);
			}
			dispositivo.setUsuario(usuarioRepository.findById(idUsuario).get());
		} else {
			// atualiza registrationID e retira data exclusao
			String novoNumRegistro = dispositivo.getNumRegistro();
			dispositivo = dispositivoRepository.findById(dispositivo.getId()).get();
			dispositivo.setNumRegistro(novoNumRegistro);
			dispositivo.setDataExclusao(null);
		}
		dispositivo.valida();
		return dispositivoRepository.save(dispositivo);
	}

	public boolean removerDispositivo(@PathVariable Long id) {
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
		}
		return usuario;
	}
}
