package com.example.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{ //criar um processador

	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {		
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) { 
		
		HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest(); // tem que transformar de ServerHttpRequest para HtttpServletRequest  
		HttpServletResponse resp = ((ServletServerHttpResponse)response).getServletResponse(); // tem que transformar de ServerHttpResponse para HtttpServletResponse
	
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body; //faz um cast do body (Auth2AccessToken) para DefautlOAuthAccessToken 
																		  //para assim remover o refreshToken do body	
		String refreshToken = body.getRefreshToken().getValue();
		adicionarRefreshTokenNoCookie(refreshToken, req, resp); // para adicionar o refreshtoken no cookie precisa do resquest e response
		
		removerRefreshTokenDoBody(token);// retira o refreshToken do body
		
		return body;
	}

	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);  
		
	}

	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		 Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		 refreshTokenCookie.setHttpOnly(true);
		 refreshTokenCookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps()); 
		 refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		 refreshTokenCookie.setMaxAge(2592000);
		 resp.addCookie(refreshTokenCookie);
		
	}

}
