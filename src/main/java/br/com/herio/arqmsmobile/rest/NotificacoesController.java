package br.com.herio.arqmsmobile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.Notificacao;
import br.com.herio.arqmsmobile.service.NotificacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("NotificacoesController")
@RestController
public class NotificacoesController {

	@Autowired
	private NotificacaoService notificacaoService;

	@ApiOperation("enviaNotificacao")
	@PostMapping("/notificacoes/envia")
	public boolean enviaNotificacao(@RequestBody Notificacao notificacao) {
		return notificacaoService.enviaNotificacao(notificacao);
	}

	@ApiOperation("atualizaNotificacao")
	@PostMapping("/notificacoes")
	public Notificacao atualizaNotificacao(@RequestBody Notificacao notificacao) {
		return notificacaoService.atualizaNotificacao(notificacao);
	}

	@ApiOperation("listaNotificacoesEnviadasNaoExcluidas")
	@GetMapping("/usuarios/{idUsuario}/notificacoes")
	public Page<Notificacao> listaNotificacoesEnviadasNaoExcluidas(@PathVariable Long idUsuario, Pageable page) {
		return notificacaoService.listaNotificacoesEnviadasNaoExcluidas(idUsuario, page);
	}

}
