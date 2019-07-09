package br.com.herio.arqmsmobile.dto

enum EnumSistema {
	NOTICIAS_JURIDICAS("Notícias Jurídicas", "https://noticias-juridicas.herokuapp.com/publico/icone_app.png",
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
		return this.nome;
	}

	String getIcone() {
		return this.icone;
	}

	String getDefaultAvatar() {
		return this.defaultAvatar;
	}
}
