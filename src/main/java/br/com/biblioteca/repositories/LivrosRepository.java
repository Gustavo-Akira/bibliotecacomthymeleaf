package br.com.biblioteca.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Livros;
@Repository
@Transactional
public interface LivrosRepository extends JpaRepository<Livros, Long>{

}
