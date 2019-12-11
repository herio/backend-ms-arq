package br.com.herio.arqmsmobile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

	@ApiOperation("enviarNotificacaoParaDispositivo")
	@PostMapping("/enviaparadispositivo")
	public boolean enviarNotificacaoParaDispositivo(@RequestParam(required = false) boolean versaoPaga, @RequestBody Notificacao notificacao) {
		return notificacaoService.salvarEEnviarNotificacaoParaDispositivo(notificacao, versaoPaga);
	}

	@ApiOperation("enviarNotificacao")
	@PostMapping("/envia")
	public boolean enviarNotificacao(@PathVariable Long idUsuario, @RequestBody Notificacao notificacao,
			@RequestParam(required = false) boolean versaoPaga, @RequestParam(required = false) boolean enviarEmail) {
		return notificacaoService.salvarEEnviarNotificacoes(notificacao.getTitulo(), notificacao.getConteudo(), notificacao.getDadosExtras(),
				idUsuario, versaoPaga, enviarEmail);
	}

	@ApiOperation("atualizarNotificacao")
	@PostMapping("/{idNotificacao}")
	public Notificacao atualizarNotificacao(@PathVariable Long idNotificacao, @RequestBody Notificacao notificacao) {
		return notificacaoService.atualizarNotificacao(idNotificacao, notificacao);
	}

    @ApiOperation("removerNotificacao")
    @DeleteMapping("/{idNotificacao}")
    public boolean removerNotificacao(@PathVariable Long idNotificacao) {
        return notificacaoService.removerNotificacao(idNotificacao);
    }

	@ApiOperation("listarNotificacoesEnviadasNaoExcluidas")
	@GetMapping("/enviadasnaoexcluidas")
	public Page<Notificacao> listarNotificacoesEnviadasNaoExcluidas(@PathVariable Long idUsuario,
			@RequestParam(required = false) String dadosExtras, Pageable page) {
		return notificacaoService.listarNotificacoesEnviadasNaoExcluidas(idUsuario, dadosExtras, page);
	}

	@ApiOperation("listarNotificacoes")
	@GetMapping
	public Page<Notificacao> listarNotificacoes(@PathVariable Long idUsuario, Pageable page) {
		return notificacaoService.listarNotificacoes(idUsuario, page);
	}

}
