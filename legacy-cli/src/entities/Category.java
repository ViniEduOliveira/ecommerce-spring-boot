package entities;

import java.util.ArrayList;
import java.util.List;

import entities.enuns.CategoryType;

// Classe que representa a categoria de um produto
public class Category extends ModelBase{
	// Atributos
	private CategoryType name;
	private String descricao;
	
	private List<Product> produto = new ArrayList<>();
	
	// Construtor vazio
	public Category() {
		super();
	}
	
	// Construtor parametrizado
	public Category(CategoryType name, String descricao) {
		this.setName(name);
		this.setDescricao(descricao);
	}

	// Getters e Setters
	public CategoryType getName() {
		return name;
	}

	public void setName(CategoryType name) {
		this.name = name;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public List<Product> getProduto() {
		return produto;
	}

	public void addProduto(Product produto) {
		this.produto.add(produto);
	}
	
	public void removeProduto(Product produto) {
		this.produto.remove(produto);
	}
}