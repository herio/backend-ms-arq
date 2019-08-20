package br.com.herio.arqmsmobile.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.DispositivoRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.service.AutenticacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("DispositivosController")
@RestController
@RequestMapping("/publico/dispositivos/{sistema}")
public class DispositivosPublicoController {
	@Autowired
	protected DispositivoRepository dispositivoRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected AutenticacaoService autenticacaoService;

	@ApiOperation("criaUsuarioeSalvaDispositivo")
	@PostMapping
	public Dispositivo criaUsuarioeSalvaDispositivo(@RequestBody Dispositivo dispositivo, @PathVariable EnumSistema sistema) {
		Optional<Dispositivo> dispOpt = dispositivoRepository.findByNumRegistroAndSo(dispositivo.getNumRegistro(), dispositivo.getSo());
		Usuario usuario = null;
		Dispositivo dispositivoBd = null;
		if (dispOpt.isPresent()) {
			dispositivoBd = dispOpt.get();
			usuario = dispositivoBd.getUsuario();
		} else {
			Optional<Usuario> usuarioOpt = usuarioRepository.findByLoginAndSistema(dispositivo.getNumRegistro(), sistema.name());
			if (usuarioOpt.isPresent()) {
				usuario = usuarioOpt.get();
			} else {
				usuario = new Usuario();
				usuario.setSistema(sistema.name());
				usuario.setLogin(dispositivo.getNumRegistro());
				usuarioRepository.save(usuario);
			}
			dispositivoBd = new Dispositivo();
			dispositivoBd.setNumRegistro(dispositivo.getNumRegistro());
			dispositivoBd.setSo(dispositivo.getSo());
			dispositivoBd.setUsuario(usuario);
			dispositivoBd.valida();
			dispositivoBd = dispositivoRepository.save(dispositivo);
		}
		usuario.setToken(autenticacaoService.criaTokenJwt(usuario));
		return dispositivoBd;
	}
}
