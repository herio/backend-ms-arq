package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.dominio.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Api("ConfiguracoesNotificacoesController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/configuracoesnotificacoes")
public class ConfiguracoesNotificacoesController {
	@Autowired
	protected ConfiguracaoNotificacaoRepository configuracaoNotificacaoRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@ApiOperation("recuperarConfiguracao")
	@GetMapping
	public ConfiguracaoNotificacao recuperarConfiguracao(@PathVariable Long idUsuario) {
		return configuracaoNotificacaoRepository.findByUsuarioId(idUsuario).get();
	}

	@ApiOperation("salvarConfiguracao")
	@PostMapping
	public ConfiguracaoNotificacao salvarConfiguracao(@PathVariable Long idUsuario, @RequestBody ConfiguracaoNotificacao configuracaoNotificacao) {
		configuracaoNotificacao.setUsuario(usuarioRepository.findById(idUsuario).get());
		return configuracaoNotificacaoRepository.save(configuracaoNotificacao);
	}

	@ApiOperation("removerConfiguracao")
	@DeleteMapping("/{id}")
	public void removerConfiguracao(@PathVariable Long id) {
		configuracaoNotificacaoRepository.deleteById(id);
	}
}
