package br.com.biblioteca.models;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;

@Entity
public class Usuarios implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "Nome não pode ser nulo")
	@NotBlank(message = "Nome não pode ser vazia")
	private String nome;
	
	@NotNull(message = "Sobrenome não pode ser nulo")
	@NotBlank(message = "Sobrenome não pode ser vazia")
	private String sobrenome;
	
	@Column(unique = true)
	@NotNull(message = "E-mail não pode ser nulo")
	@NotBlank(message = "E-mail não pode ser vazia")
	@Email(message = "E-mail invalido")
	private String email;
	
	@NotNull(message = "Senha não pode ser nulo")
	@NotBlank(message = "Senha não pode ser vazia")
	private String senha;

	@Column(unique = true)
	@CPF(message = "erro cpf invalido")
	private String CPF;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Telefone> telefones;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cartoes> cartoes;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Logradouro> logradouros;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Registros> registros;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	@JoinTable(name = "usuarios_role",joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id",table = "usuarios"), inverseJoinColumns =  @JoinColumn(name="role_id", referencedColumnName = "id"))
	private List<Role> roles;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public List<Cartoes> getCartoes() {
		return cartoes;
	}

	public void setCartoes(List<Cartoes> cartoes) {
		this.cartoes = cartoes;
	}

	public List<Logradouro> getLogradouros() {
		return logradouros;
	}

	public void setLogradouros(List<Logradouro> logradouro) {
		this.logradouros = logradouro;
	}

	public List<Registros> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Registros> registros) {
		this.registros = registros;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuarios other = (Usuarios) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
