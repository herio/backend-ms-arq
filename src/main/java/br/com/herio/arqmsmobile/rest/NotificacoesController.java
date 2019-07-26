package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.Notificacao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("NotificacoesController")
@RestController
@RequestMapping("/notificacoes")
public class NotificacoesController {

	@Autowired
	private NotificacaoService notificacaoService;

	@ApiOperation("enviaNotificacao")
	@PostMapping("/envia")
	public boolean enviaNotificacao(@RequestBody Notificacao notificacao) {
		return notificacaoService.enviaNotificacao(notificacao);
	}
}
