package com.example.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;

@Component //para que a classe seja um componente do spring
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> { //ouvir o evento do RecursoCriadoEvent

	@Override //implementa o metodo definido pela interface ApplicationListener
	public void onApplicationEvent(RecursoCriadoEvent recursoCriadoEvent) {
		HttpServletResponse response = recursoCriadoEvent.getResponse();
		Long codigo = recursoCriadoEvent.getCodigo();
		
		adicionarHeaderLocation(response, codigo);		
	}

	private void adicionarHeaderLocation(HttpServletResponse response, Long codigo) {
		URI uri  = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri(); //utilizando a classe ServletUriComponentsBuilder para pegar a requisição atual e setar o codigo da categoria criada e depois
		response.setHeader("Location", uri.toASCIIString()); // inserir no header do response essa uri.		
	}

}
