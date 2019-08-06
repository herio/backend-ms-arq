package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "LOG_NOTIFICACAO")
public class LogNotificacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "LOG")
	private String log;

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@JsonProperty
	public String getDataFormatada() {
		return getDataCriacao() == null ? "" : getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}

}
