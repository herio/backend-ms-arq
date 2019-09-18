package br.com.herio.arqmsmobile.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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

	public boolean enviaNotificacoes(Map<Long, Collection<Notificacao>> notificacoes, boolean versaoPaga) {
		boolean enviou = false;
		for (Map.Entry<Long, Collection<Notificacao>> entryNotificacoes : notificacoes.entrySet()) {
			boolean notificacaoOrigemEnviada = false;
			Notificacao primeiraNotificacaoEnviada = null;
			for (Notificacao notificacaoBd : entryNotificacoes.getValue()) {
				try {
					enviou = firebaseFachada.enviaNotificacao(notificacaoBd, versaoPaga);
					if (enviou) {
						if (notificacaoBd.getNotificacaoOrigem() == null) {
							notificacaoOrigemEnviada = true;
						} else if (!notificacaoOrigemEnviada) {
							if (primeiraNotificacaoEnviada == null) {
								// notificação origem não foi enviada, primeira após vira origem
								primeiraNotificacaoEnviada = notificacaoBd;
								notificacaoBd.setNotificacaoOrigem(null);
							} else {
								notificacaoBd.setNotificacaoOrigem(primeiraNotificacaoEnviada);
							}
						}
						notificacaoBd.setEnviada(true);
						notificacaoBd.setDataEnvio(LocalDateTime.now(ZoneId.of("UTC-3")));
						notificacaoRepository.save(notificacaoBd);

					}
				} catch (RuntimeException e) {
					LOGGER.error("Erro ao enviar notificação para dispositivo", notificacaoBd.getToken(), e);
					if (e.getMessage().contains("Requested entity was not found") ||
							e.getMessage().contains("The registration token is not a valid FCM registration token")) {
						notificacaoBd.getDispositivo().setDataExclusao(LocalDateTime.now(ZoneId.of("UTC-3")));
						dispositivoRepository.save(notificacaoBd.getDispositivo());
					}
				}
			}
		}
		return enviou;
	}

	public boolean salvarEEnviarNotificacao(Notificacao notificacao, boolean versaoPaga) {
		Dispositivo dispositivoBd = dispositivoRepository.findByNumRegistroAndSo(
				notificacao.getDispositivo().getNumRegistro(), notificacao.getDispositivo().getSo()).get();
		notificacao.setDispositivo(dispositivoBd);
		notificacao.setToken(dispositivoBd.getNumRegistro());
		notificacao = notificacaoRepository.save(notificacao);
		boolean enviou = firebaseFachada.enviaNotificacao(notificacao, versaoPaga);
		if (enviou) {
			notificacao.setEnviada(true);
			notificacao.setDataEnvio(LocalDateTime.now(ZoneId.of("UTC-3")));
			notificacaoRepository.save(notificacao);
		}
		return enviou;
	}

	public Notificacao atualizarNotificacao(Long idNotificacao, Notificacao notificacao) {
		Notificacao notificacaoBd = notificacaoRepository.findById(idNotificacao).get();
		notificacaoBd.setLida(notificacao.isLida());
		notificacaoBd.setExcluida(notificacao.isExcluida());
		if (notificacao.getDataEnvio() != null) {
			notificacaoBd.setDataEnvio(notificacao.getDataEnvio());
		}
		notificacaoRepository.save(notificacaoBd);
		return notificacaoBd;
	}

	public Page<Notificacao> listarNotificacoesEnviadasNaoExcluidas(Long idUsuario, String dadosExtras, Pageable page) {
		if(dadosExtras == null) {
			return notificacaoRepository.findAllByEnviadaAndExcluidaAndNotificacaoOrigemIsNullAndDispositivoUsuarioIdOrderByDataCriacaoDesc(
					true, false, idUsuario, page);
		} else {
			return notificacaoRepository
					.findAllByEnviadaAndExcluidaAndNotificacaoOrigemIsNullAndDispositivoUsuarioIdAndDadosExtrasIgnoreCaseContainingOrderByDataCriacaoDesc(
							true, false, idUsuario, dadosExtras, page);
		}
	}

	public Page<Notificacao> listarNotificacoes(Long idUsuario, Pageable page) {
		return notificacaoRepository.findAllByNotificacaoOrigemIsNullAndDispositivoUsuarioIdOrderByDataCriacaoDesc(idUsuario, page);
	}

	public boolean criarEEnviarNotificacoes(String titulo, String conteudo, String dadosExtras, Long idUsuarioDestino) {
		Map<Long, Collection<Notificacao>> mapNotificacoesASeremEnviadas = new HashMap<>();
		Collection<Dispositivo> dispositivosAtivos = dispositivoRepository.findAllByUsuarioIdAndDataExclusaoIsNull(idUsuarioDestino);
		LOGGER.info(String.format("MensagemBatePapoService dispositivosAtivos.size[%s]", dispositivosAtivos.size()));
		if (!dispositivosAtivos.isEmpty()) {
			mapNotificacoesASeremEnviadas = criarNotificacoesASeremEnviadas(titulo, conteudo, dadosExtras, dispositivosAtivos);
		}
		LOGGER.info(String.format("MensagemBatePapoService mapNotificacoesASeremEnviadas.size[%s]", mapNotificacoesASeremEnviadas.size()));

		return enviaNotificacoes(mapNotificacoesASeremEnviadas, false);
	}

	private Map<Long, Collection<Notificacao>> criarNotificacoesASeremEnviadas(String titulo, String conteudo, String dadosExtras,
																			   Collection<Dispositivo> dispositivos) {
		Map<Long, Collection<Notificacao>> mapNotificacoes = new HashMap<>();
		Collection<Notificacao> notificacoes = new ArrayList<>();

		Notificacao notificacaoOrigem = null;
		for (Dispositivo dispositivo : dispositivos) {
			Notificacao notificacao = new Notificacao();
			notificacao.setTitulo(titulo);
			notificacao.setConteudo(conteudo);
			notificacao.setToken(dispositivo.getNumRegistro());
			notificacao.setDispositivo(dispositivo);
			notificacao.setDadosExtras(dadosExtras);
			notificacao.setDataEnvio(LocalDateTime.now(ZoneId.of("UTC-3")));
			if (notificacaoOrigem != null) {
				notificacao.setNotificacaoOrigem(notificacaoOrigem);
			}
			notificacao = notificacaoRepository.save(notificacao);
			if (notificacoes.isEmpty()) {
				notificacaoOrigem = notificacao;
			}
			notificacoes.add(notificacao);
		}
		if (notificacaoOrigem != null) {
			mapNotificacoes.put(notificacaoOrigem.getId(), notificacoes);
		}
		return mapNotificacoes;
	}

}
