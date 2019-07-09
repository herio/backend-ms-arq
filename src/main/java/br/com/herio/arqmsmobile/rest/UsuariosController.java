package br.com.herio.arqmsmobile.rest;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Api("UsuariosController")
@RestController
public class UsuariosController {

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected UsuarioService usuarioService;

	@ApiOperation("criarUsuario")
	@PostMapping("/publico/usuarios/{sistema}")
	public Usuario criarUsuario(@RequestBody Usuario usuario, @PathVariable EnumSistema sistema) {
		return usuarioService.criarUsuario(usuario, sistema);
	}

	@ApiOperation("recuperarSenha")
	@GetMapping("/publico/usuarios/senha/{sistema}")
	public String recuperarSenha(@RequestParam String login, @PathVariable EnumSistema sistema) {
		return usuarioService.recuperarSenha(login, sistema);
	}

	@ApiOperation("atualizarUsuario")
	@PostMapping("/usuarios/{idUsuario}/{sistema}")
	public Usuario atualizarUsuario(@PathVariable Long idUsuario, @PathVariable EnumSistema sistema,
			@RequestBody Usuario usuario) {
		return usuarioService.atualizarUsuario(idUsuario, sistema, usuario);
	}

	@PostMapping("/usuarios/{idUsuario}/foto/{sistema}")
	public Usuario uploadFoto(@PathVariable Long idUsuario, @PathVariable EnumSistema sistema, @RequestParam("foto") MultipartFile file) {
		return usuarioService.uploadFoto(idUsuario, sistema, file);
	}

	@ApiOperation("removerUsuario")
	@DeleteMapping("/usuarios/{idUsuario}")
	public void removerUsuario(@PathVariable Long idUsuario) {
		if (idUsuario == null) {
			throw new IllegalArgumentException("Informe um usuário já existente (com id)!");
		}
		usuarioRepository.deleteById(idUsuario);
	}

	@ApiOperation("listarUsuarios")
	@GetMapping("/usuarios")
	public Collection<Usuario> listarUsuarios() {
		return StreamSupport.stream(usuarioRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}
}
