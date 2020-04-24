package br.com.biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
