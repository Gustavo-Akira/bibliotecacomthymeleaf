package br.com.biblioteca.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.biblioteca.models.Usuarios;
import br.com.biblioteca.repositories.UsuariosRepository;

@Service
public class ImplementUserService implements UserDetailsService {
	
	@Autowired
	private UsuariosRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuarios user = repository.findUsuarioByLogin(username);
		if(user == null) {
			throw new UsernameNotFoundException("Usuario não foi encontrado");
		}
		return new User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getAuthorities());
	}

}