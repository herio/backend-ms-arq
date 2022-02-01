package br.com.herio.arqmsmobile.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.LogNotificacaoRepository;
import br.com.herio.arqmsmobile.dominio.NotificacaoRepository;

@Service
public class CleanService {

	@Autowired
	protected LogNotificacaoRepository logNotificacaoRepository;

	@Autowired
	protected NotificacaoRepository notificacaoRepository;

	public String clean() {
		LocalDateTime dataHoraLimite = LocalDateTime.now(ZoneId.of("UTC-3")).withMinute(0).withSecond(0).minusSeconds(1);

		Long deleteLogsNotificacoes = logNotificacaoRepository.deleteAllByDataCriacaoBefore(dataHoraLimite);
		Long deleteNotificacoes = notificacaoRepository.deleteAllByDataCriacaoBefore(dataHoraLimite);

		return String.format("Clean concluído deleteLogsNotificacoes[%s] deleteNotificacoes[%s]", deleteLogsNotificacoes, deleteNotificacoes);
	}
}
