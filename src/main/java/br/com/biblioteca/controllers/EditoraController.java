package br.com.biblioteca.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.biblioteca.models.Editora;
import br.com.biblioteca.repositories.EditorasRepository;
import br.com.biblioteca.repositories.LogradouroRepository;

@Controller
@RequestMapping(value = "/editoras")
public class EditoraController {
	@Autowired
	private EditorasRepository editoraRepository;
	
	@Autowired
	private LogradouroRepository logradouroRepository;
	
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView model = new ModelAndView("editoras/index");
		Page<Editora> editoras = editoraRepository.findAll(PageRequest.of(0, 5,Sort.by("nome")));
		model.addObject("editoras",editoras);
		return model;
	}
	@GetMapping("/{id}")
	public ModelAndView id(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("editoras/unique");
		Optional<Editora> editora = editoraRepository.findById(id);
		editora.get().setLogradouro(logradouroRepository.getLogradouroBydIdEditora(id));
		model.addObject("editora",editora.get());
		return model;
	}
}
