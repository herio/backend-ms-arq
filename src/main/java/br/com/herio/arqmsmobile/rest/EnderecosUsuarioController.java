package br.com.herio.arqmsmobile.rest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.EnderecoUsuario;
import br.com.herio.arqmsmobile.dominio.EnderecoUsuarioRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("EnderecosUsuarioController")
@RestController
@RequestMapping("/usuarios/{idUsuario}/enderecos")
public class EnderecosUsuarioController {
	@Autowired
	protected EnderecoUsuarioRepository enderecoUsuarioRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@ApiOperation("listarEnderecos")
	@GetMapping
	public Collection<EnderecoUsuario> listarEnderecos(@PathVariable Long idUsuario) {
		return enderecoUsuarioRepository.findAllByUsuarioId(idUsuario);
	}

	@ApiOperation("salvarEndereco")
	@PostMapping
	public EnderecoUsuario salvarDispositivo(@PathVariable Long idUsuario, @RequestBody EnderecoUsuario endereco) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		endereco.setUsuario(usuario);
		return enderecoUsuarioRepository.save(endereco);
	}

	@ApiOperation("removerEndereco")
	@DeleteMapping("/{idEndereco}")
	public boolean removerDispositivo(@PathVariable Long idEndereco) {
		enderecoUsuarioRepository.deleteById(idEndereco);
		return true;
	}
}
