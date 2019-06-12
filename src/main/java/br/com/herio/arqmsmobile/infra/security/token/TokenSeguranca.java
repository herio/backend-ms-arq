package br.com.herio.arqmsmobile.infra.security.token;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TokenSeguranca {

    protected final String TOKEN_ISSUER = "com.herio";

    // 7 dias em milisegundos - 1000 * 60 * 60 * 24 * 7
    private final int tempoExpiracaoClienteMobile = 604800000;

    private Date expiracaoToken;
    private long criacaoToken;
    private Date dataCriacaoToken;
    private int idUsuario;
    private String nomeUsuarioLogado;
    private String usuarioLogado;
    private Set<String> roles = new HashSet<>();
    private String emissorDoToken;

    public TokenSeguranca(Date expiracaoToken, long criacaoToken, Date dataCriacaoToken, int idUsuario,
                          String nomeUsuarioLogado, String usuarioLogado, Set<String> roles, String emissorToken) {
        super();
        this.expiracaoToken = expiracaoToken;
        this.criacaoToken = criacaoToken;
        this.dataCriacaoToken = dataCriacaoToken;
        this.idUsuario = idUsuario;
        this.nomeUsuarioLogado = nomeUsuarioLogado;
        this.usuarioLogado = usuarioLogado;
        this.roles = roles;
        this.emissorDoToken = emissorToken;
        this.validaToken();
    }

    protected Date defineExpiracaoToken() {
        int tempoExpiracao =tempoExpiracaoClienteMobile;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.getDataCriacaoToken().getTime() + tempoExpiracao);
        return cal.getTime();
    }

    protected void validaToken() {
        final long expiracaoEmMills = this.getExpiracaoToken().getTime();
        final long dataHoraCorrenteEmMills = new Date().getTime();
        if (expiracaoEmMills < dataHoraCorrenteEmMills) {
            throw new IllegalStateException("Validade do token JWT expirada");
        }
    }

    public Date getExpiracaoToken() {
        return expiracaoToken;
    }

    public Date getDataCriacaoToken() { return dataCriacaoToken; }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNomeUsuarioLogado() {
        return nomeUsuarioLogado;
    }

    public String getUsuarioLogado() {
        return usuarioLogado;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getEmissorToken() {
        return emissorDoToken;
    }

    public long getCriacaoToken() {
        return criacaoToken;
    }
}
