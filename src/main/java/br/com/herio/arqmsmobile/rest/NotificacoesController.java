package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.dto.DtoNotificacao;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.feign.MPushNotificacoesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api("NotificacoesController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/notificacoes")
public class NotificacoesController {

    @Autowired
    protected MPushNotificacoesClient mPushNotificacoesClient;

    @ApiOperation("recuperarRecebimentoNotificacao")
    @GetMapping("/{sistema}/recebimento")
    public boolean recuperarRecebimentoNotificacao(@PathVariable Long idUsuario, @PathVariable EnumSistema sistema) {
        return mPushNotificacoesClient.recuperarRecebimento(idUsuario, sistema);
    }

    @ApiOperation("atualizarRecebimentoNotificacao")
    @PostMapping("/{sistema}/recebimento")
    public String atualizarRecebimentoNotificacao(@PathVariable Long idUsuario, @PathVariable EnumSistema sistema, @RequestBody boolean receberNotificacao) {
        return mPushNotificacoesClient.atualizarRecebimento(idUsuario, sistema, receberNotificacao);
    }

    @ApiOperation("limparNotificacoesPendentesDeVisualizacao")
    @PostMapping("/{sistema}/limparpendentesdevisualizacao")
    public String limparNotificacoesPendentesDeVisualizacao(@PathVariable Long idUsuario, @PathVariable EnumSistema sistema, @RequestBody Long[] idsNotificacoes) {
        return mPushNotificacoesClient.limparPendentesDeVisualizacao(idUsuario, sistema, idsNotificacoes);
    }

    @ApiOperation("enviarNotificacao")
    @PostMapping("/{sistema}")
    public boolean enviarNotificacao(@PathVariable("idUsuario") Long idUsuario, @PathVariable("sistema") EnumSistema sistema, @RequestBody DtoNotificacao dtoNotificacao) {
        return mPushNotificacoesClient.enviar(idUsuario, sistema, dtoNotificacao);
    }

    @ApiOperation("excluirNotificacao")
    @DeleteMapping("/{sistema}")
    public String excluirNotificacao(@PathVariable Long idUsuario, @PathVariable EnumSistema sistema, @RequestBody Long[] idsNotificacoes) {
        return mPushNotificacoesClient.excluir(idUsuario, sistema, idsNotificacoes);
    }

    @ApiOperation("listarNotificacoes")
    @GetMapping("/{sistema}")
    public Collection<DtoNotificacao> listarNotificacoes(@PathVariable Long idUsuario, @PathVariable EnumSistema sistema, @RequestParam Integer page, @RequestParam Integer limit) {
        return mPushNotificacoesClient.listar(idUsuario, sistema, page, limit);
    }

}