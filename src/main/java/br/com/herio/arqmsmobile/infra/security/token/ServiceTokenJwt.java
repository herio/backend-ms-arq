package br.com.herio.arqmsmobile.infra.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(100)
public class ServiceTokenJwt {

    @Autowired
    private ChaveHMACService keyService;

    public String tokenSegurancaToTokenJwt(TokenSeguranca tokenSeguranca) {

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", tokenSeguranca.getEmissorToken());
        claims.put("sub", tokenSeguranca.getUsuarioLogado());
        claims.put("created", tokenSeguranca.getCriacaoToken());
        claims.put("exp", tokenSeguranca.getExpiracaoToken());
        // As claims abaixo sao adicionais, customiizacoes do TCU
        claims.put("roles", tokenSeguranca.getRoles());
        // cod = codigo-usuario-siga
        claims.put("id", Long.valueOf(tokenSeguranca.getIdUsuario()));
        // nus = nome-usuario-logado-siga
        claims.put("nus", tokenSeguranca.getNomeUsuarioLogado());

        return Jwts.builder()
                .setClaims(claims)
                // .setExpiration(tokenSeguranca.getDataCriacaoToken())
                .signWith(SignatureAlgorithm.HS512, keyService.getSecretKey())
                // .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    public TokenSeguranca tokenJwtToTokenSeguranca(String tokenJwt) {
        final Claims claims = parseClaimsJwt(tokenJwt);
        Date expiracaoToken = new Date(claims.getExpiration().getTime() / 1000);
        long criacaoToken = claims.get("created", Long.class);
        Date dataCriacaoToken = new Date(criacaoToken);
        HashSet<String> roles = new HashSet<String>(claims.get("roles", ArrayList.class));
        int idUsuario = claims.get("id", Integer.class);
        String nomeUsuarioLogado = claims.get("nus", String.class);
        String usuarioLogado = claims.getSubject();
        String emissorToken = claims.getIssuer();

        return new TokenSeguranca(expiracaoToken, criacaoToken, dataCriacaoToken, idUsuario, nomeUsuarioLogado, usuarioLogado,
                roles, emissorToken);
    }


    private Claims parseClaimsJwt(String tokenJwt) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(keyService.getSecretKey())
                    .parseClaimsJws(tokenJwt)
                    .getBody();
        } catch (JwtException jwtException) {
            throw new AccessDeniedException("Token inválido!", jwtException);
        }
        return claims;
    }
}
