package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.service.ConfiguracaoNotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacao;
import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacaoRepository;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("ConfiguracaoNotificacaoController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/configuracaonotificacao")
public class ConfiguracaoNotificacaoController {
	@Autowired
	protected ConfiguracaoNotificacaoRepository configuracaoNotificacaoRepository;

	@Autowired
	protected ConfiguracaoNotificacaoService configuracaoNotificacaoService;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@ApiOperation("recuperarConfiguracao")
	@GetMapping
	public ConfiguracaoNotificacao recuperarConfiguracao(@PathVariable Long idUsuario) {
		return configuracaoNotificacaoService.recuperarConfiguracao(idUsuario);
	}

	@ApiOperation("salvarConfiguracao")
	@PostMapping
	public ConfiguracaoNotificacao salvarConfiguracao(@PathVariable Long idUsuario, @RequestBody ConfiguracaoNotificacao configuracaoNotificacao) {
		return configuracaoNotificacaoService.salvarConfiguracao(idUsuario, configuracaoNotificacao);
	}

	@ApiOperation("removerConfiguracao")
	@DeleteMapping("/{id}")
	public void removerConfiguracao(@PathVariable Long id) {
		configuracaoNotificacaoRepository.deleteById(id);
	}
}
