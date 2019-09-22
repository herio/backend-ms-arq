package br.com.herio.arqmsmobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;
import br.com.herio.arqmsmobile.infra.security.AppUserDetails;

@Service
public class PrincipalService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario recuperaUsuarioAutenticado() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && principal instanceof AppUserDetails) {
			Long idUsuario = ((AppUserDetails) principal).getIdUsuario();
			return usuarioRepository.findById(idUsuario).get();
		}
		return null;
	}

	public void validaPermissaoUsuario(Long idUsuarioSelecionado) {
		Usuario usuario = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && principal instanceof AppUserDetails) {
			Long idUsuario = ((AppUserDetails) principal).getIdUsuario();
			usuario = usuarioRepository.findById(idUsuario).get();
		}
		if (usuario == null) {
			throw new ExcecaoNegocio("Usuário não encontrado!");
		}
		if (!usuario.isAdmin() && !usuario.getId().equals(idUsuarioSelecionado)) {
			throw new ExcecaoNegocio("Usuário não tem permissão para realizar essa operação!");
		}
	}

}
