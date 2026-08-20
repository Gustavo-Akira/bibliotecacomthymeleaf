package br.com.biblioteca.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.biblioteca.models.Editora;
import br.com.biblioteca.models.Telefone;
import br.com.biblioteca.repositories.EditorasRepository;
import br.com.biblioteca.repositories.TelefoneRepository;

import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DashboardPublisherPhoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EditorasRepository editorasRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Test
    void shouldCreatePublisherPhone() throws Exception {
        Editora editora = createEditora();

        long phonesBefore = telefoneRepository.count();

        mockMvc.perform(
                        post(
                                "/dashboard/telefone/novo/editora/{id}",
                                editora.getId()
                        )
                                .param("numero", "11999999999")
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        assertThat(telefoneRepository.count())
                .isEqualTo(phonesBefore + 1);

        Telefone telefone = telefoneRepository.getTelefoneByEditora(editora.getId())
                .stream()
                .filter(item -> "11999999999".equals(item.getNumero()))
                .findFirst()
                .orElseThrow();

        assertThat(telefone.getEditora())
                .isNotNull();

        assertThat(telefone.getEditora().getId())
                .isEqualTo(editora.getId());
    }

    @Test
    void shouldOpenPublisherPhoneEditPage() throws Exception {
        Editora editora = createEditora();
        Telefone telefone = createTelefone(editora);

        mockMvc.perform(
                        get(
                                "/dashboard/telefone/editar/{id}",
                                telefone.getId()
                        )
                                .with(user("admin").roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/telefone/editar"));
    }

    @Test
    void shouldEditPublisherPhone() throws Exception {
        Editora editora = createEditora();
        Telefone telefone = createTelefone(editora);

        mockMvc.perform(
                        post("/dashboard/telefone/editar/")
                                .param("id", telefone.getId().toString())
                                .param("numero", "11888888888")
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        Telefone updated = telefoneRepository
                .findById(telefone.getId())
                .orElseThrow();

        assertThat(updated.getNumero())
                .isEqualTo("11888888888");
    }


    private Editora createEditora() {
        Editora editora = new Editora();
        editora.setNome("Editora Teste");
        editora.setEmail("editora@teste.com");
        editora.setTelefones(new ArrayList<>());
        return editorasRepository.save(editora);
    }

    private Telefone createTelefone(Editora editora) {
        Telefone telefone = new Telefone();
        telefone.setNumero("11999999999");
        telefone.setEditora(editora);

        return telefoneRepository.save(telefone);
    }
}