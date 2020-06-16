package com.example.algamoney.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.service.LancamentoService;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativoException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository; // injeção de dependencia do LancamentoRepository

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LancamentoService lancamentoService; // injeção de dependencia do LancamentoService
	
	@Autowired
	private ApplicationEventPublisher publisher; // publicador de AplicationEvent
	
	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter){ // listar todos os lançamentos
		return lancamentoRepository.filtrar(lancamentoFilter);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarPorId(@PathVariable Long codigo){
		Lancamento lancamento = lancamentoRepository.findById(codigo).orElse(null);
		return lancamento!=null? ResponseEntity.ok(lancamento): ResponseEntity.notFound().build();
	}
	
	@PostMapping //mapeamento para inserir lancamento
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento , HttpServletResponse response){
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	@ExceptionHandler({PessoaInexistenteOuInativoException.class})
	public ResponseEntity<Object> handlePessoaInexistenteOuInativoException(PessoaInexistenteOuInativoException ex){
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null,LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor)); // Cria a lista de erros
		return ResponseEntity.badRequest().body(erros);
	}
	
	@PutMapping
	public ResponseEntity<Lancamento> atualizarLancamento(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento){
		Lancamento lancamentoAtualizado = lancamentoService.AtualizarLancamento(codigo, lancamento);
		return ResponseEntity.ok(lancamentoAtualizado);
	}
	
	@DeleteMapping("/{codigo}") // mapeamento do deletar lancamento
	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna o codigo 204, excluiu com sucesso mais não retorna nada.
	public void remover(Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}
	
}
