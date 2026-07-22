package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Client;

// DAO específico para a entidade Cliente
public class ClientDAO extends BaseDAO<Client>{
	
	// Métodos
	
	@Override
	public boolean inserir(Client cliente) {
		String sql = "INSERT INTO clientes (nome, cpf, email, telefone, data_criacao) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection conn = ConexaoBD.getConexao();
			PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
			
			 stmt.setString(1, cliente.getName());
			 stmt.setString(2, cliente.getCpf());
	         stmt.setString(3, cliente.getEmail());
	         stmt.setString(4, cliente.getTelefone());
	         stmt.setTimestamp(5, java.sql.Timestamp.valueOf(cliente.getDataCadastro().atStartOfDay()));
	         
	         int linhasAfetadas = stmt.executeUpdate();
	         if (linhasAfetadas > 0) {
	        	 try (ResultSet rs = stmt.getGeneratedKeys()) {
	        		 if (rs.next()) {
	        			 cliente.setId(rs.getInt(1));
	        		 }
	        	 }
	        	 return true;
	         }
	         
	         return false;

		} catch (SQLException e) {
			if (e.getMessage().contains("clientes_email_key"))
		        throw new RuntimeException("E-mail já cadastrado");
		    else if (e.getMessage().contains("clientes_cpf_key"))
		        throw new RuntimeException("CPF já cadastrado");
		    else if (e.getMessage().contains("clientes_telefone_key"))
		        throw new RuntimeException("Telefone já cadastrado");
		    else
		        throw new RuntimeException("Erro ao cadastrar cliente: " + e.getMessage());
		}
	}
	
	// Método para listar todos os clientes
	@Override
	public List<Client> listarTodos() {
	    List<Client> clientes = new ArrayList<>();
	    String sql = "SELECT * FROM clientes ORDER BY nome";

	    try (Connection conn = ConexaoBD.getConexao();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        while (rs.next()) {
	            clientes.add(extrairCLienteDoResultSet(rs));
	        }

	        System.out.println("✓ " + clientes.size() + " cliente(s) encontrado(s)");

	    } catch (SQLException e) {
	        System.err.println("Erro ao listar clientes: " + e.getMessage());
	    }

	    return clientes;
	}
	
	// Método para atualizar cliente existente
	@Override
	public boolean atualizar(Client cliente) {
	    String sql = "UPDATE clientes SET nome = ?, cpf = ?, email = ?, telefone = ? WHERE id = ?";

	    try (Connection conn = ConexaoBD.getConexao();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, cliente.getName());
	        stmt.setString(2, cliente.getCpf());
	        stmt.setString(3, cliente.getEmail());
	        stmt.setString(4, cliente.getTelefone());
	        stmt.setLong(5, cliente.getId());

	        int linhasAfetadas = stmt.executeUpdate();
	        if (linhasAfetadas > 0) {
	            return true;
	        } else {
	            System.out.println("Nenhum Cliente encontrado com o ID: " + cliente.getId());
	            return false;
	        }

	    } catch (SQLException e) {
	        if (e.getMessage().contains("clientes_cpf_key"))
	            throw new RuntimeException("Erro: CPF já cadastrado");
	        else if (e.getMessage().contains("clientes_email_key"))
	            throw new RuntimeException("Erro: E-mail já cadastrado");
	        else if (e.getMessage().contains("clientes_telefone_key"))
	            throw new RuntimeException("Erro: Telefone já cadastrado");
	        else
	            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage());
	    }
	}
	
	// Método para excluir cliente por ID
	@Override
	public boolean excluir(long id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                return true;
            } else {
                System.out.println("Nenhum Cliente encontrado com o ID: " + id);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir Cliente: " + e.getMessage());
            return false;
        }
    }
	
	// Método para buscar cliente por ID
	@Override
	public Client buscarPorId(long id) {
		String sql = "SELECT * FROM clientes WHERE id = ?";
	    try (Connection conn = ConexaoBD.getConexao();
	    	PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	    	stmt.setLong(1, id);
	            
	        try (ResultSet rs = stmt.executeQuery()) {
	        	if (rs.next()) {
	        		return extrairCLienteDoResultSet(rs);
	            }
	        }
	            
	        } catch (SQLException e) {
	            System.err.println("Erro ao buscar cliente: " + e.getMessage());
	        }
	        
	    return null;
	 }
	 
	 @Override
	 public int contarTotal() {
	        String sql = "SELECT COUNT(*) FROM clientes";
	        
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
	 
	 @Override
	 public boolean existe(long id) {
		 return buscarPorId(id) != null;
	 }
	 
	 
	 public List<Client> buscarPorNome(String nome) {
		 List<Client> clientes = new ArrayList<>();
	     String sql = "SELECT * FROM clientes WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";
	        
	     try (Connection conn = ConexaoBD.getConexao();
	    		 PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	    	 stmt.setString(1, "%" + nome + "%");
	            
	         try (ResultSet rs = stmt.executeQuery()) {
	        	 while (rs.next()) {
	        		 clientes.add(extrairCLienteDoResultSet(rs));
	        	 }
	         }
	         System.out.println("✓ " + clientes.size() + " cliente(s) encontrado(s) com o nome '" + nome + "'");
	            
	     } catch (SQLException e) {
	    	 System.err.println("Erro ao buscar cliente por nome: " + e.getMessage());
	     }
	        
	     return clientes;
	 }
	 
	 private Client extrairCLienteDoResultSet(ResultSet rs) throws SQLException {
		 Client cliente = new Client();
		 cliente.setId(rs.getLong("id"));
		 cliente.setName(rs.getString("nome"));
		 cliente.setCpf(rs.getString("cpf"));
		 cliente.setEmail(rs.getString("email"));
		 cliente.setTelefone(rs.getString("telefone"));
		 cliente.setDataCadastro(rs.getDate("data_criacao").toLocalDate());
	     return cliente;
	 } 
}