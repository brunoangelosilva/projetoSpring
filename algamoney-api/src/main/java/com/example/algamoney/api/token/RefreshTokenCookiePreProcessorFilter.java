package com.example.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component // tornar componente do spring
@Order(Ordered.HIGHEST_PRECEDENCE) // filtro com propriedade alta para ser analisado antes de qualquer um
public class RefreshTokenCookiePreProcessorFilter implements Filter{

	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	
			HttpServletRequest req = (HttpServletRequest) request; // faz cast do request para HttpServletRequest
			
			if("/oauth/token".equalsIgnoreCase(req.getRequestURI()) // se na uri da request tem oauth/token
					&& "refresh_token".equals(req.getParameter("grant_type")) // se no body tiver um grand_type com value refresh_token
				   && req.getCookies() != null) {
				
				for(Cookie cookie : req.getCookies()) {
					if(cookie.getName().equals("refreshToken")){ // se o cookie tiver um nome de refresToken que foi dado ao cookie
						String refreshToken = cookie.getValue(); //captura o refeshToken;
																 // não pode simplimente alterar o map de parametros, pq não podem ser alterados.
						req = new MyServletRequestWrapper(req, refreshToken); // crio novo objeto que tem a requisição 
																			// e o refreshToken para serem retornados
					}
				}
			}
			 
			chain.doFilter(req, response); // passo a wapper da requisição criada com o response. 
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
			
	}
	
	@Override
	public void destroy() {
		
	}
	
	static class MyServletRequestWrapper extends HttpServletRequestWrapper{

		private String refreshToken;
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request); 
			this.refreshToken = refreshToken; // refreshToken recuperado do cookie;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() { // subscreve o metodo getParameterMap 

			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap()); // crio um novo parameterMap com os parametros do parametro existente
			map.put("refresh_token", new String[] {refreshToken});    //e adiciono o refresh token no map de parametros
			map.setLocked(true); // trava o mapa da requisição
			return map;
		}
		
	}
}
