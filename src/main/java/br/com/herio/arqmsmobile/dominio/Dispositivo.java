package br.com.herio.arqmsmobile.dominio;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.dto.EnumTipoSO;
import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "DISPOSITIVO", schema = "public")
public class Dispositivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario usuario;

    @Column(name = "NUM_REGISTRO")
    private String numRegistro;

    @Column(name = "IND_OS")
    @Enumerated(EnumType.STRING)
    private EnumTipoSO os;

    @Column(name = "IND_SISTEMA")
    @Enumerated(EnumType.STRING)
    private EnumSistema sistema;

    @Column(name = "DTHORA_CADASTRO")
    private Date dataCadastro;

    @Column(name = "DTHORA_EXCLUSAO")
    private Date dataExclusao;

    public Dispositivo() {
        // default
    }

    public Dispositivo(Usuario usuario, String numRegistro, EnumTipoSO os, EnumSistema sistema) {
        super();
        this.usuario = usuario;
        this.numRegistro = numRegistro;
        this.os = os;
        this.sistema = sistema;
    }

    public EnumSistema getSistema() {
        return sistema;
    }

    public void setSistema(EnumSistema sistema) {
        this.sistema = sistema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
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
        return String.format("%nDispositivo [id=%s, idUsuario=%s, numRegistro=%s, os=%s, sistema=%s, dataCadastro=%s]",
                getId(), usuario.getId(), numRegistro, os, sistema, dataCadastro);
    }

    public void valida() {
        if(StringUtils.isEmpty(this.numRegistro) || this.os == null
                || this.sistema == null || this.usuario == null || this.dataCadastro == null) {
            throw new ExcecaoNegocio("Dispositivo inválido, verifique campos obrigatórios");
        }
    }
}
