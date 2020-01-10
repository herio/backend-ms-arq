package br.com.herio.arqmsmobile.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "VIGENCIA_USUARIO")
public class VigenciaUsuario extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "DATA_TERMINO_VIGENCIA")
	private LocalDateTime dataTerminoVigencia;

	@Column(name = "DATA_EXCLUSAO")
	private LocalDateTime dataExclusao;

	public LocalDateTime getDataTerminoVigencia() {
		return dataTerminoVigencia;
	}

	public void setDataTerminoVigencia(LocalDateTime dataTerminoVigencia) {
		this.dataTerminoVigencia = dataTerminoVigencia;
	}

	public LocalDateTime getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(LocalDateTime dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

    @JsonProperty
    public String getDataTerminoVigenciaFormatada() {
        return this.dataTerminoVigencia == null ? "" : this.dataTerminoVigencia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
