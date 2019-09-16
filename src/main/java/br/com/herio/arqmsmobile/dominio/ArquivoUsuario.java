package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ARQUIVO_USUARIO")
public class ArquivoUsuario extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "LINK")
	private String link;

	@Column(name = "LINK_THUMB")
	private String linkThumb;

	@Column(name = "ID_DRIVE")
	private String idDrive;

	@Column(name = "ID_DRIVE_THUMB")
	private String idDriveThumb;

	@Column(name = "ATRIBUTOS")
	private String atributos;

	@Column(name = "DATA_EXCLUSAO")
	private LocalDateTime dataExclusao;

	public String getLinkThumb() {
		return linkThumb;
	}

	public void setLinkThumb(String linkThumb) {
		this.linkThumb = linkThumb;
	}

	public String getIdDriveThumb() {
		return idDriveThumb;
	}

	public void setIdDriveThumb(String idDriveThumb) {
		this.idDriveThumb = idDriveThumb;
	}

	public String getAtributos() {
		return atributos;
	}

	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}

	public LocalDateTime getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(LocalDateTime dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public String getIdDrive() {
		return idDrive;
	}

	public void setIdDrive(String idDrive) {
		this.idDrive = idDrive;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
