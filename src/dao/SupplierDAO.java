package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Supplier;

// DAO específico para a entidade Fornecedor
public class SupplierDAO extends BaseDAO<Supplier>{
	
	// Métodos
	
	@Override
	public boolean inserir(Supplier fornecedor) {
		String sql = "INSERT INTO fornecedores (nome, cnpj, telefone, email) VALUES (?, ?, ?, ?)";
		
		try (Connection conn = ConexaoBD.getConexao();
			PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
		
			stmt.setString(1, fornecedor.getName());
			stmt.setString(2, fornecedor.getCnpj());
			stmt.setString(3, fornecedor.getTelefone()); 
			stmt.setString(4, fornecedor.getEmail());
	         
	         int linhasAfetadas = stmt.executeUpdate();
	         if (linhasAfetadas > 0) {
	        	 try (ResultSet rs = stmt.getGeneratedKeys()) {
	        		 if (rs.next()) {
	        			 fornecedor.setId(rs.getInt(1));
	        		 }
	        	 }
	        	 
	        	 return true;
	         }
	         return false;
	         
		} catch (SQLException e) {
			if (e.getMessage().contains("fornecedores_cnpj_key"))
	            System.err.println("CNPJ já cadastrado");
	        else if (e.getMessage().contains("fornecedores_email_key"))
	            System.err.println("E-mail já cadastrado");
	        else if (e.getMessage().contains("fornecedores_telefone_key"))
	            System.err.println("Telefone já cadastrado");
	        else
	            System.err.println("Erro ao inserir fornecedor: " + e.getMessage());
            return false;
		}
	}
	
	// Método para listar todos os fornecedores
	@Override
	public List<Supplier> listarTodos() {
	    List<Supplier> clientes = new ArrayList<>();
	    String sql = "SELECT * FROM fornecedores ORDER BY nome";

	    try (Connection conn = ConexaoBD.getConexao();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        while (rs.next()) {
	            clientes.add(extrairFornecedorDoResultSet(rs));
	        }

	        System.out.println("✓ " + clientes.size() + " forncedor(es) encontrado(s)");

	    } catch (SQLException e) {
	        System.err.println("Erro ao listar fornecedores: " + e.getMessage());
	    }

	    return clientes;
	}
	
	// Método para atualizar fornecedor existente
	@Override
	public boolean atualizar(Supplier fornecedor) {
	    String sql = "UPDATE fornecedores SET nome = ?, cnpj = ?, email = ?, telefone = ? WHERE id = ?";

	    try (Connection conn = ConexaoBD.getConexao();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, fornecedor.getName());
	        stmt.setString(2, fornecedor.getCnpj());
	        stmt.setString(3, fornecedor.getEmail());
	        stmt.setString(4, fornecedor.getTelefone());
	        stmt.setLong(5, fornecedor.getId());

	        int linhasAfetadas = stmt.executeUpdate();
	        if (linhasAfetadas > 0) {
	            return true;
	        } else {
	            System.out.println("Nenhum fornecedor encontrado com o ID: " + fornecedor.getId());
	            return false;
	        }

	    } catch (SQLException e) {
	        if (e.getMessage().contains("fornecedores_cnpj_key"))
	            throw new RuntimeException("CNPJ já cadastrado");
	        else if (e.getMessage().contains("fornecedores_email_key"))
	            throw new RuntimeException("E-mail já cadastrado");
	        else if (e.getMessage().contains("fornecedores_telefone_key"))
	            throw new RuntimeException("Telefone já cadastrado");
	        else
	            throw new RuntimeException("Erro ao atualizar fornecedor: " + e.getMessage());
	    }
	}
	
	// Método para excluir fornecedor por ID
	@Override
	public boolean excluir(long id) {
        String sql = "DELETE FROM fornecedores WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                return true;
            } else {
                System.out.println("Nenhum fornecedor encontrado com o ID: " + id);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir fornecedor: " + e.getMessage());
            return false;
        }
    }
	
	// Método para buscar fornecedor por ID
	@Override
	public Supplier buscarPorId(long id) {
        String sql = "SELECT * FROM fornecedores WHERE id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairFornecedorDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar fornecedor: " + e.getMessage());
        }
        
        return null;
	}
	
	// Método para contar fornecedores cadastrados
	@Override
	 public int contarTotal() {
	        String sql = "SELECT COUNT(*) FROM fornecedores";
	        
	        try (Connection conn = ConexaoBD.getConexao();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	            
	        } catch (SQLException e) {
	            System.err.println("Erro ao contar cliente: " + e.getMessage());
	        }
	        
	        return 0;
	  }
	
	// Método para verificar se um fornecedor existe
	@Override
	 public boolean existe(long id) {
		 return buscarPorId(id) != null;
	 }
 
	 // Método para buscar fornecedores por nome
	 public List<Supplier> buscarPorNome(String nome) {
	        List<Supplier> fornecedores = new ArrayList<>();
	        String sql = "SELECT * FROM fornecedores WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";
	        
	        try (Connection conn = ConexaoBD.getConexao();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, "%" + nome + "%");
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    fornecedores.add(extrairFornecedorDoResultSet(rs));
	                }
	            }
	            
	            System.out.println("✓ " + fornecedores.size() + " fornecedor(es) encontrado(s) com o nome '" + nome + "'");
	            
	        } catch (SQLException e) {
	            System.err.println("Erro ao buscar fornecedor por nome: " + e.getMessage());
	    }
        
	    return fornecedores;
	 }
	 
	 // Método auxiliar para extrair fornecedor do ResultSet
	 private Supplier extrairFornecedorDoResultSet(ResultSet rs) throws SQLException {
		 Supplier fornecedor = new Supplier();
		 fornecedor.setId(rs.getLong("id"));
		 fornecedor.setName(rs.getString("nome"));
		 fornecedor.setCnpj(rs.getString("cnpj"));
		 fornecedor.setEmail(rs.getString("email"));
		 fornecedor.setTelefone(rs.getString("telefone"));
	        return fornecedor;
	 } 
}