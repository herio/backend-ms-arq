package br.com.herio.arqmsmobile.service;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dto.DtoAutenticacao;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

	public Usuario autenticarUsuario(DtoAutenticacao dtoAutenticacao) {
        //TODO AUTENTICAR
        return new Usuario();
	}

//	private void recuperarRecebimentoNotificacoes(Usuario usuario, DtoAutenticacao dtoAutenticacao) {
//        if(dtoAutenticacao.getSistema() != null) {
//            String urlREST = String.format(urlRSMPush + "/usuarios/%s/notificacoes/%s/recebimento", usuario.getId(),
//                dtoAutenticacao.getSistema());
//            HttpEntity httpEntity = getHttpEntity(usuario.getToken(), null);
//            boolean receberNotificacao = new RestTemplate().exchange(urlREST, HttpMethod.GET, httpEntity, Boolean.class).getBody();
//            usuario.setReceberNotificacao(receberNotificacao);
//        }
//	}
//
//    private HttpEntity getHttpEntity(String token, Object body) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.set("Authorization", token);
//        return new HttpEntity(body, httpHeaders);
//    }
}
