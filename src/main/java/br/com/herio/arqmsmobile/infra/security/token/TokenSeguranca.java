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
    private Date dataCriacaoToken;
    private Long idUsuario;
    private String nomeUsuario;
    private String loginUsuario;
    private Set<String> roles = new HashSet<>();
    private String emissorDoToken;

    public TokenSeguranca(Long idUsuario, String nomeUsuario, String loginUsuario) {
        this(null, null, idUsuario, nomeUsuario, loginUsuario, null, null );
    }

    public TokenSeguranca(Date expiracaoToken, Date dataCriacaoToken, Long idUsuario,
                          String nomeUsuario, String loginUsuario, Set<String> roles, String emissorToken) {
        super();
        this.dataCriacaoToken = dataCriacaoToken == null? new Date(): dataCriacaoToken;
        this.expiracaoToken = expiracaoToken == null ? defineExpiracaoToken() : expiracaoToken;
        this.idUsuario = idUsuario;
        this.nomeUsuario = nomeUsuario;
        this.loginUsuario = loginUsuario;
        this.roles = roles == null? new HashSet<>(): roles;
        this.emissorDoToken = emissorToken == null? TOKEN_ISSUER: emissorToken;
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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getEmissorToken() {
        return emissorDoToken;
    }

}
