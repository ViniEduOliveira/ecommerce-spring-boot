package entities;

// Classe que representa um item do pedido
public class OrderItem extends ModelBase{
    // Atributos do item do pedido
    private Integer quantidade;
    private Double precoProduto;
    private Product produto;
    private Order pedido;

    // Construtor vazio
    public OrderItem() {
        super();
    }

    // Construtor parametrizado
    public OrderItem(Integer quantidade, Double precoProduto, Product produto, Order pedido) {
        this.quantidade = quantidade;
        this.setPrecoProduto(precoProduto);
        this.setProduto(produto);
        this.setPedido(pedido);
    }

    // Método para adicionar quantidade ao item do pedido
    public void addQuantidade(int qtd) {
		this.quantidade += qtd;
	}
	
    // Método para remover quantidade do item do pedido
    public void removeQuantidade(int qtd) {
        this.quantidade -= qtd;
    }

    // Método para calcular o subtotal do item do pedido
    public Double subtotal() {
        if (this.precoProduto != null && this.quantidade != null) {
            return this.precoProduto * this.quantidade;
        }
        return 0.0;
    }

    // Getters e Setters
    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(Double precoProduto) {
        this.precoProduto = precoProduto;
    }

    public Product getProduto() {
        return produto;
    }

    public void setProduto(Product produto) {
        this.produto = produto;
    }
    
    public Order getPedido() {
        return pedido;
    }

    public void setPedido(Order pedido) {
        this.pedido = pedido;
    }
}