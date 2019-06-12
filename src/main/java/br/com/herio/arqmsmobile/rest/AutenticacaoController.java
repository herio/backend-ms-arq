package br.com.herio.arqmsmobile.rest;

import java.net.URI;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dto.DtoAutenticacao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("LoginController")
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	@Autowired
	private AutenticacaoService autenticacaoService;

	@ApiOperation("autenticar")
	@PostMapping
	public Usuario autenticar(@RequestBody DtoAutenticacao dtoAutenticacao) {
		return autenticacaoService.autenticarUsuario(dtoAutenticacao);
	}
}
