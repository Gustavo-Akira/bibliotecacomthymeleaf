package br.com.biblioteca.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Livros {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nome;

	private BigInteger quantidade;

	private BigDecimal preco;

	@ManyToMany
	private List<Registros> registros;

	@ManyToOne
	private Editora editora;

	@ManyToMany
	@JoinTable(name = "livros_genero", joinColumns = @JoinColumn(name = "livro_id", referencedColumnName = "id", table = "livros"), inverseJoinColumns = @JoinColumn(name = "genero_id", referencedColumnName = "id", table = "genero"))
	private List<Genero> generos;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Foto> fotos;

	private String descricao;

	private String edicao;

	private String acabamento;

	public List<Foto> getFotos() {
		return fotos;
	}

	public void setFotos(List<Foto> fotos) {
		this.fotos = fotos;
	}

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

	public Editora getEditora() {
		return editora;
	}

	public void setEditora(Editora editora) {
		this.editora = editora;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public List<Registros> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Registros> registros) {
		this.registros = registros;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
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
		Livros other = (Livros) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public BigInteger getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getAcabamento() {
		return acabamento;
	}
	
	public void setAcabamento(String acabamento) {
		this.acabamento = acabamento;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	
	public String getEdicao() {
		return edicao;
	}

	@Override
	public String toString() {
		return "Livros [id=" + id + ", nome=" + nome + ", quantidade=" + quantidade + ", preco=" + preco
				+ ", registros=" + registros + ", editora=" + editora + ", generos=" + generos + ", fotos=" + fotos
				+ "]";
	}

}
