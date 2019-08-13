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
@Table(name = "USUARIO")
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

	@Column(name = "CELULAR")
	private String celular;

	@Column(name = "ENDERECO")
	private String endereco;

	@Column(name = "CEP")
	private String cep;

	@Column(name = "CIDADE")
	private String cidade;

	@Column(name = "ESTADO")
	private String estado;

	@Column(name = "SISTEMA")
	private String sistema;

	@Transient
	private String token;

	public Usuario() {
		// default
		super();
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
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
			msg.append("Login inv�lido, ");
		}
		if (StringUtils.isEmpty(this.senha)) {
			msg.append("Senha inv�lida, ");
		}
		if (StringUtils.isEmpty(this.nome)) {
			msg.append("Nome inv�lido");
		}
		if (msg.length() > 0) {
			throw new ExcecaoNegocio(msg.toString());
		}
	}
}
