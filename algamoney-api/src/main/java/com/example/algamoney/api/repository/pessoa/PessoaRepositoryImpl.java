package com.example.algamoney.api.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.Pessoa_;
import com.example.algamoney.api.repository.filter.PessoaFilter;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Pessoa> filtrar(PessoaFilter pessoafilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder(); // cria um criteriabuilder
		CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class); // cria o criteriaQuery
		Root<Pessoa> root = criteria.from(Pessoa.class); // cria o root

		// cria as restrições com predicates
		Predicate[] predicates = criarRestricoes(pessoafilter, builder, root);
		criteria.where(predicates); // cria o where

		TypedQuery<Pessoa> query = manager.createQuery(criteria); // cria a query
		
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(pessoafilter));
	}

	private Long total(PessoaFilter pessoaFilter) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder(); //cria o builder pelo manager
		CriteriaQuery<Long> query = builder.createQuery(Long.class); // cria a query com retorno de Long
		Root<Pessoa> root = query.from(Pessoa.class); // especifica qual classe vai utlizar
		
		Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);
		query.where(predicates); // adiciona os predicates ( as restrições) do where
		
		query.select(builder.count(root)); // da um select count(*)
		
		return manager.createQuery(query).getSingleResult();// utilizou o getSingleResult pois vai retornar 1 registro do count
	}
	
	private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder builder,
			Root<Pessoa> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(pessoaFilter.getNome())) {
			predicates.add(builder.like(builder.lower(root.get(Pessoa_.nome)),
					"%" + pessoaFilter.getNome().toLowerCase() + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistroPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistroPorPagina);
	}
	
	
}
