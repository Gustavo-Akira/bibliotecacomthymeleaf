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
import br.com.biblioteca.models.Logradouro;
import br.com.biblioteca.repositories.EditorasRepository;
import br.com.biblioteca.repositories.LogradouroRepository;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DashboardPublisherAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EditorasRepository editorasRepository;

    @Autowired
    private LogradouroRepository logradouroRepository;

    @Test
    void shouldCreatePublisherAddress() throws Exception {
        Editora editora = createEditora();

        long addressesBefore = logradouroRepository.count();

        mockMvc.perform(
                        post("/dashboard/logradouro/salvar/editora/{id}", editora.getId())
                                .param("rua", "Rua Teste")
                                .param("numero", "100")
                                .param("cidade", "São Paulo")
                                .param("CEP", "01000000")
                                .param("complemento", "Apto 10")
                                .param("ibge", "3550308")
                                .param("uf", "SP")
                                .param("bairro", "Centro")
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        assertThat(logradouroRepository.count())
                .isEqualTo(addressesBefore + 1);

        Logradouro logradouro = logradouroRepository.findAll()
                .stream()
                .filter(item -> "Rua Teste".equals(item.getRua()))
                .findFirst()
                .orElseThrow();

        assertThat(logradouro.getNumero()).isEqualTo(100);
        assertThat(logradouro.getCidade()).isEqualTo("São Paulo");
        assertThat(logradouro.getCEP()).isEqualTo("01000000");
        assertThat(logradouro.getEditora()).isNotNull();
        assertThat(logradouro.getEditora().getId())
                .isEqualTo(editora.getId());
    }

    @Test
    void shouldEditPublisherAddress() throws Exception {
        Editora editora = createEditora();
        Logradouro logradouro = createLogradouro(editora);

        mockMvc.perform(
                        post("/dashboard/logradouro/editar/{id}", logradouro.getId())
                                .param("id", logradouro.getId().toString())
                                .param("rua", "Rua Atualizada")
                                .param("numero", "200")
                                .param("cidade", "São Paulo")
                                .param("CEP", "02000000")
                                .param("complemento", "Sala 20")
                                .param("ibge", "3550308")
                                .param("uf", "SP")
                                .param("bairro", "Novo Bairro")
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        Logradouro updated = logradouroRepository
                .findById(logradouro.getId())
                .orElseThrow();

        assertThat(updated.getRua())
                .isEqualTo("Rua Atualizada");

        assertThat(updated.getNumero())
                .isEqualTo(200);

        assertThat(updated.getCEP())
                .isEqualTo("02000000");
    }


    private Editora createEditora() {
        Editora editora = new Editora();
        editora.setNome("Editora Teste");
        editora.setEmail("editora@teste.com");

        return editorasRepository.save(editora);
    }

    private Logradouro createLogradouro(Editora editora) {
        Logradouro logradouro = new Logradouro();

        logradouro.setRua("Rua Original");
        logradouro.setNumero(100);
        logradouro.setCidade("São Paulo");
        logradouro.setCEP("01000000");
        logradouro.setComplemento("Apto 10");
        logradouro.setIbge("3550308");
        logradouro.setUf("SP");
        logradouro.setBairro("Centro");
        logradouro.setEditora(editora);

        return logradouroRepository.save(logradouro);
    }
}