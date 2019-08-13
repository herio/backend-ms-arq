package br.com.herio.arqmsmobile.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dto.DtoAutenticacao;
import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;
import br.com.herio.arqmsmobile.infra.security.token.TokenJwtService;
import br.com.herio.arqmsmobile.infra.security.token.TokenSeguranca;

@Service
public class AutenticacaoService {

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected TokenJwtService tokenJwtService;

	public Usuario autenticarUsuario(DtoAutenticacao dtoAutenticacao) {
		Optional<Usuario> usuarioOpt = usuarioRepository
				.findByLoginAndSistema(dtoAutenticacao.getLogin(), dtoAutenticacao.getSistema());
		if (!usuarioOpt.isPresent()) {
			throw new ExcecaoNegocio(String.format("Usuário '%s' inexistente!", dtoAutenticacao.getLogin()));
		}
		Usuario usuario = usuarioOpt.get();
		if (!usuario.isAtivado()) {
			throw new ExcecaoNegocio(String.format("Usuário '%s' não está ativado!", dtoAutenticacao.getLogin()));
		}
		if (!usuario.getSenha().equals(Base64.getEncoder().encodeToString(dtoAutenticacao.getSenha().getBytes()))) {
			throw new ExcecaoNegocio("Senha inválida. Digite a senha corretamente!");
		}
		usuario.setToken(criaTokenJwt(usuario));
		return usuario;
	}

	public String criaTokenJwt(Usuario usuario) {
		TokenSeguranca tokenSeguranca = new TokenSeguranca(usuario.getId(), usuario.getNome(), usuario.getLogin());
		return "Bearer " + tokenJwtService.tokenSegurancaToTokenJwt(tokenSeguranca);
	}
}
