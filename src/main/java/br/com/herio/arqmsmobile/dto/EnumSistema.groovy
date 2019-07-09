package br.com.herio.arqmsmobile.dto

enum EnumSistema {
	NOTICIAS_JURIDICAS("Notícias Jurídicas", "https://noticias-juridicas.herokuapp.com/publico/icone_app.png");

	String nome;
	String icone;

	EnumSistema(String nome, String icone) {
		this.nome = nome;
		this.icone = icone;
	}

	String getNome() {
		return this.nome;
	}

	String getIcone() {
		return this.icone;
	}
}
