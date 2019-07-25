package br.com.herio.arqmsmobile.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CONFIGURACAO_NOTIFICACAO_ITEM", schema = "public")
public class ConfiguracaoNotificacaoItem extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ID_CONFIGURACAO")
	private ConfiguracaoNotificacao configuracao;

	@Column(name = "TIPO_ITEM")
	private String tipoItem;

	@Column(name = "VALOR_ITEM")
	private String valorItem;

	public ConfiguracaoNotificacao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(ConfiguracaoNotificacao configuracao) {
		this.configuracao = configuracao;
	}

	public String getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}

	public String getValorItem() {
		return valorItem;
	}

	public void setValorItem(String valorItem) {
		this.valorItem = valorItem;
	}
}