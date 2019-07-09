package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ATIVACAO_USUARIO", schema = "public")
public class AtivacaoUsuario extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "CHAVE_ATIVACAO")
	private String chaveAtivacao;

	@Column(name = "DATA_ATIVACAO")
	private Date dataAtivacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getChaveAtivacao() {
		return chaveAtivacao;
	}

	public void setChaveAtivacao(String chaveAtivacao) {
		this.chaveAtivacao = chaveAtivacao;
	}

	public Date getDataAtivacao() {
		return dataAtivacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	public void geraChaveAtivacao() {
		String strChave = String.valueOf(System.currentTimeMillis());
		String chave = new String(Base64.getEncoder().encode(strChave.getBytes()));
		setChaveAtivacao(chave);
	}
}
