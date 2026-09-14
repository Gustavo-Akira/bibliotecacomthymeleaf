package br.com.biblioteca.genre.infraestructure.persistence;

import br.com.biblioteca.genre.application.PageResult;
import br.com.biblioteca.models.Genero;
import br.com.biblioteca.repositories.GeneroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreRepositoryPortImplTest {

    @Mock
    private GeneroRepository generoRepository;

    private GenreRepositoryPortImpl repository;

    @BeforeEach
    void setUp() {
        repository = new GenreRepositoryPortImpl(generoRepository);
    }

    @Test
    void shouldRequestTheSelectedPageSortedByName() {
        when(generoRepository.findAll(any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            assertThat(pageable.getPageNumber()).isEqualTo(2);
            assertThat(pageable.getPageSize()).isEqualTo(3);
            assertThat(pageable.getSort()).isEqualTo(Sort.by("nome"));
            return new PageImpl<Genero>(List.of(), pageable, 0);
        });

        repository.listGenre(3, 2);
    }

    @Test
    void shouldPreserveContentAndTotalsForAPartialLastPage() {
        Genero genre = new Genero();
        genre.setId(7L);
        genre.setNome("Suspense");
        when(generoRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(genre), PageRequest.of(2, 3), 7));

        PageResult<Genero> result = repository.listGenre(3, 2);

        assertThat(result.content()).extracting(Genero::getNome).containsExactly("Suspense");
        assertThat(result.content()).extracting(Genero::getId).containsExactly(7L);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.totalElements()).isEqualTo(7L);
        assertThat(result.totalPages()).isEqualTo(3);
    }

    @Test
    void shouldMapAnEmptyPage() {
        when(generoRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<Genero>(List.of(), PageRequest.of(0, 5), 0));

        PageResult<Genero> result = repository.listGenre(5, 0);

        assertThat(result.content()).isEmpty();
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.totalElements()).isZero();
        assertThat(result.totalPages()).isZero();
    }
}
