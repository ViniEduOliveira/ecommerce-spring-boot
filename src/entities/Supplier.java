package entities;

import java.util.ArrayList;
import java.util.List;

// Classe que representa um fornecedor
public class Supplier extends ModelBase{
	// Atributos
	private String name;
	private String cnpj;
	private String telefone;
	private String email;
	
	// Lista que contém os produtos do fornecedor	
	private List<Product> produto = new ArrayList<>();
	
	// Construtor vazio
	public Supplier() {
		super();
	}
	
	// Construtor parametrizado
	public Supplier(String name, String cnpj, String telefone, String email) {
		this.setName(name);
		this.setCnpj(cnpj);
		this.setTelefone(telefone);
		this.setEmail(email);
	}
	
	// Métodos para adicionar e remover produtos do fornecedor
	public void addProduto(Product produto) {
		this.produto.add(produto);
	}
	
	public void removeProduto(Product produto) {
		this.produto.remove(produto);
	}

	// Getters e Setters
	public String getName() {
		return name;
	}

	public void  setName(String name) {
		this.name = name;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Product> getProduto() {
		return produto;
	}
}