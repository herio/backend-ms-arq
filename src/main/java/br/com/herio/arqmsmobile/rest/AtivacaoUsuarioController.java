package br.com.herio.arqmsmobile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.service.AtivacaoUsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("AtivacaoUsuarioController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/ativacao")
public class AtivacaoUsuarioController {

    @Autowired
    protected AtivacaoUsuarioService ativacaoUsuarioService;
    
    @ApiOperation("confirmarAtivacaoUsuario")
    @GetMapping("/confirmarAtivacaoUsuario")
    public boolean confirmarAtivacaoUsuario(@PathVariable Long idUsuario, @RequestParam String chave) {
    	ativacaoUsuarioService.confirmarAtivacaoUsuario(idUsuario, chave);
    	return true;
    }

    @ApiOperation("gerarAtivacaoUsuario")
    @GetMapping("/gerarAtivacaoUsuario")
    public boolean gerarAtivacaoUsuario(@PathVariable Long idUsuario) {
    	ativacaoUsuarioService.gerarAtivacaoUsuario(idUsuario);
    	return true;
    }
}
