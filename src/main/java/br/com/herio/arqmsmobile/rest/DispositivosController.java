package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.dto.DtoDispositivo;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.feign.MPushDispositivosClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api("DispositivosController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/dispositivos")
public class DispositivosController {
    @Autowired
    protected MPushDispositivosClient mPushDispositivosClient;

    @ApiOperation("listarDispositivos")
    @GetMapping
    public Collection<DtoDispositivo> listarDispositivos(@PathVariable Long idUsuario, @RequestParam(required = false) boolean exibirAtivos,
            @RequestParam(required = false) EnumSistema sistema) {
        return mPushDispositivosClient.listar(idUsuario, exibirAtivos, sistema);
    }

    @ApiOperation("cadastrarDispositivo")
    @PostMapping
    public DtoDispositivo cadastrarDispositivo(@PathVariable Long idUsuario, @RequestBody DtoDispositivo dtoDispositivo) {
        return mPushDispositivosClient.cadastrar(idUsuario, dtoDispositivo);
    }

    @ApiOperation("removerDispositivo")
    @DeleteMapping("/{id}")
    public void removerDispositivo(@PathVariable Long idUsuario, @PathVariable Long id) {
        mPushDispositivosClient.remover(idUsuario, id);
    }
}
