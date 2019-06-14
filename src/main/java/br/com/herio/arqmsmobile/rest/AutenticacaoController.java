package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dto.DtoAutenticacao;
import br.com.herio.arqmsmobile.service.AutenticacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api("AutenticacaoController")
@RestController
@RequestMapping("/publico/autenticacao")
public class AutenticacaoController {
	
	@Autowired
	private AutenticacaoService autenticacaoService;

	@ApiOperation("autenticar")
	@PostMapping
	public Usuario autenticar(@RequestBody DtoAutenticacao dtoAutenticacao) {
		return autenticacaoService.autenticarUsuario(dtoAutenticacao);
	}
}
