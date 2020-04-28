package br.com.biblioteca.controllers;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.biblioteca.models.Editora;
import br.com.biblioteca.models.Foto;
import br.com.biblioteca.models.Genero;
import br.com.biblioteca.models.Livros;
import br.com.biblioteca.models.Logradouro;
import br.com.biblioteca.models.Telefone;
import br.com.biblioteca.models.Usuarios;
import br.com.biblioteca.repositories.ComprasRepository;
import br.com.biblioteca.repositories.EditorasRepository;
import br.com.biblioteca.repositories.FotoRepository;
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

	@Autowired
	private FotoRepository fotoRepository;

	@GetMapping(value = "/")
	public ModelAndView dashboard() {
		ModelAndView model = new ModelAndView("dashboard/index");
		return model;
	}

	@GetMapping(value = "/usuarios")
	public ModelAndView usuarios() {
		ModelAndView model = new ModelAndView("dashboard/usuarios/index");
		Page<Usuarios> usuarios = usuarioRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		usuarios.forEach(e -> {
			e.setLogradouros(logradouroRepository.getLogradouroBydIdUsuario(e.getId()));
		});
		model.addObject("usuarios", usuarios);
		return model;
	}

	@GetMapping(value = "/livros")
	public ModelAndView livros() {
		Page<Livros> livros = livrosRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		ModelAndView model = new ModelAndView("dashboard/livros/index");
		model.addObject("livros", livros);
		return model;
	}

	@GetMapping(value = "/livros/novo")
	public ModelAndView novolivro() {
		ModelAndView model = new ModelAndView("dashboard/livros/novo");
		model.addObject("editoras", editorasRepository.findAll());
		model.addObject("generos", generoRepository.findAll());
		return model;
	}
	@GetMapping(value = "/livros/editar/{id}")
	public ModelAndView editarlivro(@PathVariable("id")Long id) {
		ModelAndView model = new ModelAndView("dashboard/livros/editar");
		model.addObject("livro",livrosRepository.findById(id).get());
		model.addObject("editoras", editorasRepository.findAll());
		model.addObject("generos", generoRepository.findAll());
		return model;
	}
	
	@PostMapping(value = "/livros/editar/salvar/{id}")
	public ModelAndView editlivro(Livros livro,@PathVariable("id")Long id, final MultipartFile file) throws IOException {
		Livros antigo = livrosRepository.findById(id).get();
		if(file.getSize() >0) {
			List<Foto> fotos = new ArrayList<Foto>();
			try {
				Foto foto = new Foto();
				foto.setNome(file.getOriginalFilename());
				foto.setTipo(file.getContentType());
				String caminho = "/imagens/" + file.getOriginalFilename();
				try {
					FileOutputStream fos = new FileOutputStream(
							"C:/Users/akira/Desktop/ws-spring/bibilotecathymeleaf/src/main/resources/static/imagens/"
									+ file.getOriginalFilename());
					fos.write(file.getBytes());
					FileDescriptor fd = fos.getFD();
					fos.flush();
					fd.sync();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				foto.setCaminho(caminho);
				fotos.add(foto);
				foto.setLivro(livro);
			} catch (Exception e) {
				e.printStackTrace();
			}
			livro.setFotos(fotos);
		}else {
			livro.setFotos(antigo.getFotos());
		}
		try {
			livrosRepository.save(livro);
		}catch(Exception e) {
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "editar");
		model.addObject("entidade", "livro");
		return model;
	}
	
	@PostMapping(value = "/livros/salvar")
	public ModelAndView salvarlivro(Livros livro, final MultipartFile file) {
		List<Foto> fotos = new ArrayList<Foto>();
		try {
			Foto foto = new Foto();
			foto.setNome(file.getOriginalFilename());
			foto.setTipo(file.getContentType());
			String caminho = "/imagens/" + file.getOriginalFilename();
			try {
				FileOutputStream fos = new FileOutputStream(
						"C:/Users/akira/Desktop/ws-spring/bibilotecathymeleaf/src/main/resources/static/imagens/"
								+ file.getOriginalFilename());
				fos.write(file.getBytes());
				FileDescriptor fd = fos.getFD();
				fos.flush();
				fd.sync();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			foto.setCaminho(caminho);
			fotos.add(foto);
			foto.setLivro(livro);
		} catch (Exception e) {
			e.printStackTrace();
		}
		livro.setFotos(fotos);
		livrosRepository.save(livro);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "inserir");
		model.addObject("entidade", "telefone");
		return model;
	}

	@GetMapping(value = "/livros/deletar/{id}")
	public ModelAndView deletar(@PathVariable("id") Long id) {
		Livros livro = livrosRepository.findById(id).get();
		livro.getFotos().forEach(e -> {
			fotoRepository.deleteById(e.getId());
		});
		livrosRepository.delete(livro);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "inserir");
		model.addObject("entidade", "telefone");
		return model;
	}

	@GetMapping(value = "**/editoras")
	public ModelAndView editoras() {
		ModelAndView model = new ModelAndView("dashboard/editora/index");
		Page<Editora> editoras = editorasRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		model.addObject("editoras", editoras);
		return model;
	}

	@GetMapping(value = "**/editoras/novo")
	public ModelAndView novaeditora() {
		ModelAndView model = new ModelAndView("dashboard/editora/novo");
		return model;
	}

	@PostMapping(value = "**/editoras/salvar")
	public ModelAndView salvareditora(Editora editora, Logradouro logradouro) {
		editora = editorasRepository.save(editora);
		logradouro.setEditora(editora);
		logradouro = logradouroRepository.save(logradouro);
		ModelAndView model = new ModelAndView("dashboard/editora/index");
		model.addObject("editoras", editorasRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		return model;
	}

	@GetMapping(value = "/editoras/view/{id}")
	public ModelAndView editorasid(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("dashboard/editora/unique");
		Optional<Editora> editora = editorasRepository.findById(id);
		model.addObject("editora", editora.get());
		return model;
	}
	
	@GetMapping(value="/editoras/deletar/{id}")
	public ModelAndView deletareditora(@PathVariable("id")Long id) {
		Editora editora = editorasRepository.findById(id).get();
		editora.getLogradouro().forEach(e->{logradouroRepository.deleteById(e.getId());});
		editorasRepository.deleteById(id);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "deletada");
		model.addObject("entidade", "editora");
		return model;
	}
	
	@GetMapping(value = "/editoras/editar/{id}")
	public ModelAndView editareditoras(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("dashboard/editora/editar");
		Optional<Editora> editora = editorasRepository.findById(id);
		model.addObject("editora", editora.get());
		return model;
	}
	
	@PostMapping(value = "/editoras/edit")
	public ModelAndView editeditoras(Editora editora) {
		Editora antigo = editorasRepository.findById(editora.getId()).get();
		antigo.setEmail(editora.getEmail());
		antigo.setNome(editora.getNome());
		editorasRepository.save(antigo);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "editada");
		model.addObject("entidade", "editora");
		return model;
	}
	@GetMapping(value = "/generos")
	public ModelAndView generos() {
		ModelAndView model = new ModelAndView("dashboard/generos/index");
		Page<Genero> generos = generoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
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
		Page<Genero> generos = generoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		model.addObject("generos", generos);
		return model;
	}

	@GetMapping(value = "**/generos/deletar/{id}")
	public ModelAndView generoDeletar(@PathVariable("id") Long id) {
		generoRepository.deleteById(id);
		ModelAndView model = new ModelAndView("dashboard/generos/index");
		Page<Genero> generos = generoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		model.addObject("generos", generos);
		return model;
	}

	@GetMapping(value = "**/generos/editar/{id}")
	public ModelAndView generoEditar(@PathVariable("id") Long id) {
		Optional<Genero> genero = generoRepository.findById(id);
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

	@PostMapping(value = "/telefone/novo/editora/{id}")
	public ModelAndView novotelefone(Telefone telefone, @PathVariable("id") Long id) {
		Editora editora = editorasRepository.findById(id).get();
		telefone.setEditora(editora);
		telefoneRepository.save(telefone);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "inserir");
		model.addObject("entidade", "editora");
		return model;
	}

	@PostMapping(value = "/telefone/editar/")
	public ModelAndView editarTelefone(Telefone telefone) {
		telefoneRepository.save(telefone);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "inserir");
		model.addObject("entidade", "telefone");
		return model;
	}

	@GetMapping(value = "/telefone/editar/{id}")
	public ModelAndView editarT(@PathVariable("id") Long id) {
		Telefone telefone = telefoneRepository.findById(id).get();
		ModelAndView model = new ModelAndView("dashboard/telefone/editar");
		model.addObject("telefone", telefone);
		return model;
	}

	@GetMapping(value = "/telefone/deletar/{editora}/{id}")
	public ModelAndView deletartelefone(@PathVariable("id") Long id, @PathVariable("editora") Long editora) {
		Editora editoras = editorasRepository.findById(editora).get();
		Telefone telefone = telefoneRepository.findById(id).get();
		editoras.getTelefones().remove(telefone);
		editorasRepository.save(editoras);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "deletado");
		model.addObject("entidade", "telefone");
		return model;
	}

	@GetMapping(value = "logradouro/novo/editora/{id}")
	public ModelAndView novologradouro(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("dashboard/logradouro/novo");
		model.addObject("id", id);
		return model;
	}

	@PostMapping(value = "logradouro/salvar/editora/{id}")
	public ModelAndView novologradouro(Logradouro logradouro, @PathVariable("id") Long id) {
		Editora editora = editorasRepository.findById(id).get();
		logradouro.setEditora(editora);
		logradouroRepository.save(logradouro);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "inserido");
		model.addObject("entidade", "logradouro");
		return model;
	}

	@GetMapping(value = "logradouro/delete/{editora}/{id}")
	public ModelAndView excluirLogradouro(@PathVariable("id") Long id, @PathVariable("editora") Long editora) {
		Logradouro logradouro = logradouroRepository.findById(id).get();
		Editora editoras = editorasRepository.findById(editora).get();
		editoras.getLogradouro().remove(logradouro);
		editorasRepository.save(editoras);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "deletado");
		model.addObject("entidade", "logradouro");
		return model;
	}

	@GetMapping(value = "logradouro/editar/{id}")
	public ModelAndView editarlogradouroNew(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("dashboard/logradouro/editar");
		model.addObject("logradouro", logradouroRepository.findById(id).get());
		return model;
	}

	@PostMapping(value = "logradouro/editar/{id}")
	public ModelAndView editarlogradouro(Logradouro logradouro, @PathVariable("id") Long id) {
		logradouroRepository.save(logradouro);
		ModelAndView model = new ModelAndView("dashboard/sucesso");
		model.addObject("tipo", "editado");
		model.addObject("entidade", "logradouro");
		return model;
	}

}
