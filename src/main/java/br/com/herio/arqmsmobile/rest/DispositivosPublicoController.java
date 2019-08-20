package br.com.herio.arqmsmobile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.service.DispositivoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("DispositivosController")
@RestController
@RequestMapping("/publico/dispositivos/{sistema}")
public class DispositivosPublicoController {
	@Autowired
	private DispositivoService dispositivoService;

	@ApiOperation("criaUsuarioeSalvaDispositivo")
	@PostMapping
	public Dispositivo criaUsuarioeSalvaDispositivo(@RequestBody Dispositivo dispositivo, @PathVariable EnumSistema sistema) {
		return dispositivoService.criaUsuarioeSalvaDispositivo(dispositivo, sistema);
	}

}
