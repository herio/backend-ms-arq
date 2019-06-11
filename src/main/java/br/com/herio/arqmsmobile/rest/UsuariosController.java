package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("UsuariosController")
@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private UsuarioRepository usuarioDao;

	@ApiOperation("salvarUsuario")
	@PostMapping
	public Usuario salvarUsuario(@RequestBody Usuario usuario) {
		usuario.valida();
		return usuarioDao.save(usuario);
	}
}
