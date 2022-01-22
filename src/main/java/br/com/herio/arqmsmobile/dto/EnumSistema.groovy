package br.com.herio.arqmsmobile.dto

import org.apache.commons.text.StringEscapeUtils

import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacaoItem

enum EnumSistema {
	ADVOGADO_COMUNITARIO("Advogado Comunit&aacute;rio", "https://advogado-comunitario.herokuapp.com/publico/icone.png",
	"https://advogado-comunitario.herokuapp.com/publico/default-avatar.png", "1lYrXOmD4CT79jxNfXxl8wOk9BOD2FQTP",
	"https://advogado-comunitario.herokuapp.com", true),
	MEU_COACH_OAB("Meu Coach OAB", "https://coach-oab.herokuapp.com/publico/icone.png",
	"https://coach-oab.herokuapp.com/publico/default-avatar.png", "1ijvzuupj8sBY2GgV3irM7bgDQX5JDPGk",
	"https://coach-oab.herokuapp.com", true),
	ESTUDANDO_JESUS_COM_JACK_DARSA("Estudando Jesus com Jack Darsa", "https://estudando-jesus-com-jack-darsa.herokuapp.com/publico/icone.png",
	"https://estudando-jesus-com-jack-darsa.herokuapp.com/publico/default-avatar.png", "17uQFFi6FuN2ZhhZhjxfizaS_g6iFYlK_",
	"https://estudando-jesus-com-jack-darsa.herokuapp.com", false);

	String nome;
	String icone;
	String defaultAvatar;
	String uploadFolder;
	String urlBase;
	boolean enviarEmail;
	EnumSistema(String nome, String icone, String defaultAvatar, String uploadFolder, String urlBase, boolean enviarEmail) {
		this.nome = nome;
		this.icone = icone;
		this.defaultAvatar = defaultAvatar;
		this.uploadFolder = uploadFolder;
		this.urlBase = urlBase;
		this.enviarEmail = enviarEmail;
	}

	String getNome() {
		//necessário por problemas de encoding ao enviar email se a palavra estive acentuada
		return StringEscapeUtils.unescapeHtml4(this.nome);
	}

	String getIcone() {
		return this.icone;
	}

	String getDefaultAvatar() {
		return this.defaultAvatar;
	}

	public String getUploadFolder() {
		return uploadFolder;
	}

	public String getUrlDownloadFotos() {
		return urlDownloadFotos;
	}

	public String getUrlBase() {
		return urlBase;
	}

	public boolean isEnviarEmail() {
		return enviarEmail;
	}

	public static List<ConfiguracaoNotificacaoItem> getConfigsItensDefault(EnumSistema sistema) {
		List<ConfiguracaoNotificacaoItem> configs = new ArrayList<>();
		switch (sistema) {
			case ESTUDANDO_JESUS_COM_JACK_DARSA:
				ConfiguracaoNotificacaoItem configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("ESTUDOS");
				configItem.setTipoPeriodicidade("DIARIO");
				configItem.setValorItem(Boolean.TRUE.toString())
				configs.add(configItem);
				configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("LIVES");
				configItem.setTipoPeriodicidade("DIARIO");
				configItem.setValorItem(Boolean.TRUE.toString())
				configs.add(configItem);
				break;
			case MEU_COACH_OAB:
				ConfiguracaoNotificacaoItem configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("PLANOS_ESTUDOS");
				configItem.setTipoPeriodicidade("DIARIO");
				configItem.setValorItem(Boolean.TRUE.toString())
				configs.add(configItem);
				configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("SIMULADOS");
				configItem.setTipoPeriodicidade("DIARIO");
				configItem.setValorItem(Boolean.TRUE.toString())
				configs.add(configItem);
				configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("MENSAGENS_MOTIVACIONAIS");
				configItem.setTipoPeriodicidade("DIARIO");
				configItem.setValorItem(Boolean.TRUE.toString())
				configs.add(configItem);
				break;
			case ADVOGADO_COMUNITARIO:
				break;
			default:
				break;
		}
		return configs;
	}
}
