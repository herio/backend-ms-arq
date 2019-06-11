package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.DispositivoRepository;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Api("DispositivosController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/dispositivos")
public class DispositivosController {
    @Autowired
    protected DispositivoRepository dispositivoDao;

    @ApiOperation("listarDispositivos")
    @GetMapping
    public Collection<Dispositivo> listarDispositivos(@PathVariable Long idUsuario,
          @RequestParam(required = false) boolean exibirAtivos, @RequestParam(required = false) EnumSistema sistema) {
        return exibirAtivos? dispositivoDao.findByIdUsuarioSistemaAtivos(idUsuario, sistema)
                : dispositivoDao.findByIdUsuarioSistema(idUsuario, sistema);
    }

    @ApiOperation("salvarDispositivo")
    @PostMapping
    public Dispositivo salvarDispositivo(@PathVariable Long idUsuario, @RequestBody Dispositivo dispositivo) {
        if(dispositivo.getId() == null) {
            List<Dispositivo> dispositivos = dispositivoDao.findByIdUsuarioNumRegistroOsSistema(
                    dispositivo.getUsuario().getId(), dispositivo.getNumRegistro(),
                    dispositivo.getOs(), dispositivo.getSistema());
            if(dispositivos.isEmpty()) {
                //dispositivo novo
                dispositivo.setDataCadastro(new Date());
            } else {
                //dispositivo excluido anteriormente
                dispositivo = dispositivos.iterator().next();
                dispositivo.setDataExclusao(null);
            }
        } else {
            // atualiza registrationID e retira data exclusão
            String novoNumRegistro = dispositivo.getNumRegistro();
            dispositivo = dispositivoDao.findById(dispositivo.getId()).get();
            dispositivo.setNumRegistro(novoNumRegistro);
            dispositivo.setDataExclusao(null);
        }
        dispositivo.valida();
        return dispositivoDao.save(dispositivo);
    }

    @ApiOperation("removerDispositivo")
    @DeleteMapping("/{id}")
    public void removerDispositivo(@PathVariable Long id) {
        Dispositivo dispositivo = dispositivoDao.findById(id).get();
        dispositivo.setDataExclusao(new Date());
        dispositivoDao.save(dispositivo);
    }
}
