package br.com.biblioteca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/perfil")
public class PerfilController {
	@GetMapping("/")
	public ModelAndView index(){
		ModelAndView model = new ModelAndView();	
		return model;
	}
	@GetMapping("/cadastrar")
	public ModelAndView cadastrar() {
		ModelAndView model = new ModelAndView("perfil/novo");
		return model;
	}
	
}
