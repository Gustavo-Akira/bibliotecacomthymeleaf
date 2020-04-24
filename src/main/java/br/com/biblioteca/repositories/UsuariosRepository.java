package br.com.biblioteca.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.models.Usuarios;
@Repository
@Transactional
public interface UsuariosRepository extends JpaRepository<Usuarios, Long>{
	@Query("select u from Usuarios u where u.email = ?1")
	public Usuarios findUsuarioByLogin(String username);
}
