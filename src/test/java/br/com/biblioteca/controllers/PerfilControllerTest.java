package br.com.biblioteca.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import br.com.biblioteca.models.Role;
import br.com.biblioteca.models.Usuarios;
import br.com.biblioteca.repositories.LogradouroRepository;
import br.com.biblioteca.repositories.RolesRepository;
import br.com.biblioteca.repositories.UsuariosRepository;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PerfilControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private LogradouroRepository logradouroRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void shouldDisplayAuthenticatedUserProfile() throws Exception {
        Usuarios usuario = createUser(
                "profile@teste.com",
                "ROLE_USER"
        );

        mockMvc.perform(
                        get("/perfil/")
                                .with(user(usuario.getEmail()).roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("perfil/index"));
    }

    @Test
    void shouldDisplayProfileRegistrationPage() throws Exception {
        mockMvc.perform(
                        get("/perfil/cadastrar")
                                .with(user("user@teste.com").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("perfil/novo"));
    }


    private Usuarios createUser(String email, String roleName) {
        Optional<Role> oldRole = rolesRepository.findAll().stream().filter(x->x.getNome().equals(roleName)).findAny();
        Role role = null;
        if(oldRole.isEmpty()){
            role = new Role();
            role.setNome(roleName);
            role = rolesRepository.save(role);
        }else{
            role = oldRole.get();
        }

        Usuarios usuario = new Usuarios();
        usuario.setNome("Usuario");
        usuario.setSobrenome("Perfil");
        usuario.setEmail(email);
        usuario.setSenha("password");
        usuario.setRoles(List.of(role));

        return usuarioRepository.save(usuario);
    }
}