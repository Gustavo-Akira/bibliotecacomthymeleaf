package br.com.biblioteca.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Registros;
@Repository
@Transactional
public interface RegistrosRepository extends JpaRepository<Registros, Long>{

}
