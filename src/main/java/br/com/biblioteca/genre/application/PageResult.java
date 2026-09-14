package br.com.biblioteca.genre.application;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        Integer size,
        Long totalElements,
        Integer totalPages
) {
}
