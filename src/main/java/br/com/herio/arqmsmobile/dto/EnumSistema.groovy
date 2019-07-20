package br.com.herio.arqmsmobile.dto

import org.apache.commons.text.StringEscapeUtils

enum EnumSistema {
	NOTICIAS_JURIDICAS("Not&iacute;cias Jur&iacute;dicas", "https://noticias-juridicas.herokuapp.com/publico/icone_app.png",
	"https://noticias-juridicas.herokuapp.com/publico/default-avatar.png", "1qk7108N-6xW613ez3abtPfDiWahYnJ4E",
	"https://noticias-juridicas.herokuapp.com/publico/files/NOTICIAS_JURIDICAS/usuarios/%s/fotos/%s");

	String nome;
	String icone;
	String defaultAvatar;
	String uploadFolder;
	String downloadUrl;

	EnumSistema(String nome, String icone, String defaultAvatar, String uploadFolder, String downloadUrl) {
		this.nome = nome;
		this.icone = icone;
		this.defaultAvatar = defaultAvatar;
		this.uploadFolder = uploadFolder;
		this.downloadUrl = downloadUrl;
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

	public String getDownloadUrl() {
		return downloadUrl;
	}
}
