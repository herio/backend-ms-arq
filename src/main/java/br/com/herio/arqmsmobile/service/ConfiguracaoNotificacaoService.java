package br.com.herio.arqmsmobile.service;

import br.com.herio.arqmsmobile.dominio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ConfiguracaoNotificacaoService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguracaoNotificacaoService.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ConfiguracaoNotificacaoRepository configuracaoNotificacaoRepository;

	@Autowired
	private NotificacaoService notificacaoService;

	public ConfiguracaoNotificacao salvarConfiguracao(Long idUsuario, ConfiguracaoNotificacao configuracaoNotificacao) {
		ConfiguracaoNotificacao configBd;
		try {
			configBd = configuracaoNotificacaoRepository.findByUsuarioId(idUsuario).get();
		} catch (NoSuchElementException e) {
			LOGGER.debug("ConfiguracaoNotificacao salvarConfiguracao", e);
			configBd = new ConfiguracaoNotificacao();
		}
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		configBd.setUsuario(usuario);
		configBd.setReceberNotificacao(configuracaoNotificacao.isReceberNotificacao());
		for (ConfiguracaoNotificacaoItem item : configuracaoNotificacao.getItens()) {
			item.setConfiguracao(configBd);
		}
		configBd.getItens().clear();
		configBd.getItens().addAll(configuracaoNotificacao.getItens());
		return configuracaoNotificacaoRepository.save(configBd);
	}

	public ConfiguracaoNotificacao recuperarConfiguracao(Long idUsuario) {
		try {
			return configuracaoNotificacaoRepository.findByUsuarioId(idUsuario).get();
		} catch (NoSuchElementException e) {
			LOGGER.debug("ConfiguracaoNotificacao recuperarConfiguracao", e);
			return null;
		}
	}
}
