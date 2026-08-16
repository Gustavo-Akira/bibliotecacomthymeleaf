package br.com.biblioteca.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.math.BigInteger;

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
}