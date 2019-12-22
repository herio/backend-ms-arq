package br.com.herio.arqmsmobile.dto

import org.apache.commons.text.StringEscapeUtils

import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacaoItem

enum EnumSistema {
	NOTICIAS_JURIDICAS("Not&iacute;cias Jur&iacute;dicas", "https://noticias-juridicas.herokuapp.com/publico/icone.png",
	"https://noticias-juridicas.herokuapp.com/publico/default-avatar.png", "1qk7108N-6xW613ez3abtPfDiWahYnJ4E",
	"https://noticias-juridicas.herokuapp.com"),
	NOTICIAS_JURIDICAS_PAGO("Not&iacute;cias Jur&iacute;dicas", "https://noticias-juridicas.herokuapp.com/publico/icone_pago.png",
	"https://noticias-juridicas.herokuapp.com/publico/default-avatar.png", "1qk7108N-6xW613ez3abtPfDiWahYnJ4E",
	"https://noticias-juridicas.herokuapp.com"),
	ADVOGADO_COMUNITARIO("Advogado Comunit&aacute;rio", "https://advogado-comunitario.herokuapp.com/publico/icone.png",
	"https://advogado-comunitario.herokuapp.com/publico/default-avatar.png", "1lYrXOmD4CT79jxNfXxl8wOk9BOD2FQTP",
	"https://advogado-comunitario.herokuapp.com",),
	MEU_COACH_OAB("Meu Coach OAB", "https://meu-coach-oab.herokuapp.com/publico/icone.png",
	"https://meu-coach-oab.herokuapp.com/publico/default-avatar.png", "1pObQJ5qW2dp2qssrBEM0adDIonTXR8mL",
	"https://meu-coach-oab.herokuapp.com",);

	String nome;
	String icone;
	String defaultAvatar;
	String uploadFolder;
	String urlBase;
	EnumSistema(String nome, String icone, String defaultAvatar, String uploadFolder, String urlBase) {
		this.nome = nome;
		this.icone = icone;
		this.defaultAvatar = defaultAvatar;
		this.uploadFolder = uploadFolder;
		this.urlBase = urlBase;
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

	public static ConfiguracaoNotificacaoItem getConfigItemDefault(EnumSistema sistema) {
		ConfiguracaoNotificacaoItem configItem = null;
		switch (sistema) {
			case NOTICIAS_JURIDICAS:
				configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("DESTAQUE");
				configItem.setTipoPeriodicidade("DOIS_POR_DIA");
				break;
			case NOTICIAS_JURIDICAS_PAGO:
				configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("DESTAQUE");
				configItem.setTipoPeriodicidade("DOIS_POR_DIA");
				break;
			case ADVOGADO_COMUNITARIO:
				break;
			default:
				break;
		}
		return configItem;
	}
}
