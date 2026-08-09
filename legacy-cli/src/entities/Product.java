package entities;

import java.util.ArrayList;
import java.util.List;

// Classe que representa um produto
public class Product extends ModelBase{
	// Atributos do produto
	private String name;
	private String descricao;
	private double preco;
	private int qtdEstoque;
	private String sku;
	
	// Listas que contem fornecedores e categorias respectivamente
	private List<Supplier> fornecedores = new ArrayList<>();
	private List<Category> categorias = new ArrayList<>();
	
	// Construtor vazio
	public Product() {
		super();
	}
	
	// Construtor parametrizado
	public Product(String name, String descricao, double preco, int qtdEstoque) {
		this.setName(name);
		this.setDescricao(descricao);
		this.setPreco(preco);
		this.qtdEstoque = qtdEstoque < 0 ? 0 : qtdEstoque;
	}
	
	// Métodos para adicionar e remover quantidade do estoque, fornecedores e categorias
	public void addQtdEstoque(int qtdEstoque) {
		this.qtdEstoque += qtdEstoque;
	}
	
	public void removeQtdEstoque(int qtdEstoque) {
		if (qtdEstoque <= this.qtdEstoque)
			this.qtdEstoque -= qtdEstoque;
	}

	public void addFornecedor(Supplier fornecedor) {
		if (fornecedor != null) {
	        this.fornecedores.add(fornecedor);
	    }
	}
	
	public void removeFornecedor(Supplier fornecedor) {
		if (fornecedor != null) {
	        this.fornecedores.remove(fornecedor);
	    }
	}

	public void addCategoria(Category categoria) {
		if (categoria != null) {
	        this.categorias.add(categoria);
	    }
		
	}
	
	public void removeCategoria(Category categoria) {
		if (categoria != null) {
	        this.categorias.remove(categoria);
	    }
	}	


	// Getters e Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public int getQtdEstoque() {
		return qtdEstoque;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public List<Supplier> getFornecedores() {
		return fornecedores;
	}

	public List<Category> getCategorias() {
		return categorias;
	}
}