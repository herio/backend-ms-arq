package br.com.herio.arqmsmobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.DispositivoRepository;
import br.com.herio.arqmsmobile.dominio.Notificacao;
import br.com.herio.arqmsmobile.dominio.NotificacaoRepository;
import br.com.herio.arqmsmobile.infra.firebase.FirebaseFachada;

@Service
public class NotificacaoService {

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private NotificacaoRepository notificacaoRepository;

	@Autowired
	private FirebaseFachada firebaseFachada;

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

	public Notificacao atualizaNotificacao(Long idNotificacao, Notificacao notificacao) {
		Notificacao notificacaoBd = notificacaoRepository.findById(idNotificacao).get();
		notificacaoBd.setLida(notificacao.isLida());
		notificacaoBd.setExcluida(notificacao.isExcluida());
		notificacaoRepository.save(notificacaoBd);
		return notificacaoBd;
	}

	public Page<Notificacao> listaNotificacoesEnviadasNaoExcluidas(Long idUsuario, Pageable page) {
		return notificacaoRepository.findAllByEnviadaNaoExcluidaDispositivoUsuarioIdOrderByDataCriacaoDesc(idUsuario, page);
	}
}
