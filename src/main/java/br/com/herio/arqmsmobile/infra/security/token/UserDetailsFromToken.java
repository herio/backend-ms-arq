package br.com.herio.arqmsmobile.infra.security.token;

import br.com.herio.arqmsmobile.infra.security.AppUserDetails;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsFromToken {

    @Autowired
    private ServiceTokenJwt serviceTokenJwt;

    @Autowired
    private ChaveHMACService keyService;

    public UserDetails criaUsuario(String token) {

        Validate.notNull(token, "Não foi possível criar o usuário a partir do token porque o token está nulo");
        TokenSeguranca tokenSeguranca = serviceTokenJwt.tokenJwtToTokenSeguranca(token);

        Integer codigoUsuario = tokenSeguranca.getIdUsuario();
        String login = tokenSeguranca.getUsuarioLogado();
        String nomeUsuarioLogado = tokenSeguranca.getNomeUsuarioLogado();
        String password = null;
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        Set<String> roles = tokenSeguranca.getRoles();
        String[] rolesArray = roles.stream().map(str -> "ROLE_" + str).toArray(size -> new String[roles.size()]);

        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(rolesArray);

        return new AppUserDetails(codigoUsuario, nomeUsuarioLogado, login, password, authorities,
                accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }


}
