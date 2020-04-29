package br.com.biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.biblioteca.repositories.LivrosRepository;

@Controller
@RequestMapping("/livros")
public class LivrosController {
	
	@Autowired
	private LivrosRepository livrosRepository;
	
	@GetMapping("/{id}")
	public ModelAndView id(@PathVariable("id")Long id) {
		ModelAndView model = new ModelAndView("livros/unique");
		model.addObject("livro",livrosRepository.findById(id).get());
		return model;
	}
}
