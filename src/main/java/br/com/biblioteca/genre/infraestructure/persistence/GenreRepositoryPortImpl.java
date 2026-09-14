package br.com.biblioteca.genre.infraestructure.persistence;

import br.com.biblioteca.genre.application.PageResult;
import br.com.biblioteca.genre.application.port.GenreRepositoryPort;
import br.com.biblioteca.models.Genero;
import br.com.biblioteca.repositories.GeneroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class GenreRepositoryPortImpl implements GenreRepositoryPort {

    private final GeneroRepository generoRepository;

    public GenreRepositoryPortImpl(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Override
    public PageResult<Genero> listGenre(int size, int page) {
        Page<Genero> pageGenre = generoRepository.findAll(PageRequest.of(page, size, Sort.by("nome")));
        return new PageResult<Genero>(
                pageGenre.getContent(),
                pageGenre.getSize(),
                pageGenre.getTotalElements(),
                pageGenre.getTotalPages()
        );
    }
}
