package br.com.herio.arqmsmobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.DispositivoRepository;
import br.com.herio.arqmsmobile.dominio.Notificacao;
import br.com.herio.arqmsmobile.dominio.NotificacaoRepository;
import br.com.herio.arqmsmobile.infra.firebase.FirebaseFachada;
import io.swagger.annotations.ApiOperation;

@Service
public class NotificacaoService {

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private NotificacaoRepository notificacaoRepository;

	@Autowired
	private FirebaseFachada firebaseFachada;

	@ApiOperation("enviaNotificacao")
	@PostMapping("/envia")
	public boolean enviaNotificacao(Notificacao notificacao) {
		Dispositivo dispositivoBd = dispositivoRepository.findByNumRegistroAndSo(
				notificacao.getDispositivo().getNumRegistro(), notificacao.getDispositivo().getSo()).get();
		notificacao.setDispositivo(dispositivoBd);
		notificacao.setToken(dispositivoBd.getNumRegistro());
		notificacao = notificacaoRepository.save(notificacao);
		boolean enviou = firebaseFachada.enviaNotificacao(notificacao);
		if (enviou) {
			notificacao.setEnviada(true);
			notificacaoRepository.save(notificacao);
		}
		return enviou;
	}
}
