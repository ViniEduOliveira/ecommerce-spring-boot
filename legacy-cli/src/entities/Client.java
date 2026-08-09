package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Classe que representa um cliente
public class Client extends ModelBase{
	// Atributos
	private String name;
	private String cpf;
	private String email;
	private String telefone;
	private LocalDate dataCadastro;
	
	private List<Order> pedidos = new ArrayList<>();
	
	// Construtor vazio
	public Client() {
		super();
	}

	// Construtor parametrizado
	public Client(String name, String cpf, String email, String telefone) {
		this.setName(name);
		this.setCpf(cpf);
		this.setEmail(email);
		this.setTelefone(telefone);
		this.dataCadastro = LocalDate.now();
	}

	// Getters e Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;	
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}
	
	public void setDataCadastro(LocalDate dataCadastro) {
	    this.dataCadastro = dataCadastro;
	}
	
	public List<Order> getPedidos() {
	    return pedidos;
	}

	// Métodos para adicionar e remover pedidos do cliente
	public void addPedido(Order pedido) {
	    this.pedidos.add(pedido);
	}

	public void removePedido(Order pedido) {
	    this.pedidos.remove(pedido);
	}
}