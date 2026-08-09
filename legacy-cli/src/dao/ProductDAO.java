package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Category;
import entities.Product;
import entities.Supplier;
import entities.enuns.CategoryType;

// DAO específico para a entidade Produto
public class ProductDAO extends BaseDAO<Product> {

    // Métodos

    @Override
    public boolean inserir(Product produto) {
        String sql = "INSERT INTO produtos (nome, descricao, preco, quantidade_estoque, sku) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getName());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQtdEstoque());
            stmt.setString(5, produto.getSku());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) produto.setId(rs.getLong(1));
                }

                for (Category categoria : produto.getCategorias())
                    vincularCategoria(produto.getId(), categoria.getId());

                for (Supplier fornecedor : produto.getFornecedores())
                    vincularFornecedor(produto.getId(), fornecedor.getId());
                return true;
            }
            return false;

        } catch (SQLException e) {
            if (e.getMessage().contains("produtos_sku_key"))
                throw new RuntimeException("Erro: SKU já cadastrado");
            else
                throw new RuntimeException("Erro ao inserir produto: " + e.getMessage());
        }
    }
    
    // Método para listar todos os produtos
    @Override
    public List<Product> listarTodos() {
        List<Product> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY nome";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) produtos.add(extrairProdutoDoResultSet(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    // Método para atualizar produto existente
    @Override
    public boolean atualizar(Product produto) {
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, preco = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getName());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setLong(4, produto.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto no banco: " + e.getMessage());
        }
    }

    // Método para excluir produto por ID
    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage());
        }
    }

    // Método para buscar produto por ID
    @Override
    public Product buscarPorId(long id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extrairProdutoDoResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
        return null;
    }
    
    // Método para contar produtos cadastrados
    @Override
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM produtos";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.err.println("Erro ao contar produtos: " + e.getMessage());
        }
        return 0;
    }

    // Método para verificar se um produto existe
    @Override
    public boolean existe(long id) {
        return buscarPorId(id) != null;
    }

    
    // Método para buscar produtos por nome
    public List<Product> buscarPorNome(String nome) {
        List<Product> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) produtos.add(extrairProdutoDoResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por nome: " + e.getMessage());
        }
        return produtos;
    }
    
    // Método para atualizar estoque do produto
    public boolean atualizarEstoque(long id, int qtdEstoque) {
        String sql = "UPDATE produtos SET quantidade_estoque = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, qtdEstoque);
            stmt.setLong(2, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar estoque: " + e.getMessage());
        }
    }

    // Método para vincular categoria ao produto
    public boolean vincularCategoria(long idProduto, long idCategoria) {
        String sql = "INSERT INTO categoria_produto (id_produto, id_categoria) VALUES (?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);
            stmt.setLong(2, idCategoria);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular categoria ao produto: " + e.getMessage());
        }
    }
    
    // Método para desvincular categoria do produto
    public boolean desvincularCategoria(long idProduto, long idCategoria) {
        String sql = "DELETE FROM categoria_produto WHERE id_produto = ? AND id_categoria = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);
            stmt.setLong(2, idCategoria);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desvincular categoria do produto: " + e.getMessage());
        }
    }

    // Método para vincular fornecedor ao produto
    public boolean vincularFornecedor(long idProduto, long idFornecedor) {
        String sql = "INSERT INTO fornecedor_produto (id_produto, id_fornecedor) VALUES (?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);
            stmt.setLong(2, idFornecedor);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular fornecedor ao produto: " + e.getMessage());
        }
    }

    // Método para desvincular fornecedor do produto
    public boolean desvincularFornecedor(long idProduto, long idFornecedor) {
        String sql = "DELETE FROM fornecedor_produto WHERE id_produto = ? AND id_fornecedor = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);
            stmt.setLong(2, idFornecedor);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desvincular fornecedor do produto: " + e.getMessage());
        }
    }

    // Método para verificar se produto está em pedidos em andamento
    public boolean temPedidosEmAndamento(long idProduto) {
        String sql = "SELECT COUNT(*) FROM itens_pedido ip " +
                     "JOIN pedidos p ON ip.id_pedido = p.id " +
                     "WHERE ip.id_produto = ? " +
                     "AND p.status IN ('PAGO', 'EM_SEPARACAO', 'ENVIADO', 'ENTREGUE')";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar pedidos em andamento: " + e.getMessage());
        }
        return false;
    }

    // Método para buscar categorias associadas ao produto
    public List<Category> buscarCategoriasPorProduto(long idProduto) {
        List<Category> categorias = new ArrayList<>();
        String sql = "SELECT c.* FROM categorias c " +
                     "JOIN categoria_produto cp ON c.id = cp.id_categoria " +
                     "WHERE cp.id_produto = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category categoria = new Category();
                    categoria.setId(rs.getLong("id"));
                    categoria.setName(CategoryType.valueOf(rs.getString("nome")));
                    categoria.setDescricao(rs.getString("descricao"));
                    categorias.add(categoria);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar categorias do produto: " + e.getMessage());
        }
        return categorias;
    }

    // Método para buscar fornecedores associados ao produto
    public List<Supplier> buscarFornecedoresPorProduto(long idProduto) {
        List<Supplier> fornecedores = new ArrayList<>();
        String sql = "SELECT f.* FROM fornecedores f " +
                     "JOIN fornecedor_produto fp ON f.id = fp.id_fornecedor " +
                     "WHERE fp.id_produto = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Supplier fornecedor = new Supplier();
                    fornecedor.setId(rs.getLong("id"));
                    fornecedor.setName(rs.getString("nome"));
                    fornecedor.setCnpj(rs.getString("cnpj"));
                    fornecedor.setEmail(rs.getString("email"));
                    fornecedor.setTelefone(rs.getString("telefone"));
                    fornecedores.add(fornecedor);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar fornecedores do produto: " + e.getMessage());
        }
        return fornecedores;
    }
    
    // Método para excluir itens relacionados ao produto
    public boolean excluirItensPorProduto(long idProduto) {
        String sql = "DELETE FROM itens_pedido WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir itens do produto: " + e.getMessage());
        }
    }

    // Método auxiliar para extrair produto do ResultSet
    private Product extrairProdutoDoResultSet(ResultSet rs) throws SQLException {
        Product produto = new Product();
        produto.setId(rs.getLong("id"));
        produto.setName(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setPreco(rs.getDouble("preco"));
        produto.addQtdEstoque(rs.getInt("quantidade_estoque"));
        produto.setSku(rs.getString("sku"));

        for (Category c : buscarCategoriasPorProduto(produto.getId()))
            produto.addCategoria(c);

        for (Supplier s : buscarFornecedoresPorProduto(produto.getId()))
            produto.addFornecedor(s);

        return produto;
    }
}