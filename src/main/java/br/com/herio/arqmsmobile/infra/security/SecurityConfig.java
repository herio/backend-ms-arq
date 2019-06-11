package br.com.herio.arqmsmobile.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Order(HIGHEST_PRECEDENCE)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    protected EntryPointUnauthorizedHandler unauthorizedHandler;

    @Bean
    public FiltroValidadorToken authenticationTokenFilterBean() throws Exception {
        final FiltroValidadorToken authenticationTokenFilter = new FiltroValidadorToken();
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
        httpSecurity
                .authorizeRequests()
                //options
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //apple
                .antMatchers("/apple-app-site-association").permitAll()
                //management públicos
                .antMatchers("/management/**").permitAll()
                //demais públicos
                .antMatchers("/auth/**", "/publico/**").permitAll()
                //swagger
                .antMatchers("/v2/**", "swagger-resources/configuration/ui", "/swagger-resources/**", "/configuration/app", "/swagger-ui.html", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(this.unauthorizedHandler,
                        new AntPathRequestMatcher("/**"));
    }
	
    protected void adicionaFiltroCustomizadoAutenticacaoTokenJWT(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}

