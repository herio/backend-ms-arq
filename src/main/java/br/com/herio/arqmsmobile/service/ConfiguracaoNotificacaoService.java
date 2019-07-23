package br.com.herio.arqmsmobile.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacao;
import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacaoItem;
import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacaoRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;

@Service
public class ConfiguracaoNotificacaoService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguracaoNotificacaoService.class);

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ConfiguracaoNotificacaoRepository configuracaoNotificacaoRepository;

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
		configBd.setItens(null);
		configBd = configuracaoNotificacaoRepository.save(configBd);
		configBd.setItens(configuracaoNotificacao.getItens());
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
