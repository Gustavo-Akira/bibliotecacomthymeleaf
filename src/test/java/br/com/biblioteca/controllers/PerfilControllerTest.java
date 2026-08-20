package br.com.biblioteca.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.biblioteca.models.Logradouro;
import br.com.biblioteca.models.Role;
import br.com.biblioteca.models.Usuarios;
import br.com.biblioteca.repositories.LogradouroRepository;
import br.com.biblioteca.repositories.RolesRepository;
import br.com.biblioteca.repositories.UsuariosRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private LogradouroRepository logradouroRepository;

    @Autowired
    private RolesRepository rolesRepository;

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
        Role role = new Role();
        role.setNome(roleName);
        role = rolesRepository.save(role);

        Usuarios usuario = new Usuarios();
        usuario.setNome("Usuario");
        usuario.setSobrenome("Perfil");
        usuario.setEmail(email);
        usuario.setSenha("password");
        usuario.setRoles(List.of(role));

        return usuarioRepository.save(usuario);
    }

    private void givenRoleWithId(Long id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setNome(name);

        rolesRepository.save(role);
    }
}