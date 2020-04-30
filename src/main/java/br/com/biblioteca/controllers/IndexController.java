package br.com.biblioteca.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.biblioteca.models.Role;
import br.com.biblioteca.models.Usuarios;
import br.com.biblioteca.repositories.LivrosRepository;
import br.com.biblioteca.repositories.UsuariosRepository;

@Controller
public class IndexController {
	@Autowired
	private UsuariosRepository usuarioRepository;
	
	@Autowired
	private LivrosRepository livrosRepository;
	
	@GetMapping(value = "/")
	public ModelAndView index() {
		ModelAndView model = new ModelAndView("index");
		return model;
	}
	@GetMapping(value="/login")
	public ModelAndView login() {
		ModelAndView model = new ModelAndView("login");
		return model;
	}
	@GetMapping("/entrar")
	public String entrar() {
		User principal =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuarios usuario = usuarioRepository.findUsuarioByLogin(principal.getUsername());
		for( Role role :(List<Role>) usuario.getAuthorities()) {
			if(role.getNome() == "ROLE_USER") {
				return "redirect:/perfil/"; 
			}else{
				return "redirect:/dashboard/";
			}
		}
		return "redirect:/home";
	}
}
