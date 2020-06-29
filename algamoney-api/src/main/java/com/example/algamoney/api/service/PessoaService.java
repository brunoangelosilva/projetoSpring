package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {  // classe para regra de negócio (insert, update, delete)
	
	@Autowired//injetar PessoaRepository
	private PessoaRepository pessoaRepository;

	public Pessoa pessoaAtualizar(Long codigo, Pessoa pessoa) {
		
		Pessoa pessoaSalva = buscarPessoaPorId(codigo); 
		if(pessoaSalva==null) {
			throw new EmptyResultDataAccessException(1); // levanta excessao  
		}
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");//source = a pessoa que recebo por parametro
																	//target = a pessoa que busquei do banco
																	//ignoreProperties = campos que quero que ignore na copia.
		return pessoaRepository.save(pessoaSalva);
	}	

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {		
		Pessoa pessoaSalva = buscarPessoaPorId(codigo);
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}
	
	private Pessoa buscarPessoaPorId(Long codigo) {
		Pessoa pessoaSalva = pessoaRepository.findOne(codigo);
		if(pessoaSalva==null) {
			throw new  EmptyResultDataAccessException(1); // levanta excessao 
		 }
		return pessoaSalva;
	}
}
