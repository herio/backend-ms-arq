package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "APP", schema = "public")
public class App extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "TITULO")
	private String titulo;

	@Column(name = "DESCRICAO")
	private String descricao;

	@Column(name = "URL_ICONE")
	private String urlIcone;

	@Column(name = "URL_ANDROID")
	private String urlAndroid;

	@Column(name = "URL_IOS")
	private String urlIos;

	@Column(name = "ATIVO")
	private boolean ativo;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUrlIcone() {
		return urlIcone;
	}

	public void setUrlIcone(String urlIcone) {
		this.urlIcone = urlIcone;
	}

	public String getUrlAndroid() {
		return urlAndroid;
	}

	public void setUrlAndroid(String urlAndroid) {
		this.urlAndroid = urlAndroid;
	}

	public String getUrlIos() {
		return urlIos;
	}

	public void setUrlIos(String urlIos) {
		this.urlIos = urlIos;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

}
