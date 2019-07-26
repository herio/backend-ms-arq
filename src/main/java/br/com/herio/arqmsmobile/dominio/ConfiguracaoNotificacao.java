package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CONFIGURACAO_NOTIFICACAO")
public class ConfiguracaoNotificacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "RECEBER_NOTIFICACAO")
	private boolean receberNotificacao;

	@OneToMany(mappedBy = "configuracao", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ConfiguracaoNotificacaoItem> itens = new ArrayList<>();

	public List<ConfiguracaoNotificacaoItem> getItens() {
		return itens;
	}

	public void setItens(List<ConfiguracaoNotificacaoItem> itens) {
		this.itens = itens;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isReceberNotificacao() {
		return receberNotificacao;
	}

	public void setReceberNotificacao(boolean receberNotificacao) {
		this.receberNotificacao = receberNotificacao;
	}
}
