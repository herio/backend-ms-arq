package br.com.herio.arqmsmobile.infra.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {
	@Autowired
	private HttpServletRequest request;

	@Override
	public void apply(RequestTemplate template) {
		if(request.getHeader("Authorization") != null) {
			template.header("Authorization", request.getHeader("Authorization"));
		}
		template.header("accept", "application/json");
	}
}
