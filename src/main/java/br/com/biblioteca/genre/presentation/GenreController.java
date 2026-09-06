package br.com.biblioteca.genre.presentation;

import br.com.biblioteca.models.Genero;
import br.com.biblioteca.repositories.GeneroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class GenreController {
    private final GeneroRepository generoRepository;

    public GenreController(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @GetMapping(value = "/generos")
    public ModelAndView listGenres() {
        ModelAndView model = new ModelAndView("dashboard/generos/index");
        Page<Genero> generos = generoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
        model.addObject("generos", generos);
        return model;
    }

    @GetMapping(value = "/generos/novo")
    public ModelAndView newGenre() {
        return new ModelAndView("dashboard/generos/novo");
    }

    @PostMapping(value = "/generos/salvar")
    public ModelAndView saveGenre(Genero genero) {
        generoRepository.save(genero);
        ModelAndView model = new ModelAndView("dashboard/generos/index");
        Page<Genero> genre = generoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
        model.addObject("generos", genre);
        return model;
    }

    @GetMapping(value = "/generos/deletar/{id}")
    public ModelAndView deleteGenre(@PathVariable("id") Long id) {
        generoRepository.deleteById(id);
        ModelAndView model = new ModelAndView("dashboard/generos/index");
        Page<Genero> genres = generoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
        model.addObject("generos", genres);
        return model;
    }

    @GetMapping(value = "/generos/editar/{id}")
    public ModelAndView editGenre(@PathVariable("id") Long id) {
        Optional<Genero> genre = generoRepository.findById(id);
        if(genre.isEmpty()) {
            return new ModelAndView("dashboard/generos/novo");
        }
        ModelAndView model = new ModelAndView("dashboard/generos/editar");
        model.addObject("genero", genre.get());
        return model;
    }
}
