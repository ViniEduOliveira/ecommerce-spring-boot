package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Client;
import entities.Order;
import entities.OrderItem;
import entities.Product;
import entities.enuns.OrderStatus;

public class OrderDAO extends BaseDAO<Order> {

    @Override
    public boolean inserir(Order pedido) {
        String sql = "INSERT INTO pedidos(Data_criacao, Valor_total, Status, ID_Cliente) VALUES(?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(pedido.getDataCriacao().atStartOfDay()));
            stmt.setDouble(2, pedido.valorTotal());
            stmt.setString(3, pedido.getStatus().name());
            stmt.setLong(4, pedido.getCliente().getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) pedido.setId(rs.getInt(1));
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            if (e.getMessage().contains("fk_pedido_cliente"))
                throw new RuntimeException("Erro: Cliente não encontrado");
            else
                throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage());
        }
    }

    @Override
    public List<Order> listarTodos() {
        List<Order> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) pedidos.add(extrairPedidoDoResultSet(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    @Override
    public boolean atualizar(Order pedido) {
        String sql = "UPDATE pedidos SET Status = ?, Valor_total = ? WHERE ID = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getStatus().name());
            stmt.setDouble(2, pedido.valorTotal());
            stmt.setLong(3, pedido.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pedido: " + e.getMessage());
        }
    }

    @Override
    public boolean excluir(long id) {
        String sqlDeleteItens = "DELETE FROM itens_pedido WHERE ID_Pedido = ?";
        String sqlDeletePedido = "DELETE FROM pedidos WHERE ID = ?";

        try (Connection conn = ConexaoBD.getConexao()) {
            try (PreparedStatement stmtItens = conn.prepareStatement(sqlDeleteItens)) {
                stmtItens.setLong(1, id);
                stmtItens.executeUpdate();
            }

            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlDeletePedido)) {
                stmtPedido.setLong(1, id);
                return stmtPedido.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir pedido: " + e.getMessage());
        }
    }

    @Override
    public Order buscarPorId(long id) {
        String sql = "SELECT * FROM pedidos WHERE ID = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extrairPedidoDoResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pedido: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM pedidos";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.err.println("Erro ao contar pedidos: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean existe(long id) {
        return buscarPorId(id) != null;
    }

    public List<Order> buscarPorCliente(long idCliente) {
        List<Order> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE ID_Cliente = ? ORDER BY Data_criacao";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) pedidos.add(extrairPedidoDoResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pedidos por cliente: " + e.getMessage());
        }
        return pedidos;
    }

    public boolean adicionarItem(long idPedido, OrderItem item) {
        String sql = "INSERT INTO itens_pedido (quantidade, Preco_produto, ID_Pedido, ID_Produto) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getQuantidade());
            stmt.setDouble(2, item.getPrecoProduto());
            stmt.setLong(3, idPedido);
            stmt.setLong(4, item.getProduto().getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar item: " + e.getMessage());
        }
    }

    public boolean removerItem(long idPedido, long idProduto) {
        String sql = "DELETE FROM itens_pedido WHERE ID_Pedido = ? AND ID_Produto = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idPedido);
            stmt.setLong(2, idProduto);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover item: " + e.getMessage());
        }
    }

    public boolean atualizarQuantidadeItem(long idPedido, long idProduto, int novaQuantidade) {
        String sql = "UPDATE itens_pedido SET quantidade = ? WHERE ID_Pedido = ? AND ID_Produto = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novaQuantidade);
            stmt.setLong(2, idPedido);
            stmt.setLong(3, idProduto);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quantidade: " + e.getMessage());
        }
    }

    public void carregarItens(Order pedido) {
        String sql = "SELECT * FROM itens_pedido WHERE ID_Pedido = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, pedido.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                ProductDAO productDAO = new ProductDAO();

                while (rs.next()) {
                    long idProduto = rs.getLong("ID_Produto");
                    Product produto = productDAO.buscarPorId(idProduto);

                    OrderItem item = new OrderItem(
                            rs.getInt("quantidade"),
                            rs.getDouble("Preco_produto"),
                            produto,
                            pedido
                    );
                    item.setId(rs.getLong("ID"));
                    pedido.addItem(item);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar itens do pedido: " + e.getMessage());
        }
    }

    private Order extrairPedidoDoResultSet(ResultSet rs) throws SQLException {
        Order pedido = new Order();
        pedido.setId(rs.getLong("ID"));
        pedido.setStatus(OrderStatus.valueOf(rs.getString("Status")));
        pedido.setDataCriacao(rs.getTimestamp("Data_criacao").toLocalDateTime().toLocalDate());

        long idCliente = rs.getLong("ID_Cliente");
        ClientDAO clientDAO = new ClientDAO();
        Client cliente = clientDAO.buscarPorId(idCliente);
        pedido.setCliente(cliente);

        carregarItens(pedido);

        return pedido;
    }
}