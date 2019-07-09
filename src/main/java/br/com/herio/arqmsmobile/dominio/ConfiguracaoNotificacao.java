package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONFIGURACAO_NOTIFICACAO", schema = "public")
public class ConfiguracaoNotificacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "RECEBER_NOTIFICACAO")
	private Boolean receberNotificacao;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getReceberNotificacao() {
		return receberNotificacao;
	}

	public void setReceberNotificacao(Boolean receberNotificacao) {
		this.receberNotificacao = receberNotificacao;
	}
}
