package br.com.herio.arqmsmobile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("UsuariosPublicoController")
@RestController
@RequestMapping("/publico/usuarios")
public class UsuariosPublicoController {

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected UsuarioService usuarioService;

	@ApiOperation("root")
	@PostMapping("/root/{sistema}")
	public Usuario root(@PathVariable EnumSistema sistema) {
		return usuarioService.root(sistema);
	}

	@ApiOperation("criarUsuario")
	@PostMapping
	public Usuario criarUsuario(@RequestBody Usuario usuario) {
		return usuarioService.criarUsuario(usuario);
	}

	@ApiOperation("recuperarSenha")
	@GetMapping("/senha/{sistema}")
	public String recuperarSenha(@RequestParam String email, @PathVariable EnumSistema sistema) {
		return usuarioService.recuperarSenha(email, sistema);
	}
}
