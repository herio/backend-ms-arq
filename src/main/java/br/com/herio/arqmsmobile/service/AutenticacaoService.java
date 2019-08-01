package br.com.herio.arqmsmobile.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.dto.DtoAutenticacao;
import br.com.herio.arqmsmobile.infra.security.token.TokenJwtService;
import br.com.herio.arqmsmobile.infra.security.token.TokenSeguranca;

@Service
public class AutenticacaoService {

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected TokenJwtService tokenJwtService;

	public Usuario autenticarUsuario(DtoAutenticacao dtoAutenticacao) {
		Usuario usuario = usuarioRepository.findByLoginAndSenhaAndAtivado(dtoAutenticacao.getLogin(),
				Base64.getEncoder().encodeToString(dtoAutenticacao.getSenha().getBytes()), true).get();
		usuario.setToken(criaTokenJwt(usuario));
		return usuario;
	}

	public String criaTokenJwt(Usuario usuario) {
		TokenSeguranca tokenSeguranca = new TokenSeguranca(usuario.getId(), usuario.getNome(), usuario.getLogin());
		return "Bearer " + tokenJwtService.tokenSegurancaToTokenJwt(tokenSeguranca);
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
