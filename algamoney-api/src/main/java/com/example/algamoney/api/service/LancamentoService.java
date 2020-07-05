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

	public Lancamento buscarPorId(Long codigo) {

		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		if (lancamentoSalvo == null) {
			throw new EmptyResultDataAccessException(1);
		}

		return lancamentoSalvo;
	}

	public Lancamento salvar(@Valid Lancamento lancamento) {
		validarPessoa(lancamento);

		return lancamentoRepository.save(lancamento);
	}

	public Lancamento AtualizarLancamento(Long codigo, Lancamento lancamento) {

		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		
		if(!lancamentoSalvo.getPessoa().getCodigo().equals(lancamento.getPessoa().getCodigo())) { // se estou alterando a pessoa do lançamento
			validarPessoa(lancamento);// verifico se a pessoa é valida
		}
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo"); // atualiza o lancamentoSalvo a partir do
																			// lancamento, ingnorando o codigo;
		return lancamentoRepository.save(lancamentoSalvo);
	}

	public Pessoa validarPessoa(Lancamento lancamento) {

		Pessoa pessoa = null;
		if (lancamento.getPessoa().getCodigo() != null) {
			pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		}

		if (pessoa == null || pessoa.isInativo()) { // se a pessoa não existir ou não estiver ativa levanta excessão
			throw new PessoaInexistenteOuInativoException();
		}

		return pessoa;
	}

	public Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		if (lancamentoSalvo == null) {
			throw new IllegalArgumentException(); 
		}
		return lancamentoSalvo;

	}
}
