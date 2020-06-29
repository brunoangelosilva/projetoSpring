package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired // injetar o CategoriaRepository
	private CategoriaRepository categoriaRepository;
	
	public Categoria atualizarCategoria(Long codigo, Categoria categoria) {
		Categoria categoriaSalva = categoriaRepository.findOne(codigo);
		if(categoriaSalva==null) {
			throw new EmptyResultDataAccessException(1);
		}
		BeanUtils.copyProperties(categoria, categoriaSalva, "codigo");//source = a categoria que recebo por parametro
																	//target = a categoria que busquei do banco
																	//ignoreProperties = campos que quero que ignore na copia.
		return categoriaRepository.save(categoriaSalva);
		
	}
	
	public Categoria buscarPorId(Long codigo) {
		
		Categoria categoriaSalva = categoriaRepository.findOne(codigo);
		if(categoriaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return categoriaSalva;
	}

}
