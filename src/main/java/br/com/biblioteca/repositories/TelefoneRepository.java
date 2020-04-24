package br.com.biblioteca.repositories;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Telefone;
@Repository
@Transactional
public interface TelefoneRepository extends CrudRepository<Telefone, Long>{

}
