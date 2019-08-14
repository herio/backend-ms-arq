package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "USUARIO")
public class Usuario extends Entidade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "SISTEMA")
	private String sistema;

	@Column(name = "LOGIN")
	private String login;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "NOME")
	private String nome;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "SENHA")
	private String senha;

	@Column(name = "URL_FOTO")
	private String urlFoto;

	@Column(name = "TELEFONE")
	private String telefone;

	@Column(name = "CELULAR")
	private String celular;

	@Column(name = "INSTAGRAM")
	private String instagram;

	@Column(name = "FACEBOOK")
	private String facebook;

	@Column(name = "CPF")
	private String cpf;

	@Column(name = "ENDERECO")
	private String endereco;

	@Column(name = "CEP")
	private String cep;

	@Column(name = "CIDADE")
	private String cidade;

	@Column(name = "ESTADO")
	private String estado;

	@Column(name = "ATIVADO")
	private boolean ativado;

	@Column(name = "DATA_EXCLUSAO")
	private LocalDateTime dataExclusao;

	@Transient
	private String token;

	public LocalDateTime getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(LocalDateTime dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
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

}
