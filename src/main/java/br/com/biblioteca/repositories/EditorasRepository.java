package br.com.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.models.Editora;

public interface EditorasRepository extends JpaRepository<Editora, Long>{

}
