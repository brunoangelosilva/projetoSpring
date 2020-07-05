package com.example.algamoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.example.algamoney.api.config.token.CustonTokenEnhancer;

@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager; // responsavel por autenticar o objeto de autenticação (
															// cliente) .

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() // em memoria
				.withClient("angular") // cliente (aplicação que está solicitando ao nosso Authorizationserver , naõ
										// confundir com usuario final.
				.secret("@ngul@r0") // senha do cliente
				.scopes("read", "write", "delete") // escopo de acesso
				.authorizedGrantTypes("password", "refresh_token")// password flow, refresh token serve para solicitar
																	// um novo access token (que nesse caso dura 20 seg.) sem precisar utilizar o
																	// usuario e senha no body da requisição
				.accessTokenValiditySeconds(1800) // tempo de duração do token.
				.refreshTokenValiditySeconds(3600 * 24)  // tempo de duração do refresh token
			.and() 
				.withClient("mobile")  // outra client aplicação, por ex. mobile
				.secret("m0b1l30") 
				.scopes("read") 
				.authorizedGrantTypes("password", "refresh_token")						
				.accessTokenValiditySeconds(1800) // tempo de duração do token.
				.refreshTokenValiditySeconds(3600 * 24); // tempo de duração do refresh token
			
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain(); //token com algo a mais no token
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter())); // uma lista de objetos que vão trabalham com os tokens
		
		endpoints.tokenStore(tokenStore()) // armazenamento do token (em memoria)
				.tokenEnhancer(tokenEnhancerChain) // adicionar mais informações ao token, no exemplo o nome do usuario.( para mostrar no angular)
				.reuseRefreshTokens(false) // para não reusar o refresh token.
				.authenticationManager(authenticationManager); // define o authentication manager
	}

	private TokenEnhancer tokenEnhancer() {		
		return new CustonTokenEnhancer();
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks");
		return accessTokenConverter;
	}
}
