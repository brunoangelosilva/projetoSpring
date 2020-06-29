package com.example.algamoney.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService{

	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);	
		Usuario usuario = usuarioOptional.orElseThrow(()-> new UsernameNotFoundException("Usuário e/ou senha incorretos!"));
		return new User(email, usuario.getSenha(), getPermissao(usuario)); // returna o email = login, senha  e as permissões do usuario 
		
	}

	private Collection<? extends GrantedAuthority> getPermissao(Usuario usuario) { //adiciona as permissões do usuário a lista de authorities
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getPermissoes().forEach(p-> authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));		
		return authorities;
	}

}
