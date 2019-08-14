package br.com.herio.arqmsmobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.infra.security.AppUserDetails;

@Service
public class PrincipalService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario recuperaUsuarioAutenticado() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && principal instanceof UsernamePasswordAuthenticationToken &&
				((UsernamePasswordAuthenticationToken) principal).getPrincipal() instanceof AppUserDetails) {
			AppUserDetails appPrincipal = (AppUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
			Long idUsuario = appPrincipal.getIdUsuario();
			return usuarioRepository.findById(idUsuario).get();
		}
		return null;
	}

}
