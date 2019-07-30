package br.com.herio.arqmsmobile.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacaoService.class);

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private NotificacaoRepository notificacaoRepository;

	@Autowired
	private FirebaseFachada firebaseFachada;

	public boolean enviaNotificacaoParaTodosDispositivosUsuario(Long idUsuario, Notificacao notificacao) {
		List<Dispositivo> dispositivos = dispositivoRepository.findNaoExcluidosByIdUsuario(idUsuario);
		boolean enviou = false;
		if (dispositivos != null && !dispositivos.isEmpty()) {
			List<Notificacao> notificacoesASeremEnviadas = new ArrayList<>();
			Notificacao notificacaoOrigem = null;
			for (Dispositivo dispositivo : dispositivos) {
				Notificacao notificacaoAtual = new Notificacao();
				notificacaoAtual.setTitulo(notificacao.getTitulo());
				notificacaoAtual.setConteudo(notificacao.getConteudo());
				notificacaoAtual.setDadosExtras(notificacao.getDadosExtras());
				notificacaoAtual.setDispositivo(dispositivo);
				notificacaoAtual.setToken(dispositivo.getNumRegistro());
				if (notificacaoOrigem != null) {
					notificacaoAtual.setNotificacaoOrigem(notificacaoOrigem);
				}
				notificacaoAtual = notificacaoRepository.save(notificacaoAtual);
				if (notificacoesASeremEnviadas.isEmpty()) {
					notificacaoOrigem = notificacaoAtual;
				}
				notificacoesASeremEnviadas.add(notificacaoAtual);
			}
			for (Notificacao notificacaoBd : notificacoesASeremEnviadas) {
				try {
					enviou = firebaseFachada.enviaNotificacao(notificacaoBd);
					if (enviou) {
						notificacaoBd.setEnviada(true);
						notificacaoRepository.save(notificacaoBd);
					}
				} catch (RuntimeException e) {
					LOGGER.error("Erro ao enviar notificação para dispositivo", notificacaoBd.getToken(), e);
					if (e.getMessage().contains("Requested entity was not found")) {
						notificacaoBd.getDispositivo().setDataExclusao(new Date());
						dispositivoRepository.save(notificacaoBd.getDispositivo());
					}
				}
			}
		}
		return enviou;
	}

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
