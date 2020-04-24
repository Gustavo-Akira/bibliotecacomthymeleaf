package br.com.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.models.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long>{

}
