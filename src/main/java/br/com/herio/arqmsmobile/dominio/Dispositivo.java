package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import br.com.herio.arqmsmobile.dto.EnumTipoSO;
import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;

@Entity
@Table(name = "DISPOSITIVO", schema = "public")
public class Dispositivo extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "NUM_REGISTRO")
	private String numRegistro;

	@Column(name = "IND_OS")
	@Enumerated(EnumType.STRING)
	private EnumTipoSO os;

	@Column(name = "DATA_EXCLUSAO")
	private Date dataExclusao;

	public Dispositivo() {
		super();
		// default
	}

	public Dispositivo(Usuario usuario, String numRegistro, EnumTipoSO os) {
		super();
		this.usuario = usuario;
		this.numRegistro = numRegistro;
		this.os = os;
	}

	public String getNumRegistro() {
		return numRegistro;
	}

	public void setNumRegistro(String numRegistro) {
		this.numRegistro = numRegistro;
	}

	public EnumTipoSO getOs() {
		return os;
	}

	public void setOs(EnumTipoSO so) {
		this.os = so;
	}

	public Date getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(Date dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return String.format("%nDispositivo [id=%s, idUsuario=%s, numRegistro=%s, os=%s, sistema=%s]", getId(),
				usuario.getId(), numRegistro, os);
	}

	public void valida() {
		if (StringUtils.isEmpty(this.numRegistro) || this.os == null || this.usuario == null) {
			throw new ExcecaoNegocio("Dispositivo inválido, verifique campos obrigatórios");
		}
	}
}
