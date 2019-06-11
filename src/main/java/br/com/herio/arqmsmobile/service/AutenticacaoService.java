package br.com.herio.arqmsmobile.service;


import br.com.herio.arqmsmobile.dto.DtoAutenticacao;
import br.com.herio.arqmsmobile.dto.DtoUsuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class AutenticacaoService {

	@Value("${integracao.urlRSMPush}")
	private String urlRSMPush;
	
    public DtoUsuario autenticarServidor(DtoAutenticacao dtoAutenticacao) {
        return autenticarUsuario(dtoAutenticacao);
    }

	public DtoUsuario autenticarUsuario(DtoAutenticacao dtoAutenticacao) {
        DtoUsuario dtoUsuario = new DtoUsuario();
        recuperarRecebimentoNotificacoes(dtoUsuario, dtoAutenticacao);
		return dtoUsuario;
	}

	private void recuperarRecebimentoNotificacoes(DtoUsuario dtoUsuario, DtoAutenticacao dtoAutenticacao) {
        if(dtoAutenticacao.getSistema() != null) {
            String urlREST = String.format(urlRSMPush + "/usuarios/%s/notificacoes/%s/recebimento", dtoUsuario.getId(),
                dtoAutenticacao.getSistema());
            HttpEntity httpEntity = getHttpEntity(dtoUsuario.getToken(), null);
            boolean receberNotificacao = new RestTemplate().exchange(urlREST, HttpMethod.GET, httpEntity, Boolean.class).getBody();
            dtoUsuario.setReceberNotificacao(receberNotificacao);
        }
	}

    private HttpEntity getHttpEntity(String token, Object body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", token);
        return new HttpEntity(body, httpHeaders);
    }
}
