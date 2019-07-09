package br.com.herio.arqmsmobile.dominio;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class Entidade {

	@CreationTimestamp
	@Column(name = "DATA_CRIACAO")
	private LocalDateTime dataCriacao;

	@UpdateTimestamp
	@Column(name = "DATA_ATUALIZACAO")
	private LocalDateTime dataAtualizacao;

	@Version
	@Column(name = "VERSAO")
	private Long versao;

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

}
