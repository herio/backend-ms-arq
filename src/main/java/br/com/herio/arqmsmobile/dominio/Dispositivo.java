package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.herio.arqmsmobile.dto.EnumTipoSO;
import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;

@Entity
@Table(name = "DISPOSITIVO")
public class Dispositivo extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "NUM_REGISTRO")
	private String numRegistro;

	@Column(name = "IND_SO")
	@Enumerated(EnumType.STRING)
	private EnumTipoSO so;

	@Column(name = "DATA_EXCLUSAO")
	private LocalDateTime dataExclusao;

	@Transient
	private boolean validacaoApple;

	public Dispositivo() {
		super();
		// default
	}

	public Dispositivo(Usuario usuario, String numRegistro, EnumTipoSO so) {
		super();
		this.usuario = usuario;
		this.numRegistro = numRegistro;
		this.so = so;
	}

	public boolean isValidacaoApple() {
		return validacaoApple;
	}

	public void setValidacaoApple(boolean validacaoApple) {
		this.validacaoApple = validacaoApple;
	}

	public String getNumRegistro() {
		return numRegistro;
	}

	public void setNumRegistro(String numRegistro) {
		this.numRegistro = numRegistro;
	}

	public EnumTipoSO getSo() {
		return so;
	}

	public void setSo(EnumTipoSO so) {
		this.so = so;
	}

	public LocalDateTime getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(LocalDateTime dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	@JsonProperty
	public String getDataExclusaoFormatada() {
		return this.dataExclusao == null ? "" : this.dataExclusao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return String.format("%n Dispositivo [id=%s, idUsuario=%s, numRegistro=%s, so=%s]", getId(),
				usuario.getId(), numRegistro, so);
	}

	public void valida() {
		if (StringUtils.isEmpty(this.numRegistro) || this.so == null || this.usuario == null) {
			throw new ExcecaoNegocio("Dispositivo inválido, verifique campos obrigatórios");
		}
	}
}
