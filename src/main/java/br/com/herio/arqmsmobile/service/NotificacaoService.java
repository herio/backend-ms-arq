package br.com.herio.arqmsmobile.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacao;
import br.com.herio.arqmsmobile.dominio.Dispositivo;
import br.com.herio.arqmsmobile.dominio.DispositivoRepository;
import br.com.herio.arqmsmobile.dominio.LogNotificacao;
import br.com.herio.arqmsmobile.dominio.LogNotificacaoRepository;
import br.com.herio.arqmsmobile.dominio.Notificacao;
import br.com.herio.arqmsmobile.dominio.NotificacaoRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.infra.firebase.FirebaseFachada;

@Service
public class NotificacaoService {

	@Autowired
	protected LogNotificacaoRepository logNotificacaoRepository;

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private NotificacaoRepository notificacaoRepository;

	@Autowired
	private EnviadorEmailService enviadorEmailService;

	@Autowired
	private ConfiguracaoNotificacaoService configuracaoNotificacaoService;

	@Autowired
	private FirebaseFachada firebaseFachada;

	private StringBuilder log = new StringBuilder("");

	public boolean salvarEEnviarNotificacaoParaDispositivo(Notificacao notificacao, boolean versaoPaga) {
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

	public boolean salvarEEnviarNotificacoes(String titulo, String conteudo, String dadosExtras, Long idUsuarioDestino, boolean versaoPaga,
			boolean enviarEmail) {
		boolean enviou = false;
		this.log = new StringBuilder("");
		this.log.append(String.format(
				">>> NotificacaoService salvarEEnviarNotificacoes titulo[%s] conteudo[%s] dadosExtras[%s] idUsuarioDestino[%s] versaoPaga[%s]%n",
				titulo, conteudo, dadosExtras, idUsuarioDestino, versaoPaga));

		ConfiguracaoNotificacao configuracaoNotificacao = configuracaoNotificacaoService.recuperarConfiguracao(idUsuarioDestino);
		if (configuracaoNotificacao != null && configuracaoNotificacao.isReceberNotificacao()) {
			Map<Long, Collection<Notificacao>> mapNotificacoesASeremEnviadas = new HashMap<>();
			Collection<Dispositivo> dispositivosAtivos = dispositivoRepository.findAllByUsuarioIdAndDataExclusaoIsNull(idUsuarioDestino);
			this.log.append(String.format(">>> NotificacaoService dispositivosAtivos.size[%s]%n", dispositivosAtivos.size()));
			if (!dispositivosAtivos.isEmpty()) {
				mapNotificacoesASeremEnviadas = criarNotificacoesASeremEnviadas(titulo, conteudo, dadosExtras, dispositivosAtivos);
			}
			this.log.append(String.format(">>> NotificacaoService mapNotificacoesASeremEnviadas.size[%s]%n", mapNotificacoesASeremEnviadas.size()));
			enviou = enviarNotificacoes(mapNotificacoesASeremEnviadas, versaoPaga, this.log);
		} else {
			this.log.append(String.format(">>> NotificacaoService configuracaoNotificacao.isReceberNotificacao[%s]%n",
					configuracaoNotificacao == null ? "null" : configuracaoNotificacao.isReceberNotificacao()));
		}

		// salva log em banco
		LogNotificacao logNotificacao = new LogNotificacao();
		logNotificacao.setLog(this.log.toString());
		logNotificacaoRepository.save(logNotificacao);

		if (enviarEmail) {
			Usuario usuario = usuarioRepository.findById(idUsuarioDestino).get();
			enviadorEmailService.enviaEmailParaUsuario(titulo, conteudo, usuario);
		}

		return enviou;
	}

	public boolean enviarNotificacoes(Map<Long, Collection<Notificacao>> notificacoes, boolean versaoPaga, StringBuilder log) {
		boolean enviou = false;
		for (Map.Entry<Long, Collection<Notificacao>> entryNotificacoes : notificacoes.entrySet()) {
			boolean notificacaoOrigemEnviada = false;
			Notificacao primeiraNotificacaoEnviada = null;
			for (Notificacao notificacaoBd : entryNotificacoes.getValue()) {
				try {
					enviou = firebaseFachada.enviaNotificacao(notificacaoBd, versaoPaga);
					log.append(String.format(">>> NotificacaoService enviarNotificacoes enviou[%s] notificacao[%s]%n", enviou, notificacaoBd));

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
					log.append(String.format(">>> NotificacaoService enviarNotificacoes ERRO e.getMessage[%s] notificacao[%s]%n", e.getMessage(),
							notificacaoBd));
					if (e.getMessage().contains("Requested entity was not found") ||
							e.getMessage().contains("The registration token is not a valid FCM registration token")) {
						log.append(String.format(">>> NotificacaoService enviarNotificacoes ERRO excluindo dispositivo[%s]%n",
								notificacaoBd.getDispositivo()));
						notificacaoBd.getDispositivo().setDataExclusao(LocalDateTime.now(ZoneId.of("UTC-3")));
						dispositivoRepository.save(notificacaoBd.getDispositivo());
					}
				}
			}
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
		if (dadosExtras == null) {
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
