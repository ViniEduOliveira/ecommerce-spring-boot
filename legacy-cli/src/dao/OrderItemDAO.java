package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Order;
import entities.OrderItem;
import entities.Product;

public class OrderItemDAO extends BaseDAO<OrderItem> {

    @Override
    public boolean inserir(OrderItem item) {
        String sql = "INSERT INTO itens_pedido (quantidade, preco_produto, id_pedido, id_produto) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, item.getQuantidade());
            stmt.setDouble(2, item.getPrecoProduto());
            stmt.setLong(3, item.getPedido().getId());
            stmt.setLong(4, item.getProduto().getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) item.setId(rs.getLong(1));
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            if (e.getMessage().contains("fk_itempedido_pedido"))
                throw new RuntimeException("Erro: Pedido não encontrado");
            else if (e.getMessage().contains("fk_itempedido_produto"))
                throw new RuntimeException("Erro: Produto não encontrado");
            else
                throw new RuntimeException("Erro ao inserir item: " + e.getMessage());
        }
    }

    @Override
    public List<OrderItem> listarTodos() { return null; }

    @Override
    public boolean atualizar(OrderItem entidade) { return false; }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM itens_pedido WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir item: " + e.getMessage());
        }
    }

    @Override
    public OrderItem buscarPorId(long id) {
        String sql = "SELECT * FROM itens_pedido WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extrairItemDoResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar item: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int contarTotal() { return 0; }

    @Override
    public boolean existe(long id) {
        return buscarPorId(id) != null;
    }

    public boolean atualizarQuantidade(OrderItem item) {
        String sql = "UPDATE itens_pedido SET quantidade = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getQuantidade());
            stmt.setLong(2, item.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quantidade: " + e.getMessage());
        }
    }

    public List<OrderItem> buscarPorPedido(long idPedido) {
        List<OrderItem> itens = new ArrayList<>();
        String sql = "SELECT * FROM itens_pedido WHERE id_pedido = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) itens.add(extrairItemDoResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens por pedido: " + e.getMessage());
        }

        return itens;
    }

    private OrderItem extrairItemDoResultSet(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.addQuantidade(rs.getInt("quantidade"));
        item.setPrecoProduto(rs.getDouble("preco_produto"));

        Order pedido = new Order();
        pedido.setId(rs.getLong("id_pedido"));
        item.setPedido(pedido);

        Product produto = new Product();
        produto.setId(rs.getLong("id_produto"));
        item.setProduto(produto);

        return item;
    }
}