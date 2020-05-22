package com.example.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.UsuarioRepository;


@RestController//basicamente já transformar o retorno em json por exemplo
@RequestMapping("/usuario")//mapeamento da requisição 
public class UsuarioResource {
	
	@Autowired// procure uma implenetçaão de PessoaRepository e me entregue
	private UsuarioRepository usuarioRepository;
	
	@Autowired // serve para dispara um evento no spring
	private ApplicationEventPublisher publisher; // publicador de applicationEvent
	
	@GetMapping  //mapeamento do get para essa Pessoa
	public List<Usuario> listar(){
		return usuarioRepository.findAll();	
	}
	
	@PostMapping
	public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario , HttpServletResponse response){
		
		Usuario usuarioSalvo  = usuarioRepository.save(usuario);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
		
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Optional<Usuario>> buscarPorId(@PathVariable Long codigo){		
		Optional<Usuario> usuario = usuarioRepository.findById(codigo);
		return usuario.isPresent() ? ResponseEntity.ok(usuario):ResponseEntity.notFound().build();		
	}
	
	@DeleteMapping("{codigo}") // mapeamento do delete 
	@ResponseStatus(HttpStatus.NO_CONTENT)// retorna 204, foi executado com sucesso mais não tem nada pra retornar
	public void remover(@PathVariable Long codigo) {
		usuarioRepository.deleteById(codigo);
	}
	
}
