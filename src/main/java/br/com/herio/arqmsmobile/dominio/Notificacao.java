package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.Entidade;

@Entity
@Table(name = "NOTIFICACAO", schema = "public")
public class Notificacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "TITULO")
	private String titulo;

	@Column(name = "CONTEUDO")
	private String conteudo;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "ENVIADA")
	private boolean enviada;

	@Column(name = "LIDA")
	private boolean lida;

	@Column(name = "EXCLUIDA")
	private boolean excluida;

	@ManyToOne
	@JoinColumn(name = "ID_NOTIFICACAO_ORIGEM")
	private Notificacao notificacaoOrigem;

	@ManyToOne
	@JoinColumn(name = "ID_DISPOSITIVO")
	private Dispositivo dispositivo;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isEnviada() {
		return enviada;
	}

	public void setEnviada(boolean enviada) {
		this.enviada = enviada;
	}

	public boolean isLida() {
		return lida;
	}

	public void setLida(boolean lida) {
		this.lida = lida;
	}

	public boolean isExcluida() {
		return excluida;
	}

	public void setExcluida(boolean excluida) {
		this.excluida = excluida;
	}

	public Notificacao getNotificacaoOrigem() {
		return notificacaoOrigem;
	}

	public void setNotificacaoOrigem(Notificacao notificacaoOrigem) {
		this.notificacaoOrigem = notificacaoOrigem;
	}

	public Dispositivo getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

}
