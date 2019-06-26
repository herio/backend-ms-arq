package br.com.herio.arqmsmobile.infra.log;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

public class AppCommonsRequestLoggingFilter extends CommonsRequestLoggingFilter {

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		if (request.getRequestURL().toString().contains("/auth")) {
			// payload da autenticacao nao eh logado
			if (message.indexOf("payload") > 0) {
				logger.debug(message.substring(0, message.indexOf("payload")));
			}
		} else {
			logger.debug(message);
		}
	}
}
