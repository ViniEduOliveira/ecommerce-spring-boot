package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Category;
import entities.enuns.CategoryType;

// DAO específico para a entidade Categoria
public class CategoryDAO extends BaseDAO<Category> {

    // Implementação dos métodos abstratos definidos em BaseDAO para a entidade Categoria
    @Override
    public boolean inserir(Category categoria) {
        String sql = "INSERT INTO categorias(nome, descricao) VALUES (?,?)";

        // Tratamento de exceções para exibir possíveis erros
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getName().name());
            stmt.setString(2, categoria.getDescricao());

            int linhasAfetadas = stmt.executeUpdate();
            // Verifica se a inserção foi bem-sucedida e obtém o ID gerado
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) categoria.setId(rs.getLong(1));
                }
                System.out.println("Categoria inserida com sucesso! ID: " + categoria.getId());
                return true;
            }
            return false;

        } catch (SQLException e) {
            if (e.getMessage().contains("categorias_nome_key"))
                throw new RuntimeException("Categoria já cadastrada");
            else
                throw new RuntimeException("Erro ao inserir categoria: " + e.getMessage());
        }
    }
    
    // Método para listar todas as categorias, ordenadas por nome
    @Override
    public List<Category> listarTodos() {
        List<Category> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nome";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) categorias.add(extrairCategoriaDoResultSet(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }
        return categorias;
    }

    // Método para atualizar uma categoria existente, verificando se o ID existe
    @Override
    public boolean atualizar(Category categoria) {
        String sql = "UPDATE categorias SET descricao = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getDescricao());
            stmt.setLong(2, categoria.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            } else {
                System.out.println("Nenhuma Categoria encontrada com o ID: " + categoria.getId());
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria: " + e.getMessage());
        }
    }

    // Método para excluir uma categoria, verificando se o ID existe
    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            } else {
                System.out.println("Nenhuma Categoria encontrada com o ID: " + id);
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categoria: " + e.getMessage());
        }
    }

    // Método para buscar uma categoria por ID, retornando null se não encontrada
    @Override
    public Category buscarPorId(long id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extrairCategoriaDoResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria: " + e.getMessage());
        }
        return null;
    }
    
    // Método para contar o total de categorias cadastradas
    @Override
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM categorias";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.err.println("Erro ao contar categorias: " + e.getMessage());
        }
        return 0;
    }
    
    // Método para verificar se uma categoria existe por ID, utilizando o método buscarPorId
    @Override
    public boolean existe(long id) {
        return buscarPorId(id) != null;
    }
    
    // Método para buscar categorias por tipo, retornando uma lista de categorias correspondentes
    public List<Category> buscarTipo(CategoryType tipo) {
        List<Category> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE nome = ? ORDER BY nome";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) categorias.add(extrairCategoriaDoResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria por tipo: " + e.getMessage());
        }
        return categorias;
    }

    // Método para verificar se uma categoria existe por tipo, utilizando o método buscarTipo
    public boolean existePorTipo(CategoryType tipo) {
        String sql = "SELECT COUNT(*) FROM categorias WHERE nome = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.name());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar categoria: " + e.getMessage());
        }
        return false;
    }

    // Método auxiliar para extrair os dados de uma categoria do ResultSet e criar um objeto Category
    private Category extrairCategoriaDoResultSet(ResultSet rs) throws SQLException {
        Category categoria = new Category();
        categoria.setId(rs.getLong("id"));
        categoria.setName(CategoryType.valueOf(rs.getString("nome")));
        categoria.setDescricao(rs.getString("descricao"));
        return categoria;
    }   
}