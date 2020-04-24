package br.com.biblioteca.repositories;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Cartoes;
@Repository
@Transactional
public interface CartoesRepository extends CrudRepository<Cartoes, Long>{

}
