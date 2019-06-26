package br.com.herio.arqmsmobile.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true, prePostEnabled = true)
@Order(99)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	protected AppAuthenticationEntryPoint unauthorizedHandler;

	@Bean
	public AppUsernamePasswordAuthenticationFilter authenticationTokenFilterBean() throws Exception {
		final AppUsernamePasswordAuthenticationFilter authenticationTokenFilter = new AppUsernamePasswordAuthenticationFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
		return authenticationTokenFilter;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		configuraSegurancaGenerica(httpSecurity);
		configuraEntryPointAutenticacao(httpSecurity);
		configuraSessao(httpSecurity);
		configuraRestricaoURLsAcesso(httpSecurity);
		adicionaFiltroCustomizadoAutenticacaoTokenJWT(httpSecurity);
	}

	protected void configuraSegurancaGenerica(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.csrf().disable();
	}

	protected ExceptionHandlingConfigurer<HttpSecurity> configuraEntryPointAutenticacao(HttpSecurity httpSecurity)
			throws Exception {
		return httpSecurity.exceptionHandling().authenticationEntryPoint(this.unauthorizedHandler);
	}

	protected void configuraSessao(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	protected void configuraRestricaoURLsAcesso(HttpSecurity httpSecurity) throws Exception {
		// @formatter:off
		httpSecurity.authorizeRequests()
				// options
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				// apple
				.antMatchers("/apple-app-site-association").permitAll()
				// testeProducao
				.antMatchers("/testeProducao/**").permitAll()
				// actuator
				.antMatchers("/actuator/**").permitAll()
				// demais publicos
				.antMatchers("/publico/**").permitAll()
				// swagger
				.antMatchers("/v2/api-docs", "/swagger/**", "swagger-resources/configuration/ui",
						"/swagger-resources/**", "/configuration/app", "/swagger-ui.html", "/webjars/**")
				.permitAll().anyRequest().authenticated();
		// @formatter:on
	}

	protected void adicionaFiltroCustomizadoAutenticacaoTokenJWT(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	}

}
