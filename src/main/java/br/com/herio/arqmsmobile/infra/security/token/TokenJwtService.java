package br.com.herio.arqmsmobile.infra.security.token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.infra.security.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenJwtService {

	@Autowired
	private ChaveHMACService keyService;

	public String tokenSegurancaToTokenJwt(TokenSeguranca tokenSeguranca) {

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("iss", tokenSeguranca.getEmissorToken());
		claims.put("sub", tokenSeguranca.getLoginUsuario());
		claims.put("created", tokenSeguranca.getDataCriacaoToken().getTime());
		claims.put("exp", tokenSeguranca.getDataExpiracaoToken().getTime());
		// As claims abaixo sao adicionais
		claims.put("roles", tokenSeguranca.getRoles());
		// idUsuario
		claims.put("id", Long.valueOf(tokenSeguranca.getIdUsuario()));
		// nu = nome-usuario
		claims.put("nu", tokenSeguranca.getNomeUsuario());

		return Jwts.builder().setClaims(claims)
				// .setExpiration(tokenSeguranca.getDataCriacaoToken())
				.signWith(SignatureAlgorithm.HS512, keyService.getSecretKey())
				// .compressWith(CompressionCodecs.GZIP)
				.compact();
	}

	@SuppressWarnings("unchecked")
	public TokenSeguranca tokenJwtToTokenSeguranca(String tokenJwt) {
		final Claims claims = parseClaimsJwt(tokenJwt);
		String emissorToken = claims.getIssuer();
		String loginUsuario = claims.getSubject();
		Date dataCriacaoToken = claims.getIssuedAt();
		Date dataExpiracaoToken = claims.getExpiration();
		HashSet<String> roles = new HashSet<String>(claims.get("roles", ArrayList.class));
		Long idUsuario = claims.get("id", Long.class);
		String nomeUsuario = claims.get("nu", String.class);

		return new TokenSeguranca(dataExpiracaoToken, dataCriacaoToken, idUsuario, nomeUsuario, loginUsuario, roles, emissorToken);
	}

	public UserDetails tokenJwtToUserDetais(String token) {

		Validate.notNull(token, "Nao foi possivel criar o usuario a partir do token porque o token esta nulo");
		TokenSeguranca tokenSeguranca = tokenJwtToTokenSeguranca(token);

		Long idUsuario = tokenSeguranca.getIdUsuario();
		String loginUsuario = tokenSeguranca.getLoginUsuario();
		String nomeUsuarioLogado = tokenSeguranca.getNomeUsuario();
		String password = null;
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		Set<String> roles = tokenSeguranca.getRoles();
		String[] rolesArray = roles.stream().map(str -> "ROLE_" + str).toArray(size -> new String[roles.size()]);

		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(rolesArray);

		return new AppUserDetails(idUsuario, nomeUsuarioLogado, loginUsuario, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
	}

	private Claims parseClaimsJwt(String tokenJwt) {
		Claims claims = null;
		try {
			claims = Jwts.parser().setSigningKey(keyService.getSecretKey()).parseClaimsJws(tokenJwt).getBody();
		} catch (JwtException jwtException) {
			throw new AccessDeniedException("Token invalido!", jwtException);
		}
		return claims;
	}
}
