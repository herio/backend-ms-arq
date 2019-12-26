package br.com.herio.arqmsmobile.infra.security.dto

import java.time.LocalDateTime

class DtoTokenAutenticacao {
	Long idUsuario;
	String token;
	LocalDateTime dataHora;
}
