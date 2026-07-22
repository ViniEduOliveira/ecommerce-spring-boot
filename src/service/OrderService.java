package service;

import java.time.LocalDate;
import java.util.List;

import dao.OrderDAO;
import entities.Order;
import entities.OrderItem;
import entities.enuns.OrderStatus;

// Serviço para gerenciar as regras de negócio da entidade Pedido
public class OrderService extends BaseService<Order> {
    private OrderDAO dao = new OrderDAO();
    private ProductService productService = new ProductService();

    // Métodos

    @Override
    public void salvar(Order pedido) {
        validarData(pedido.getDataCriacao());
        validarStatus(pedido.getStatus());
        dao.inserir(pedido);
    }
    
    // Método para listar todos os pedidos
    @Override
    public List<Order> listarTodos() {
        List<Order> pedidos = dao.listarTodos();
        if (pedidos != null && !pedidos.isEmpty())
            pedidos.sort(java.util.Comparator.comparing(Order::getStatus));
        return pedidos;
    }
    
    @Override
    public void atualizar(Order pedido) {}

    // Método para excluir pedido após validação de status
    @Override
    public void excluir(Order pedido) {
        if (pedido == null)
            throw new IllegalArgumentException("Erro: Pedido não encontrado");

        if (pedido.getStatus() != OrderStatus.EM_ABERTO &&
            pedido.getStatus() != OrderStatus.CANCELADO)
            throw new IllegalArgumentException("Erro: Não é possível excluir um pedido em andamento");

        if (pedido.getCliente() != null)
            pedido.getCliente().removePedido(pedido);

        dao.excluir(pedido.getId());
    }
    
    // Método para buscar pedido por ID
    @Override
    public Order buscarPorId(long id) {
        return dao.buscarPorId(id);
    }
    
    
    // Método para atualizar status do pedido
    public void atualizarStatus(Order pedido, OrderStatus novoStatus) {
        if (pedido == null)
            throw new IllegalArgumentException("Erro: Pedido não encontrado");
        if (novoStatus == null)
            throw new IllegalArgumentException("Erro: Status inválido");

        List<OrderStatus> ordem = List.of(
            OrderStatus.EM_ABERTO,
            OrderStatus.PAGO,
            OrderStatus.EM_SEPARACAO,
            OrderStatus.ENVIADO,
            OrderStatus.ENTREGUE
        );

        OrderStatus statusAtual = pedido.getStatus();

        if (novoStatus == OrderStatus.CANCELADO) {
            if (statusAtual == OrderStatus.ENTREGUE)
                throw new IllegalArgumentException("Erro: Pedido já entregue não pode ser cancelado");
            pedido.setStatus(novoStatus);
            dao.atualizar(pedido);
            return;
        }

        if (!ordem.contains(novoStatus) || ordem.indexOf(novoStatus) <= ordem.indexOf(statusAtual))
            throw new IllegalArgumentException("Erro: Não é possível voltar ou manter o status atual");

        if (novoStatus == OrderStatus.PAGO) {
            for (OrderItem item : pedido.getItens()) {
                productService.baixarEstoque(item.getProduto(), item.getQuantidade());
            }
        }

        pedido.setStatus(novoStatus);
        dao.atualizar(pedido);
    }

    // Método para validar data do pedido
    public void validarData(LocalDate data) {
        if (data == null)
            throw new IllegalArgumentException("Erro: A data do pedido não pode ser nula");
        if (data.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Erro: A data do pedido não pode ser no futuro");
    }

    // Método para validar status do pedido
    public void validarStatus(OrderStatus status) {
        if (status == null)
            throw new IllegalArgumentException("Erro: O status do pedido não pode ser nulo");
    }
}