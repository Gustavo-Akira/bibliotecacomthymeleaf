package br.com.biblioteca.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.biblioteca.models.Role;
import br.com.biblioteca.models.Usuarios;
import br.com.biblioteca.repositories.RolesRepository;
import br.com.biblioteca.repositories.UsuariosRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WebConfigSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private RolesRepository rolesRepository;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        userRole = createRole("ROLE_USER");
        adminRole = createRole("ROLE_ADMIN");
    }

    @Test
    void shouldLoginWithValidCredentials() throws Exception {
        Usuarios usuario = createUser(
                "user.login@teste.com",
                "123456",
                userRole
        );

        mockMvc.perform(
                        post("/login")
                                .param("username", usuario.getEmail())
                                .param("password", "123456")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/entrar"));
    }

    @Test
    void shouldRejectLoginWithInvalidPassword() throws Exception {
        Usuarios usuario = createUser(
                "user.invalid.password@teste.com",
                "123456",
                userRole
        );

        mockMvc.perform(
                        post("/login")
                                .param("username", usuario.getEmail())
                                .param("password", "wrong-password")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    void shouldBlockRegularUserFromDashboard() throws Exception {
        mockMvc.perform(
                        get("/dashboard/")
                                .with(user("user")
                                        .roles("USER"))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminToAccessDashboard() throws Exception {
        mockMvc.perform(
                        get("/dashboard/")
                                .with(user("admin")
                                        .roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/index"));
    }

    private Usuarios createUser(
            String email,
            String password,
            Role role) {

        Usuarios usuario = new Usuarios();

        usuario.setNome("Teste");
        usuario.setSobrenome("Usuario");
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(password));
        usuario.setRoles(java.util.List.of(role));

        return usuarioRepository.save(usuario);
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setNome(name);

        return rolesRepository.save(role);
    }
}