package br.com.herio.arqmsmobile.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CONFIGURACAO_NOTIFICACAO_ITEM")
public class ConfiguracaoNotificacaoItem extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ID_CONFIGURACAO")
	private ConfiguracaoNotificacao configuracao;

	@Column(name = "TIPO_ITEM")
	private String tipoItem;

	@Column(name = "TIPO_PERIODICIDADE")
	private String tipoPeriodicidade;

	@Column(name = "VALOR_ITEM")
	private String valorItem;

	public String getTipoPeriodicidade() {
		return tipoPeriodicidade;
	}

	public void setTipoPeriodicidade(String tipoPeriodicidade) {
		this.tipoPeriodicidade = tipoPeriodicidade;
	}

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

	@Override
	public String toString() {
		return String.format("{tipoItem:'%s', tipoPeriodicidade: '%s', valorItem: '%s'}", tipoItem, tipoPeriodicidade, valorItem);
	}
}
