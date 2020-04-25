package br.com.biblioteca.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Telefone;
@Repository
@Transactional
public interface TelefoneRepository extends CrudRepository<Telefone, Long>{
	@Query(value="SELECT * from telefone INNER JOIN editora on telefone.editora_id = editora.id where editora.id=?1",nativeQuery = true)
	public List<Telefone> getTelefoneByEditora(Long id);
}
