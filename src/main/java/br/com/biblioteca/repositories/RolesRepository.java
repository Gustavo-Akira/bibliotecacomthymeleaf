package br.com.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import br.com.biblioteca.models.Role;

public interface RolesRepository  extends JpaRepository<Role, Long> {

}
