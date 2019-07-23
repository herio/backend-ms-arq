package br.com.herio.arqmsmobile.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "CONFIGURACAO_NOTIFICACAO", schema = "public")
public class ConfiguracaoNotificacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "RECEBER_NOTIFICACAO")
	private boolean receberNotificacao;

	@OneToMany(mappedBy="configuracao", cascade=CascadeType.ALL)
	private List<ConfiguracaoNotificacaoItem> itens;

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
