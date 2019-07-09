package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ENVIO_NOTIFICACAO", schema = "public")
public class EnvioNotificacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_DISPOSITIVO")
	private Dispositivo dispositivo;

	@Column(name = "TITULO")
	private String titulo;

	@Column(name = "CONTEUDO")
	private String conteudo;

	@Column(name = "EXTRAS")
	private String extras;

	@Column(name = "ENVIADO")
	private boolean enviado;

	@Column(name = "ID_NOTIFICACAO_ORIGEM")
	private Long idNotificacaoOrigem;

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

	public Dispositivo getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

	public String getExtras() {
		return extras;
	}

	public void setExtras(String extras) {
		this.extras = extras;
	}

	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	public Long getIdNotificacaoOrigem() {
		return idNotificacaoOrigem;
	}

	public void setIdNotificacaoOrigem(Long idNotificacaoOrigem) {
		this.idNotificacaoOrigem = idNotificacaoOrigem;
	}

}
