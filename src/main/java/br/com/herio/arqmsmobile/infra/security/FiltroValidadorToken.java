package br.com.herio.arqmsmobile.infra.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class FiltroValidadorToken extends UsernamePasswordAuthenticationFilter {

    private static final String MSG_TOKEN_NAO_INFORMADO = "Token de autorização não enviado";

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TIPO_TOKEN_HEADER = "Bearer";

    @Autowired
    private UserDetailsFromToken userDetailsFromToken;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        String token;
        try {
            token = extraiTokenDoHeader(request);
        } catch (BadCredentialsException ex) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Se o token nao existir no request deixa passar direto.
        // No momento em que usuario eh efetivamente autenticado no Spring Security este filtro eh novamente disparado, por isso
        // testamos se o contexto de seguranca do Spring Security jah contem a Authentication, o que significa que o usuario jah
        // foi autenticado, e nesta situacao tambem deixamos o filtro passar direto.
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = userDetailsFromToken.criaUsuario(token);
            } catch (AuthenticationException ex) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private String extraiTokenDoHeader(ServletRequest request) {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(TOKEN_HEADER);
        String tokenJwt = null;
        if (authToken != null && !authToken.startsWith("Basic")) {
            if (authToken.toLowerCase().startsWith(TIPO_TOKEN_HEADER.toLowerCase())) {
                tokenJwt = StringUtils.substringAfter(authToken, " ");
            } else {
                throw new BadCredentialsException("erro ao extrair token do header");
            }
            if (StringUtils.isBlank(tokenJwt)) {
                throw new BadCredentialsException(MSG_TOKEN_NAO_INFORMADO);
            }
        }
        return tokenJwt;
    }

}
