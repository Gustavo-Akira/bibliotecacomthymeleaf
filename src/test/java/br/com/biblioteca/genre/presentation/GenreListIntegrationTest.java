package br.com.biblioteca.genre.presentation;

import br.com.biblioteca.genre.application.ListGenreUseCase;
import br.com.biblioteca.genre.application.PageResult;
import br.com.biblioteca.genre.application.query.ListGenreQuery;
import br.com.biblioteca.models.Genero;
import br.com.biblioteca.repositories.GeneroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GenreListIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private ListGenreUseCase listGenreUseCase;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        generoRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRenderOnlyTheFirstFiveGenresInAlphabeticalOrder() throws Exception {
        saveGenres("Terror", "Romance", "Poesia", "Fantasia", "Drama", "Aventura");

        var response = mockMvc.perform(get("/dashboard/generos"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/generos/index"))
                .andReturn();

        String html = response.getResponse().getContentAsString();
        assertThat(html).containsSubsequence("<td>Aventura</td>", "<td>Drama</td>",
                "<td>Fantasia</td>", "<td>Poesia</td>", "<td>Romance</td>");
        assertThat(html).doesNotContain("<td>Terror</td>");

        var modelAndView = response.getModelAndView();
        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getModel().get("generos")).isInstanceOf(PageResult.class);
        PageResult<?> page = (PageResult<?>) modelAndView.getModel().get("generos");
        assertThat(page.size()).isEqualTo(5);
        assertThat(page.totalElements()).isEqualTo(6L);
        assertThat(page.totalPages()).isEqualTo(2);
        assertThat(page.content()).extracting("nome")
                .containsExactly("Aventura", "Drama", "Fantasia", "Poesia", "Romance");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRenderAnEmptyGenreList() throws Exception {
        var response = mockMvc.perform(get("/dashboard/generos"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/generos/index"))
                .andReturn();

        assertThat(response.getResponse().getContentAsString())
                .containsPattern("(?s)<tbody>\\s*</tbody>");
    }

    @Test
    void shouldListTheRequestedPageThroughTheUseCase() {
        saveGenres("Terror", "Romance", "Poesia", "Fantasia", "Drama", "Aventura", "Suspense");

        PageResult<Genero> page = listGenreUseCase.execute(new ListGenreQuery(3, 2));

        assertThat(page.content()).extracting(Genero::getNome).containsExactly("Terror");
        assertThat(page.size()).isEqualTo(3);
        assertThat(page.totalElements()).isEqualTo(7L);
        assertThat(page.totalPages()).isEqualTo(3);
    }

    @Test
    void shouldPreserveTotalsWhenTheRequestedPageIsBeyondTheLastPage() {
        saveGenres("Terror", "Aventura");

        PageResult<Genero> page = listGenreUseCase.execute(new ListGenreQuery(3, 2));

        assertThat(page.content()).isEmpty();
        assertThat(page.size()).isEqualTo(3);
        assertThat(page.totalElements()).isEqualTo(2L);
        assertThat(page.totalPages()).isEqualTo(1);
    }

    private void saveGenres(String... names) {
        generoRepository.saveAll(Arrays.stream(names).map(name -> {
            Genero genre = new Genero();
            genre.setNome(name);
            return genre;
        }).toList());
    }
}
