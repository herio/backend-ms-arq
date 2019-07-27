package br.com.herio.arqmsmobile.dominio;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "NOTIFICACAO")
public class Notificacao extends Entidade implements Serializable {
	private static final Logger LOGGER = LoggerFactory.getLogger(Notificacao.class);

	private static final long serialVersionUID = 1L;

	@Column(name = "TITULO")
	private String titulo;

	@Column(name = "CONTEUDO")
	private String conteudo;

	@Column(name = "DADOS_EXTRAS")
	private String dadosExtras;

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

	public String getDadosExtras() {
		return dadosExtras;
	}

	public void setDadosExtras(String dadosExtras) {
		this.dadosExtras = dadosExtras;
	}

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

	public Map<String, String> getMapDadosExtras() {
		// dadosExtras: { "field1" : "value1", "field2" : "value2" }
		Map<String, String> map = new HashMap<>();
		if (this.dadosExtras != null) {
			try {
				map = new ObjectMapper().readValue(this.dadosExtras, HashMap.class);
			} catch (IOException e) {
				LOGGER.error("Erro em getMapDadosExtras", e);
			}
		}
		map.put("id", String.valueOf(this.getId()));
		map.put("titulo", this.titulo);
		map.put("conteudo", this.conteudo);
		return map;
	}

}
