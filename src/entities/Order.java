package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entities.enuns.OrderStatus;

// Classe que representa um pedido de compra
public class Order extends ModelBase{
	// Atributos
	private LocalDate dataCriacao;
	private OrderStatus status;
	private Client cliente;
	
	// Lista de itens do pedido
	private List<OrderItem> itens = new ArrayList<>(); 
	
	// Construtor vazio
	public Order() {
		super();
	}
	
	// Construtor parametrizado
	public Order(Client cliente) {
		this.dataCriacao = LocalDate.now();
		this.setStatus(OrderStatus.EM_ABERTO);
		this.setCliente(cliente);
	}

	// Método para adicionar um item ao pedido
	public void addItem(OrderItem item) {
		this.itens.add(item);
	}
	
	// Método para remover um item do pedido
	public void removeItem(OrderItem item) {
		this.itens.remove(item);
	}
	
	// Método para calcular o valor total do pedido
	public double valorTotal() {
		double soma = 0.0;
		for (OrderItem i : itens) {
			soma += i.subtotal();
		}
		
		return soma;
	}

	// Getters e Setters
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
	    this.dataCriacao = dataCriacao;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	public Client getCliente() {
	    return cliente;
	}

	public void setCliente(Client cliente) {
	    this.cliente = cliente;
	}

	public List<OrderItem> getItens() {
		return itens;
	}
}