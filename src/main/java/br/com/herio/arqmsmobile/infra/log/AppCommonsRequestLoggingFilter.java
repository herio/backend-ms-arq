package br.com.herio.arqmsmobile.infra.log;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

public class AppCommonsRequestLoggingFilter extends CommonsRequestLoggingFilter {
	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		message = removeAtributo(message, "Bearer");
		message = removeAtributo(message, "v1");
		if (!request.getRequestURL().toString().contains("/management") && !request.getRequestURL().toString().contains("/actuator")) {
			logger.debug(message);
		}
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		message = removeAtributo(message, "Bearer");
		message = removeAtributo(message, "v1");
		if (request.getRequestURL().toString().contains("/auth") || request.getRequestURL().toString().contains("/autenticacao")) {
			// payload da autenticação não é logado
			if (message.indexOf("payload") > 0) {
				message = message.substring(0, message.indexOf("payload"));
			}
		}
		if (!request.getRequestURL().toString().contains("/management") && !request.getRequestURL().toString().contains("/actuator")) {
			logger.debug(message);
		}
	}

	private String removeAtributo(String message, String chaveAtributo) {
		int indiceInicioBearer = message.indexOf(chaveAtributo) - 1;
		if (indiceInicioBearer > 0) {
			int indiceTerminoBearer = message.indexOf(",", indiceInicioBearer);
			if (indiceTerminoBearer > 0) {
				String antesBearer = message.substring(0, indiceInicioBearer);
				String aposBearer = message.substring(indiceTerminoBearer);
				return antesBearer + aposBearer;
			}
		}
		return message;
	}
}
