package br.com.biblioteca.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Compras;
@Repository
@Transactional
public interface ComprasRepository extends JpaRepository<Compras, Long>{

}
