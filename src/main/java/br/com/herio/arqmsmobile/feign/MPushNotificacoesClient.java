package br.com.herio.arqmsmobile.feign;

import br.com.herio.arqmsmobile.dto.DtoNotificacao;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@FeignClient(name = "MPushNotificacoesClient", url = "${integracao.urlRSMPush}")
public interface MPushNotificacoesClient {

    @GetMapping("/usuarios/{idUsuario}/notificacoes/{sistema}/recebimento")
    boolean recuperarRecebimento(@PathVariable("idUsuario") Long idUsuario, @PathVariable("sistema") EnumSistema sistema);

    @PostMapping("/usuarios/{idUsuario}/notificacoes/{sistema}/recebimento")
    String atualizarRecebimento(@PathVariable("idUsuario") Long idUsuario, @PathVariable("sistema") EnumSistema sistema,
        @RequestBody boolean receberNotificacao);

    @PostMapping("/usuarios/{idUsuario}/notificacoes/{sistema}/limparpendentesdevisualizacao")
    String limparPendentesDeVisualizacao(@PathVariable("idUsuario") Long idUsuario, @PathVariable("sistema") EnumSistema sistema,
        @RequestBody Long[] idsNotificacoes);

    @GetMapping("/usuarios/{idUsuario}/notificacoes/{sistema}")
    public Collection<DtoNotificacao> listar(@PathVariable("idUsuario") Long idUsuario, @PathVariable("sistema") EnumSistema sistema, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit);

    @PostMapping("/usuarios/{idUsuario}/notificacoes/{sistema}")
    boolean enviar(@PathVariable("idUsuario") Long idUsuario, @PathVariable("sistema") EnumSistema sistema, @RequestBody DtoNotificacao dtoNotificacao);

    @DeleteMapping("/usuarios/{idUsuario}/notificacoes/{sistema}")
    String excluir(@PathVariable("idUsuario") Long idUsuario, @PathVariable("sistema") EnumSistema sistema, @RequestBody Long[] idsNotificacoes);
}
