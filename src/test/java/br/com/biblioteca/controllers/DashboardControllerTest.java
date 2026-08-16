package br.com.biblioteca.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import br.com.biblioteca.models.Editora;
import br.com.biblioteca.models.Logradouro;
import br.com.biblioteca.repositories.EditorasRepository;
import br.com.biblioteca.repositories.LogradouroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.biblioteca.models.Livros;
import br.com.biblioteca.repositories.LivrosRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivrosRepository livrosRepository;

    @Autowired
    private EditorasRepository  editorasRepository;

    @Autowired
    private LogradouroRepository logradouroRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateBookWithoutGenre() throws Exception {
        long booksBefore = livrosRepository.count();

        mockMvc.perform(
                        multipart("/dashboard/livros/salvar")
                                .param("nome", "Clean Code")
                                .param("quantidade", "10")
                                .param("preco", "89.90")
                                .param("descricao", "A book about software craftsmanship")
                                .param("edicao", "1")
                                .param("acabamento", "Capa dura")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        assertThat(livrosRepository.count()).isEqualTo(booksBefore + 1);

        Livros livro = livrosRepository.findAll()
                .stream()
                .filter(item -> "Clean Code".equals(item.getNome()))
                .findFirst()
                .orElseThrow();

        assertThat(livro.getNome()).isEqualTo("Clean Code");
        assertThat(livro.getQuantidade()).isEqualTo(BigInteger.TEN);
        assertThat(livro.getPreco()).isEqualByComparingTo(new BigDecimal("89.90"));

        // Characterization:
        // the application currently allows a book without a genre.
        assertThat(livro.getGeneros()).isNullOrEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldEditBook() throws Exception {
        Livros livro = new Livros();
        livro.setNome("Old Book Name");
        livro.setQuantidade(BigInteger.TEN);
        livro.setPreco(new BigDecimal("50.00"));

        livro = livrosRepository.save(livro);

        Long id = livro.getId();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "",
                "application/octet-stream",
                new byte[0]
        );

        mockMvc.perform(
                        multipart("/dashboard/livros/editar/salvar/{id}", id)
                                .file(file)
                                .param("nome", "Updated Book Name")
                                .param("quantidade", "20")
                                .param("preco", "75.00")
                                .param("descricao", "Updated description")
                                .param("edicao", "2")
                                .param("acabamento", "Brochura")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        Livros updated = livrosRepository.findById(id)
                .orElseThrow();

        assertThat(updated.getNome()).isEqualTo("Updated Book Name");
        assertThat(updated.getQuantidade())
                .isEqualTo(BigInteger.valueOf(20));
        assertThat(updated.getPreco())
                .isEqualByComparingTo(new BigDecimal("75.00"));
        assertThat(updated.getDescricao())
                .isEqualTo("Updated description");
        assertThat(updated.getEdicao()).isEqualTo("2");
        assertThat(updated.getAcabamento()).isEqualTo("Brochura");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteBook() throws Exception {
        Livros livro = new Livros();
        livro.setNome("Book To Delete");
        livro.setQuantidade(BigInteger.ONE);
        livro.setPreco(new BigDecimal("30.00"));
        livro = livrosRepository.save(livro);

        Long id = livro.getId();

        mockMvc.perform(
                        get("/dashboard/livros/deletar/{id}", id)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        assertThat(livrosRepository.findById(id)).isEmpty();
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreatePublisher() throws Exception {
        long publishersBefore = editorasRepository.count();

        mockMvc.perform(
                        post("/dashboard/editoras/salvar")
                                .param("nome", "Editora Teste")
                                .param("email", "editora@teste.com")
                                .param("logradouro", "")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/editora/index"));

        assertThat(editorasRepository.count())
                .isEqualTo(publishersBefore + 1);

        Editora editora = editorasRepository.findAll()
                .stream()
                .filter(item -> "Editora Teste".equals(item.getNome()))
                .findFirst()
                .orElseThrow();

        assertThat(editora.getNome()).isEqualTo("Editora Teste");
        assertThat(editora.getEmail()).isEqualTo("editora@teste.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldEditPublisher() throws Exception {
        Editora editora = new Editora();
        editora.setNome("Editora Original");
        editora.setEmail("original@teste.com");

        editora = editorasRepository.save(editora);

        Long id = editora.getId();

        mockMvc.perform(
                        post("/dashboard/editoras/edit")
                                .param("id", id.toString())
                                .param("nome", "Editora Atualizada")
                                .param("email", "atualizada@teste.com")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        Editora updated = editorasRepository.findById(id)
                .orElseThrow();

        assertThat(updated.getNome())
                .isEqualTo("Editora Atualizada");

        assertThat(updated.getEmail())
                .isEqualTo("atualizada@teste.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFailToDeletePublisherWithoutAddress() throws Exception {
        Editora editora = new Editora();
        editora.setNome("Editora Sem Logradouro");
        editora.setEmail("semendereco@teste.com");

        editora = editorasRepository.save(editora);

        Long id = editora.getId();

        assertThatThrownBy(() ->
                mockMvc.perform(
                        get("/dashboard/editoras/deletar/{id}", id)
                ).andReturn()
        )
                .hasCauseInstanceOf(NullPointerException.class);

        assertThat(editorasRepository.findById(id))
                .isPresent();
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeletePublisherWithAddress() throws Exception {
        Editora editora = new Editora();
        editora.setNome("Editora Para Deletar");
        editora.setEmail("deletar@teste.com");
        Logradouro logradouro = new Logradouro();
        editora = editorasRepository.save(editora);
        logradouro.setEditora(editora);
        logradouro.setBairro("Bairro");
        logradouro.setCEP("445151");
        logradouro.setCidade("Cidade");
        logradouro.setIbge("41414");
        logradouro.setNumero(45);
        logradouro.setRua("Rua");
        logradouro.setUf("UF");
        logradouro = logradouroRepository.save(logradouro);
        editora.setLogradouro(List.of(logradouro));
        Long editoraId = editora.getId();
        Long logradouroId = logradouro.getId();

        mockMvc.perform(
                        get("/dashboard/editoras/deletar/{id}", editoraId)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/sucesso"));

        assertThat(editorasRepository.findById(editoraId))
                .isEmpty();

        assertThat(logradouroRepository.findById(logradouroId))
                .isEmpty();
    }
}