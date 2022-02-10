package br.com.herio.arqmsmobile.infra.app;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.herio.arqmsmobile.infra.feign.FeignErrorDecoder;
import br.com.herio.arqmsmobile.infra.feign.FeignRequestInterceptor;
import br.com.herio.arqmsmobile.infra.log.AppCommonsRequestLoggingFilter;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;

@Order(HIGHEST_PRECEDENCE)
@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/",
			"classpath:/public/" };

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}

	// CORS
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	// LOGS
	@Bean
	public AppCommonsRequestLoggingFilter requestLoggingFilter() {
		AppCommonsRequestLoggingFilter loggingFilter = new AppCommonsRequestLoggingFilter();
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludeHeaders(true);
		loggingFilter.setIncludePayload(true);
		return loggingFilter;
	}

	// FEIGN
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	public ErrorDecoder feignErrorDecoder() {
		return new FeignErrorDecoder();
	}

	@Bean
	public RequestInterceptor FeignRequestInterceptor() {
		return new FeignRequestInterceptor();
	}

}