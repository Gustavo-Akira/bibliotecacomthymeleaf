package br.com.biblioteca.genre.application;

import br.com.biblioteca.genre.application.port.GenreRepositoryPort;
import br.com.biblioteca.genre.application.query.ListGenreQuery;
import br.com.biblioteca.models.Genero;
import org.springframework.stereotype.Service;


@Service
public class ListGenreUseCase {
    private final GenreRepositoryPort repository;
    public ListGenreUseCase(GenreRepositoryPort repository) {
        this.repository = repository;
    }

    public PageResult<Genero>  execute(ListGenreQuery query){
        return repository.listGenre(query.size(), query.page());
    }
}
