package br.com.biblioteca.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Livros;
@Repository
@Transactional
public interface LivrosRepository extends JpaRepository<Livros, Long>{
	@Query(value="SELECT * FROM livros where editora_id = ?1",nativeQuery=true)
	public List<Livros> getLivrosByEditora(Long id);
}
