package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;

@Entity
@Table(schema = "public", name = "USUARIO")
public class Usuario extends Entidade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "LOGIN")
	private String login;

	@Column(name = "EMAIL")
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "SENHA")
	private String senha;

	@Column(name = "URL_FOTO")
	private String urlFoto;

	@Column(name = "ATIVADO")
	private boolean ativado;

	@Transient
	private String token;

	public Usuario() {
		// default
		super();
	}

	public Usuario(String login, String senha, String nome, String email, String urlFoto, boolean ativado) {
		super();
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.email = email;
		this.urlFoto = urlFoto;
		this.ativado = ativado;
	}

	public boolean isAtivado() {
		return ativado;
	}

	public void setAtivado(boolean ativado) {
		this.ativado = ativado;
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
		if (StringUtils.isEmpty(this.login)) {
			msg.append("Login inválido, ");
		}
		if (StringUtils.isEmpty(this.senha)) {
			msg.append("Senha inválida, ");
		}
		if (StringUtils.isEmpty(this.nome)) {
			msg.append("Nome inválido");
		}
		if (msg.length() > 0) {
			throw new ExcecaoNegocio(msg.toString());
		}
	}
}
