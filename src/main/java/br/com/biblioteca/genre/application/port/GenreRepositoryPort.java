package br.com.biblioteca.genre.application.port;

import br.com.biblioteca.genre.application.PageResult;
import br.com.biblioteca.models.Genero;

public interface GenreRepositoryPort {
    PageResult<Genero> listGenre(int size, int page);
}
