package br.com.herio.arqmsmobile.rest;

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
