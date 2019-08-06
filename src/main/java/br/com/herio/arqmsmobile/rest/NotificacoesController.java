package br.com.herio.arqmsmobile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.Notificacao;
import br.com.herio.arqmsmobile.service.NotificacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("NotificacoesController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/notificacoes")
public class NotificacoesController {

	@Autowired
	private NotificacaoService notificacaoService;

	@ApiOperation("enviarNotificacao")
	@PostMapping("/envia")
	public boolean enviarNotificacao(@RequestBody Notificacao notificacao) {
		return notificacaoService.salvarEEnviarNotificacao(notificacao);
	}

	@ApiOperation("atualizarNotificacao")
	@PostMapping("/{idNotificacao}")
	public Notificacao atualizarNotificacao(@PathVariable Long idNotificacao, @RequestBody Notificacao notificacao) {
		return notificacaoService.atualizarNotificacao(idNotificacao, notificacao);
	}

	@ApiOperation("listarNotificacoesEnviadasNaoExcluidas")
	@GetMapping("/enviadasnaoexcluidas")
	public Page<Notificacao> listarNotificacoesEnviadasNaoExcluidas(@PathVariable Long idUsuario, Pageable page) {
		return notificacaoService.listarNotificacoesEnviadasNaoExcluidas(idUsuario, page);
	}

	@ApiOperation("listarNotificacoes")
	@GetMapping
	public Page<Notificacao> listarNotificacoes(@PathVariable Long idUsuario, Pageable page) {
		return notificacaoService.listarNotificacoes(idUsuario, page);
	}

}
