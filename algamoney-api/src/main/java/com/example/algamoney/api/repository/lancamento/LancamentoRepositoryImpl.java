package com.example.algamoney.api.repository.lancamento;

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

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Lancamento_;
import com.example.algamoney.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder(); // cria um criteriabuilder
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class); // cria o criteriaQuery
		Root<Lancamento> root = criteria.from(Lancamento.class); // cria o root

		// cria as restrições com predicates
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates); // cria o where

		TypedQuery<Lancamento> query = manager.createQuery(criteria); // cria a query
		
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}



	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));

		}

		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoDe()));

		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoAte()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private Long total(LancamentoFilter lancamentoFilter) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder(); //cria o builder pelo manager
		CriteriaQuery<Long> query = builder.createQuery(Long.class); // cria a query com retorno de Long
		Root<Lancamento> root = query.from(Lancamento.class); // especifica qual classe vai utlizar
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		query.where(predicates); // adiciona os predicates ( as restrições) do where
		
		query.select(builder.count(root)); // da um select count(*)
		
		return manager.createQuery(query).getSingleResult();// utilizou o getSingleResult pois vai retornar 1 registro do count
	}
	
	private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistroPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistroPorPagina);
	}

}
