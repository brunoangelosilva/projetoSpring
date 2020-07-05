package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;


@Configuration // identificar que é uma classe de configuração de segurança
@EnableWebSecurity // habilitar a seguranca 
@EnableResourceServer  
@EnableGlobalMethodSecurity(prePostEnabled = true) //habilitar a segurança dos métodos
public class ResourceServerConfig extends ResourceServerConfigurerAdapter { // vai fazer o papel do resource server do oauth2
	
		 	@Autowired
		 	private UserDetailsService userDetailsService;
			//metodos para sobrescrever para customizar a configuração.
			@Autowired
			public void configure(AuthenticationManagerBuilder auth) throws Exception { //metodo injetado
				//auth.inMemoryAuthentication() // trabalhando em memoria
				// .withUser("admin").password("admin").roles("ROLE");
				auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
			}

			@Override
			public void configure(HttpSecurity http) throws Exception { 
				http.authorizeRequests()
					.antMatchers("/categorias").permitAll() // permitir acessar sem autenticação somente categorias					
					.anyRequest().authenticated()// qualquer requisição vai precisar ser autenticada
					.and()
				//.httpBasic().and()  // autenticação basic
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()//não manter sessão nenhum , nenhum estado no servido 
				.csrf().disable();//csrf = basicamente vc fazer um javascript injection de um serviço web, como não tem disabilita				
			}

			@Override
				public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
					resources.stateless(true); // reforço de configuração para não manter sessão , nenhum estado no servidor
				}
			
			@Bean
			public PasswordEncoder passwordEncoder() {				
				return new BCryptPasswordEncoder();
			}			
		
			@Bean
			public MethodSecurityExpressionHandler createExpressionHandler() {
				return new OAuth2MethodSecurityExpressionHandler(); // handler para conseguir fazer segurança dos metodos com OAuth2 
			}
		   
}
