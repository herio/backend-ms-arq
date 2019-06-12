package br.com.herio.arqmsmobile.rest;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("UsuariosController")
@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@ApiOperation("salvarUsuario")
	@PostMapping
	public Usuario salvarUsuario(@RequestBody Usuario usuario) {
		usuario.valida();
		if(usuario.getId() == null) {
			//salva
			return usuarioRepository.save(usuario);
		} else {
			//atualiza
			Usuario usuarioBd = usuarioRepository.findById(usuario.getId()).get();
			BeanUtils.copyProperties(usuario, usuarioBd);
			return usuarioRepository.save(usuarioBd);	
		}
	}

	@ApiOperation("listarUsuarios")
	@GetMapping
	public Collection<Usuario> listarUsuarios() {
		return StreamSupport.stream(usuarioRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}
}
