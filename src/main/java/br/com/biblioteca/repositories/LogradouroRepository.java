package br.com.biblioteca.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Logradouro;
@Repository
@Transactional
public interface LogradouroRepository extends CrudRepository<Logradouro, Long>{
	@Query(value="select * from logradouro Inner Join usuarios on logradouro.usuario_id = usuarios.id where usuarios.id = ?1",nativeQuery=true)
	public List<Logradouro> getLogradouroBydIdUsuario(Long id);
	@Query(value="select * from logradouro Inner Join editora on logradouro.editora_id = editora.id where editora.id = ?1",nativeQuery=true)
	public List<Logradouro> getLogradouroBydIdEditora(Long id);
}
