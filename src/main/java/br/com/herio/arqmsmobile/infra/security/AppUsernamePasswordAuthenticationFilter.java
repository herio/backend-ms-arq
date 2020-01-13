package br.com.herio.arqmsmobile.infra.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;
import br.com.herio.arqmsmobile.infra.security.dto.DtoTokenAutenticacao;
import br.com.herio.arqmsmobile.infra.security.token.TokenJwtService;
import br.com.herio.arqmsmobile.service.PrincipalService;

public class AppUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	final private static Map<Long, DtoTokenAutenticacao> ULTIMAS_AUTENTICACOES = new HashMap<>();

	final private String MSG_TOKEN_NAO_INFORMADO = "Token de autorizacao nao enviado";
	final private String MSG_AUTHORIZATION_HEADER_INCORRETO = "Header do token de autorizacao esta incorreto ou inexistente";

	final private String TOKEN_HEADER = "Authorization";
	final private String TIPO_TOKEN_HEADER = "Bearer";

	@Autowired
	private TokenJwtService tokenJwtService;

	@Autowired
	private PrincipalService principalService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String token = extraiTokenDoHeader(request);
			// Se o token nao existir no request deixa passar direto.
			// No momento em que usuario eh efetivamente autenticado no Spring Security este
			// filtro eh novamente disparado, por isso
			// testamos se o contexto de seguranca do Spring Security jah contem a
			// Authentication, o que significa que o usuario jah
			// foi autenticado, e nesta situacao tambem deixamos o filtro passar direto.
			if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails;
				userDetails = tokenJwtService.tokenJwtToUserDetais(token);

				Long idUsuario = principalService.recuperaIdUsuarioAutenticado();
				if (!isAutenticacao(request) && idUsuario != null) {
					validaAutenticacaoSimultanea(idUsuario, token);
				}

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails
						.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (AccessDeniedException ade) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.getWriter().write(ade.getMessage());
			httpResponse.flushBuffer();
			return;
		}
		chain.doFilter(request, response);
	}

	private boolean isAutenticacao(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURL().toString();
		return url.contains("/publico/autenticacao");
	}

	private void criaDtoTokenAutenticacao(Long idUsuario, String token) {
		DtoTokenAutenticacao dtoTokenAutenticacao = new DtoTokenAutenticacao();
		dtoTokenAutenticacao.setIdUsuario(idUsuario);
		dtoTokenAutenticacao.setToken(token);
		dtoTokenAutenticacao.setDataHora(LocalDateTime.now());
		ULTIMAS_AUTENTICACOES.put(idUsuario, dtoTokenAutenticacao);
	}

	private void validaAutenticacaoSimultanea(Long idUsuario, String token) {
		DtoTokenAutenticacao dtoTokenAutenticacao = ULTIMAS_AUTENTICACOES.get(idUsuario);
		if (dtoTokenAutenticacao == null) {
			criaDtoTokenAutenticacao(idUsuario, token);
		} else {
			LocalDateTime dataLimite = LocalDateTime.now().minusHours(1);
			if (!dtoTokenAutenticacao.getToken().equals(token) && dtoTokenAutenticacao.getDataHora().isAfter(dataLimite)) {
				throw new ExcecaoNegocio("Você já está autenticado em outro dispositivo, faça uma nova autenticação para "
						+ "acessar o app a partir desse dispositivo!");
			} else {
				ULTIMAS_AUTENTICACOES.remove(idUsuario);
			}
		}
	}

	private String extraiTokenDoHeader(ServletRequest request) {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String authToken = httpRequest.getHeader(this.TOKEN_HEADER);
		String tokenJwt = null;
		if (authToken != null && !authToken.startsWith("Basic")) {
			if ("".equals(authToken)) {
				throw new AccessDeniedException(MSG_AUTHORIZATION_HEADER_INCORRETO);
			}
			if (authToken.toLowerCase().startsWith(this.TIPO_TOKEN_HEADER.toLowerCase())) {
				tokenJwt = StringUtils.substringAfter(authToken, " ");
			} else {
				throw new AccessDeniedException(MSG_AUTHORIZATION_HEADER_INCORRETO);
			}
			if (tokenJwt == null || "".equals(tokenJwt)) {
				throw new AccessDeniedException(MSG_TOKEN_NAO_INFORMADO);
			}
		}
		return tokenJwt;
	}

}
