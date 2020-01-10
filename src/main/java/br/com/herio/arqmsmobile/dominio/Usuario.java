package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "USUARIO")
public class Usuario extends Entidade implements Serializable {
	private static final long serialVersionUID = 1L;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private VigenciaUsuario vigenciaUsuario;

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

	@Column(name = "URL_FOTO_THUMB")
	private String urlFotoThumb;

	@Column(name = "ID_DRIVE_FOTO")
	private String idDriveFoto;

	@Column(name = "ID_DRIVE_FOTO_THUMB")
	private String idDriveFotoThumb;

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

	@Column(name = "PERFIL")
	private String perfil;

	@Column(name = "ATIVADO")
	private boolean ativado;

	@Column(name = "ADMIN")
	private boolean admin;

	@Column(name = "DATA_EXCLUSAO")
	private LocalDateTime dataExclusao;

	@Transient
	private String token;

    public VigenciaUsuario getVigenciaUsuario() {
        return vigenciaUsuario;
    }

    public void setVigenciaUsuario(VigenciaUsuario vigenciaUsuario) {
        this.vigenciaUsuario = vigenciaUsuario;
    }

    public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getIdDriveFoto() {
		return idDriveFoto;
	}

	public void setIdDriveFoto(String idDriveFoto) {
		this.idDriveFoto = idDriveFoto;
	}

	public String getIdDriveFotoThumb() {
		return idDriveFotoThumb;
	}

	public void setIdDriveFotoThumb(String idDriveFotoThumb) {
		this.idDriveFotoThumb = idDriveFotoThumb;
	}

	public String getUrlFotoThumb() {
		return urlFotoThumb;
	}

	public void setUrlFotoThumb(String urlFotoThumb) {
		this.urlFotoThumb = urlFotoThumb;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

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
