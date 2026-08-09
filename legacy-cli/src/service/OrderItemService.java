package service;

import java.util.List;
import dao.OrderDAO;
import dao.OrderItemDAO;
import entities.Order;
import entities.OrderItem;
import entities.enuns.OrderStatus;

// Serviço para gerenciar as regras de negócio dos itens de pedido
public class OrderItemService extends BaseService<OrderItem> {
    private OrderItemDAO dao = new OrderItemDAO();
    private OrderDAO orderDAO = new OrderDAO();

    // Métodos

    @Override
    public void salvar(OrderItem itemPedido) {
        validarQuantidade(itemPedido.getQuantidade());
        validarPrecoProduto(itemPedido.getPrecoProduto());
        dao.inserir(itemPedido);
    }
    
    // Método para listar todos os itens de pedido
    @Override
    public List<OrderItem> listarTodos() { return null; }
    
    // Método para atualizar item de pedido
    @Override
    public void atualizar(OrderItem entidade) {}

    // Método para excluir item de pedido
    @Override
    public void excluir(OrderItem itemPedido) {}

    // Método para buscar item de pedido por ID
    @Override
    public OrderItem buscarPorId(long id) { return null; }


    // Método para adicionar item ao pedido em aberto
    public void adicionarNovoItem(Order pedido, OrderItem item) {
        if (pedido.getStatus() != OrderStatus.EM_ABERTO)
            throw new IllegalArgumentException("Erro: Só é possível adicionar itens em pedidos EM_ABERTO");
        if (item.getQuantidade() <= 0)
            throw new IllegalArgumentException("Erro: A quantidade deve ser maior que zero");
        if (item.getProduto().getQtdEstoque() < item.getQuantidade())
            throw new IllegalArgumentException("Erro: Estoque insuficiente para o produto " + item.getProduto().getName());

        dao.inserir(item);
        pedido.addItem(item);
        orderDAO.atualizar(pedido);
    }

    // Método para remover item do pedido
    public void removerItem(Order pedido, long idProduto) {
        if (pedido.getStatus() != OrderStatus.EM_ABERTO)
            throw new IllegalArgumentException("Erro: Só é possível remover itens de pedidos EM_ABERTO");

        OrderItem itemParaRemover = null;
        for (OrderItem item : pedido.getItens()) {
            if (item.getProduto().getId() == idProduto) {
                itemParaRemover = item;
                break;
            }
        }

        if (itemParaRemover == null)
            throw new IllegalArgumentException("Erro: Produto não encontrado no pedido");

        dao.excluir(itemParaRemover.getId());
        pedido.removeItem(itemParaRemover);
        orderDAO.atualizar(pedido);
    }

    // Método para atualizar quantidade de item no pedido
    public void atualizarQuantidadeItem(Order pedido, long idProduto, int novaQuantidade, int estoqueDisponivel) {
        if (pedido.getStatus() != OrderStatus.EM_ABERTO)
            throw new IllegalArgumentException("Erro: Só é possível alterar itens de pedidos EM_ABERTO");
        if (novaQuantidade <= 0)
            throw new IllegalArgumentException("Erro: A quantidade deve ser maior que zero");
        if (estoqueDisponivel < novaQuantidade)
            throw new IllegalArgumentException("Erro: Estoque insuficiente");

        for (OrderItem item : pedido.getItens()) {
            if (item.getProduto().getId() == idProduto) {
                int diferenca = novaQuantidade - item.getQuantidade();
                if (diferenca > 0)
                    item.addQuantidade(diferenca);
                else if (diferenca < 0)
                    item.removeQuantidade(Math.abs(diferenca));
                dao.atualizarQuantidade(item);
                break;
            }
        }
        orderDAO.atualizar(pedido);
    }

    // Método para validar a quantidade do item
    public void validarQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade < 0)
            throw new IllegalArgumentException("Erro: Tenha ao menos uma unidade do produto");
    }

    // Método para validar o preço do item
    public void validarPrecoProduto(Double preco) {
        if (preco == null || preco < 0)
            throw new IllegalArgumentException("Erro: O preço tem que ser maior que 0");
    }
}