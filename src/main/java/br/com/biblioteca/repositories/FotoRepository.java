package br.com.biblioteca.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.biblioteca.models.Foto;

public interface FotoRepository  extends CrudRepository<Foto, Long>{

}
