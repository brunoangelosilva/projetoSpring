package com.example.algamoney.api.service;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativoException;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Lancamento AtualizarLancamento(Long codigo, Lancamento lancamento) {

		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo).orElse(null);
		if (lancamentoSalvo == null) {
			throw new EmptyResultDataAccessException(1); // levanta excessão de resultado vazio experava pelo menos 1 resultado.
		}
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo"); // atualiza o lancamentoSalvo a partir do
																			// lancamento, ingnorando o codigo;
		return lancamentoRepository.save(lancamentoSalvo);
	}

	public Lancamento buscarPorId(Long codigo) {

		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo).orElse(null);
		if (lancamentoSalvo == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return lancamentoSalvo;
	}

	public Lancamento salvar(@Valid Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo()).orElse(null);
		if(pessoa==null || pessoa.isInativo() ) {  // se a pessoa não existir ou não estiver ativa levanta excessão
			throw new PessoaInexistenteOuInativoException();
		}
		
		return lancamentoRepository.save(lancamento);
	}

}
