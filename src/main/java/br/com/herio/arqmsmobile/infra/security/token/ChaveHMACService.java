package br.com.herio.arqmsmobile.infra.security.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

@Service
public class ChaveHMACService {

    @Value("${seguranca.chave.assinatura.token.jwt}")
    private String jjwtSignKey;

    private Key chaveParaAssinauraTokenJwt;

    public Key getSecretKey() {
        return chaveParaAssinauraTokenJwt;
    }

    @PostConstruct
    public void criaChaveAssinaturaTokenJwt() {
        final byte[] decodedKey = DatatypeConverter.parseBase64Binary(this.jjwtSignKey);
        this.chaveParaAssinauraTokenJwt = new SecretKeySpec(decodedKey, 0, decodedKey.length, "SHA-512");
    }

}
