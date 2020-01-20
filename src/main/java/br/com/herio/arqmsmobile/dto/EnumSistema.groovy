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
	MEU_COACH_OAB("Meu Coach OAB", "https://coach-oab.herokuapp.com/publico/icone.png",
	"https://coach-oab.herokuapp.com/publico/default-avatar.png", "1ijvzuupj8sBY2GgV3irM7bgDQX5JDPGk",
	"https://coach-oab.herokuapp.com",);

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

	public static List<ConfiguracaoNotificacaoItem> getConfigsItensDefault(EnumSistema sistema) {
        List<ConfiguracaoNotificacaoItem> configs = new ArrayList<>();
		switch (sistema) {
			case NOTICIAS_JURIDICAS:
                ConfiguracaoNotificacaoItem configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("DESTAQUE");
				configItem.setTipoPeriodicidade("DOIS_POR_DIA");
                configs.add(configItem);
				break;
			case NOTICIAS_JURIDICAS_PAGO:
                ConfiguracaoNotificacaoItem configItem = new ConfiguracaoNotificacaoItem();
				configItem.setTipoItem("DESTAQUE");
				configItem.setTipoPeriodicidade("DOIS_POR_DIA");
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
