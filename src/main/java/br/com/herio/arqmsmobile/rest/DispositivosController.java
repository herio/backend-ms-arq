package br.com.herio.arqmsmobile.rest;

import java.util.Collection;

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
import br.com.herio.arqmsmobile.service.DispositivoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("DispositivosController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/dispositivos")
public class DispositivosController {
	@Autowired
	protected DispositivoRepository dispositivoRepository;

	@Autowired
	protected DispositivoService dispositivoService;

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
		return dispositivoService.salvarDispositivo(idUsuario, dispositivo);
	}

	@ApiOperation("removerDispositivo")
	@DeleteMapping("/{id}")
	public boolean removerDispositivo(@PathVariable Long idUsuario, @PathVariable Long id) {
		return dispositivoService.removerDispositivo(idUsuario, id);
	}
}
