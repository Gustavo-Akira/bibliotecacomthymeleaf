package br.com.biblioteca.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.biblioteca.models.Editora;
import br.com.biblioteca.models.Genero;
import br.com.biblioteca.models.Logradouro;
import br.com.biblioteca.models.Telefone;
import br.com.biblioteca.models.Usuarios;
import br.com.biblioteca.repositories.ComprasRepository;
import br.com.biblioteca.repositories.EditorasRepository;
import br.com.biblioteca.repositories.GeneroRepository;
import br.com.biblioteca.repositories.LivrosRepository;
import br.com.biblioteca.repositories.LogradouroRepository;
import br.com.biblioteca.repositories.RegistrosRepository;
import br.com.biblioteca.repositories.TelefoneRepository;
import br.com.biblioteca.repositories.UsuariosRepository;

@Controller
@RequestMapping(value = "dashboard")
public class DashboardController {
	@Autowired
	private UsuariosRepository usuarioRepository;
	
	@Autowired
	private LivrosRepository livrosRepository;
	
	@Autowired
	private EditorasRepository editorasRepository;
	
	@Autowired
	private ComprasRepository comprasRepository;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	@Autowired
	private RegistrosRepository registroRepository;
	
	@Autowired
	private LogradouroRepository logradouroRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@GetMapping(value = "/")
	public ModelAndView dashboard() {
		ModelAndView model = new ModelAndView("dashboard/index");
		return model;
	}
	@GetMapping(value="/usuarios")
	public ModelAndView usuarios() {
		ModelAndView model = new ModelAndView("dashboard/usuarios/index");
		Page<Usuarios> usuarios = usuarioRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		usuarios.forEach(e->{e.setLogradouros(logradouroRepository.getLogradouroBydIdUsuario(e.getId()));});
		model.addObject("usuarios",usuarios);
		return model;
	}
	@GetMapping(value = "/livros")
	public ModelAndView livros() {
		ModelAndView model = new ModelAndView("dashboard/livros/index");
		return model;
	}
	@GetMapping(value = "**/editoras")
	public ModelAndView editoras() {
		ModelAndView model = new ModelAndView("dashboard/editora/index");
		Page<Editora> editoras = editorasRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		model.addObject("editoras",editoras);
		return model;
	}
	@GetMapping(value="**/editoras/novo")
	public ModelAndView novaeditora() {
		ModelAndView model = new ModelAndView("dashboard/editora/novo");
		return model;
	}
	@PostMapping(value="**/editoras/salvar")
	public ModelAndView salvareditora(Editora editora,Logradouro logradouro) {
		editora = editorasRepository.save(editora);
		logradouro.setEditora(editora);
		logradouro = logradouroRepository.save(logradouro);
		ModelAndView model = new ModelAndView("dashboard/editora/index");
		return model;
	}
	@GetMapping(value = "/editoras/{id}")
	public ModelAndView editorasid(@PathVariable("id")Long id) {
	ModelAndView model = new ModelAndView("dashboard/editora/unique");
	Optional<Editora> editora = editorasRepository.findById(id);
	model.addObject("editora",editora.get());
	return model;
	}
	@GetMapping(value = "/generos")
	public ModelAndView generos() {
		ModelAndView model = new ModelAndView("dashboard/generos/index");
		Page<Genero> generos = generoRepository.findAll(PageRequest.of(0, 5,Sort.by("nome")));
		model.addObject("generos", generos);
		return model;
	}
	@GetMapping(value = "/generos/novo")
	public ModelAndView generosnovo() {
		ModelAndView model = new ModelAndView("dashboard/generos/novo");
		return model;
	}
	@PostMapping(value = "**/generos/salvar")
	public ModelAndView generosalvar(Genero genero) {
		generoRepository.save(genero);
		ModelAndView model = new ModelAndView("dashboard/generos/index");
		Page<Genero> generos = generoRepository.findAll(PageRequest.of(0, 5,Sort.by("nome")));
		model.addObject("generos", generos);
		return model;
	}
	@GetMapping(value="**/generos/deletar/{id}")
	public ModelAndView generoDeletar(@PathVariable("id") Long id) {
		generoRepository.deleteById(id);
		ModelAndView model = new ModelAndView("dashboard/generos/index");
		Page<Genero> generos = generoRepository.findAll(PageRequest.of(0, 5,Sort.by("nome")));
		model.addObject("generos", generos);
		return model;
	}
	@GetMapping(value="**/generos/editar/{id}")
	public ModelAndView generoEditar(@PathVariable("id") Long id) {
		Optional<Genero> genero= generoRepository.findById(id);
		ModelAndView model = new ModelAndView("dashboard/generos/editar");
		model.addObject("genero", genero.get());
		return model;
	}
	@GetMapping(value = "/compras")
	public ModelAndView compras() {
		ModelAndView model = new ModelAndView("dashboard/compras/index");
		return model;
	}
	@GetMapping(value = "/registros")
	public ModelAndView registros() {
		ModelAndView model = new ModelAndView("dashboard/registros/index");
		return model;
	}
	@PostMapping(value="/telefone/novo/editora/{id}")
	public ModelAndView novotelefone(Telefone telefone,@PathVariable("id") Long id) {
		Editora editora = editorasRepository.findById(id).get();
		telefone.setEditora(editora);
		telefoneRepository.save(telefone);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo","inserir");
		model.addObject("entidade","telefone");
		return model;
	}
	@GetMapping(value="logradouro/novo/editora/{id}")
	public ModelAndView novologradouro(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("dashboard/logradouro/novo");
		model.addObject("id",id);
		return model;
	}
	@PostMapping(value="logradouro/salvar/editora/{id}")
	public ModelAndView novologradouro(Logradouro logradouro,@PathVariable("id") Long id) {
		Editora editora = editorasRepository.findById(id).get();
		logradouro.setEditora(editora);
		logradouroRepository.save(logradouro);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo","inserção");
		model.addObject("entidade","logradouro");
		return model;
	}
	@GetMapping(value="logradouro/delete/{id}")
	public ModelAndView excluirLogradouro(@PathVariable("id") Long id) {
		Logradouro logradouro =logradouroRepository.getOne(id);
		while(logradouroRepository.existsById(id)) {
			logradouroRepository.delete(logradouro);
		}
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo","deletado");
		model.addObject("entidade","logradouro");
		return model;
	}
	
}
