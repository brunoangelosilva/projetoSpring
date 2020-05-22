package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository; 
	
	public Usuario atualizarUsuario(Long codigo, Usuario usuario) {
		Usuario usuarioSalvo = usuarioRepository.findById(codigo).orElse(null);
		if(usuarioSalvo==null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		BeanUtils.copyProperties(usuario, usuarioSalvo, "codigo");//source = a usuairo que recebo por parametro
																	//target = a usuario que busquei do banco
																	//ignoreProperties = campos que quero que ignore na copia.
		
		return usuarioRepository.save(usuarioSalvo);
	}

}
