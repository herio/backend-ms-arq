package br.com.herio.arqmsmobile.dominio;

import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(schema = "public", name = "USUARIO")
public class Usuario implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID")
    private Long id;

    @Column(name="LOGIN")
    private String login;

    @Column(name="SENHA")
    private String senha;

    @Column(name="NOME")
    private String nome;

    @Column(name="EMAIL")
    private String email;

    @Version
    @Column(name = "NUM_VERSAO_REGISTRO")
    private Long versao;

    public Usuario() {
        //default
    }

    public Usuario(String login, String senha, String nome, String email) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    //UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void valida() {
        StringBuilder msg = new StringBuilder();
        if(StringUtils.isEmpty(this.login)) {
            msg.append("Login inválido");
        }
        if(StringUtils.isEmpty(this.senha)) {
            msg.append("Senha inválida");
        }
        if(StringUtils.isEmpty(this.nome)) {
            msg.append("Nome inválido");
        }
        if(msg.length() > 0) {
            throw new ExcecaoNegocio(msg.toString());
        }
    }
}
