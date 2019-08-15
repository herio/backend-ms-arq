package br.com.herio.arqmsmobile.rest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.DispositivoRepository;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("DispositivosController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/dispositivos")
public class DispositivosController {
	@Autowired
	protected DispositivoRepository dispositivoRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@ApiOperation("listarDispositivos")
	@GetMapping
	public Collection<Dispositivo> listarDispositivos(@PathVariable Long idUsuario,
			@RequestParam(required = false) boolean exibirAtivos) {
		return exibirAtivos ? dispositivoRepository.findAllByUsuarioIdAndDataExclusaoIsNull(idUsuario)
				: dispositivoRepository.findAllByUsuarioId(idUsuario);
	}

	@ApiOperation("salvarDispositivo")
	@PostMapping
	public Dispositivo salvarDispositivo(@PathVariable Long idUsuario, @RequestBody Dispositivo dispositivo) {
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

	@ApiOperation("removerDispositivo")
	@DeleteMapping("/{id}")
	public boolean removerDispositivo(@PathVariable Long id) {
		Dispositivo dispositivo = dispositivoRepository.findById(id).get();
		dispositivo.setDataExclusao(LocalDateTime.now(ZoneId.of("UTC-3")));
		dispositivoRepository.save(dispositivo);
		return true;
	}
}
