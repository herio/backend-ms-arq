package br.com.herio.arqmsmobile.feign;

import br.com.herio.arqmsmobile.dto.DtoDispositivo;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@FeignClient(name = "MPushDispositivosClient", url = "${integracao.urlRSMPush}")
public interface MPushDispositivosClient {

    @GetMapping("/usuarios/{idUsuario}/dispositivos")
    Collection<DtoDispositivo> listar(@PathVariable("idUsuario") Long idUsuario,
                                      @RequestParam(required = false, value = "exibirAtivos") boolean exibirAtivos,
                                      @RequestParam(required = false, value = "sistema") EnumSistema sistema);

    @PostMapping("/usuarios/{idUsuario}/dispositivos")
    DtoDispositivo cadastrar(@PathVariable("idUsuario") Long idUsuario, @RequestBody DtoDispositivo dtoDispositivo);

    @DeleteMapping("/usuarios/{idUsuario}/dispositivos/{id}")
    void remover(@PathVariable("idUsuario") Long idUsuario, @PathVariable("id") Long id);
}
