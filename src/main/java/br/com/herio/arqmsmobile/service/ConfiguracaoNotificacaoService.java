package br.com.herio.arqmsmobile.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacao;
import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacaoItem;
import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacaoRepository;
import br.com.herio.arqmsmobile.dominio.Notificacao;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;

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
		configBd = configuracaoNotificacaoRepository.save(configBd);

		if (configBd.isReceberNotificacao()) {
			enviaNotificacao(configBd);
		}
		return configBd;
	}

	public ConfiguracaoNotificacao recuperarConfiguracao(Long idUsuario) {
		try {
			return configuracaoNotificacaoRepository.findByUsuarioId(idUsuario).get();
		} catch (NoSuchElementException e) {
			LOGGER.debug("ConfiguracaoNotificacao recuperarConfiguracao", e);
			return null;
		}
	}

	private void enviaNotificacao(ConfiguracaoNotificacao configBd) {
		Notificacao notificacao = new Notificacao();
		notificacao.setTitulo("Configurações atualizadas com sucesso");
		notificacao.setConteudo(String.format("A partir de agora você receberá notificações de acordo com as suas configurações pessoais!"));
		notificacaoService.enviaNotificacaoParaTodosDispositivosUsuario(configBd.getUsuario().getId(), notificacao);
	}

}
