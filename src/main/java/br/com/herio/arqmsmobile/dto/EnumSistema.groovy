package br.com.herio.arqmsmobile.dto

import org.apache.commons.text.StringEscapeUtils

enum EnumSistema {
	NOTICIAS_JURIDICAS("Not&iacute;cias Jur&iacute;dicas", "https://noticias-juridicas.herokuapp.com/publico/icone_app.png",
	"https://noticias-juridicas.herokuapp.com/publico/default-avatar.png");

	String nome;
	String icone;
	String defaultAvatar;

	EnumSistema(String nome, String icone, String defaultAvatar) {
		this.nome = nome;
		this.icone = icone;
		this.defaultAvatar = defaultAvatar;
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
}
