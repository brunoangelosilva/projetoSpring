package com.example.algamoney.api.resource;

import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;
import com.example.algamoney.api.service.CategoriaService;

@RestController //basicamente já transformar o retorno em json por exemplo
@RequestMapping("/categorias") //mapeamento da requisição 
public class CategoriaResource {
	
	@Autowired // procure uma implenetçaão de CategoriaRepository e me entregue
	private CategoriaRepository  categoriaRepository; //injeção da categoria repository
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired // serve para dispara um evento no spring
	private ApplicationEventPublisher publisher; // publicador de applicationEvent
	
	@GetMapping //mapeamento do get para essa categoria
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	@PostMapping // mapeamento do post (inserir) para categoria
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) { //HttpServletResponse serve 
																											//para identificar no header 
																											//como eu posso consultar o recurso criado
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo())); // this = quem gerou o evento ; response e código
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva); // retornando no body do response a categoria criada e substitui a anotação @ResponseStatus do método criar 																														 		
																		
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity <Categoria> buscarPorId(@PathVariable Long codigo) { //consultar uma categoria pelo código
		Categoria categoria =  categoriaRepository.findById(codigo).orElse(null);
		return categoria!=null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{codigo}")// mapeamento do delete 
	@ResponseStatus(HttpStatus.NO_CONTENT)// retorna 204, foi executado com sucesso mais não tem nada pra retornar
	public void remover(@PathVariable Long codigo) {
		categoriaRepository.deleteById(codigo);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Categoria> atualizar(@PathVariable Long codigo, @Valid @RequestBody Categoria categoria){
		Categoria categoriaSalva = categoriaService.atualizarCategoria(codigo, categoria);
		return ResponseEntity.ok(categoriaSalva);
		
	}
	
}
