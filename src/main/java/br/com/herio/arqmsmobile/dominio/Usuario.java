package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;

@Entity
@Table(schema = "public", name = "USUARIO")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="LOGIN")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="SENHA")
    private String senha;

    @Column(name="NOME")
    private String nome;

    @Column(name="EMAIL")
    private String email;

    @Column(name="URL_FOTO")
    private String urlFoto;
    
    @Version
    @Column(name = "NUM_VERSAO_REGISTRO")
    private Long versao;

    @Transient
    private String token;

    public Usuario() {
        //default
    	super();
    }

    public Usuario(String login, String senha, String nome, String email, String urlFoto) {
    	super();
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.email = email;
        this.urlFoto = urlFoto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
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
