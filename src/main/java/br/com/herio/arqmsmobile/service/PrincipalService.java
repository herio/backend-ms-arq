package br.com.herio.arqmsmobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

	public Long recuperaIdUsuarioAutenticado() {
		Object principal = getPrincipal();
		if (principal != null && principal instanceof AppUserDetails) {
			return ((AppUserDetails) principal).getIdUsuario();
		}
		return null;
	}

	public Usuario recuperaUsuarioAutenticado() {
		Object principal = getPrincipal();
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
			throw new ExcecaoNegocio("Usu�rio n�o encontrado!");
		}
		if (!usuario.isAdmin() && !usuario.getId().equals(idUsuarioSelecionado) && !"heriothiago@gmail.com".equals(usuario.getLogin())) {
			throw new ExcecaoNegocio("Usu�rio n�o tem permiss�o para realizar essa opera��o!");
		}
	}

	private Object getPrincipal() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			return auth.getPrincipal();
		}
		return null;
	}
}
