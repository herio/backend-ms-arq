package br.com.herio.arqmsmobile.infra.log;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class AppCommonsRequestLoggingFilter extends CommonsRequestLoggingFilter {

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        if(request.getRequestURL().toString().contains("/auth")) {
            //payload da autenticação não é logado
            if(message.indexOf("payload") > 0) {
                logger.debug(message.substring(0, message.indexOf("payload")));
            }
        } else {
            logger.debug(message);
        }
    }
}
