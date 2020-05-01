package br.com.biblioteca.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.biblioteca.models.Logradouro;
import br.com.biblioteca.models.Role;
import br.com.biblioteca.models.Usuarios;
import br.com.biblioteca.repositories.LogradouroRepository;
import br.com.biblioteca.repositories.RolesRepository;
import br.com.biblioteca.repositories.UsuariosRepository;

@Controller
@RequestMapping("/perfil")
public class PerfilController {
	
	@Autowired
	private UsuariosRepository usuarioRepository;
	
	@Autowired
	private LogradouroRepository logradouroRepository;
	
	@Autowired
	private RolesRepository roleRepository;
	
	@GetMapping("/")
	public ModelAndView index(){
		User principal =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuarios usuario = usuarioRepository.findUsuarioByLogin(principal.getUsername());
		ModelAndView model = new ModelAndView("perfil/index");
		model.addObject("usuario",usuario);
		return model;
	}
	@GetMapping("/cadastrar")
	public ModelAndView cadastrar() {
		ModelAndView model = new ModelAndView("perfil/novo");
		return model;
	}
	@PostMapping("/salvar")
	public String salvar(Usuarios usuario, Logradouro logradouro) {
		List<Role> roles = new ArrayList<Role>();
		roles.add(roleRepository.findById(3L).get());
		usuario.setRoles(roles);
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		try {
			usuarioRepository.save(usuario);
		}catch(Exception e){
			e.printStackTrace();
		}
		logradouro.setUsuario(usuario);
		logradouroRepository.save(logradouro);
		return "redirect:/home";
	}
	
}
