package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe responsável por gerenciar a conexão com o banco de dados PostgreSQL
public class ConexaoBD {
    
    // Configurações de conexão
    private static final String URL = "jdbc:postgresql://localhost:5432/ecommerce_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    
    // Instância única de conexão
    private static Connection conexao = null;
    
    // Construtor privado para impedir instância direta
    private ConexaoBD() {
    }
    
    // Método para obter a conexão com o banco de dados
    public static Connection getConexao() throws SQLException {
        try {
            if (conexao == null || conexao.isClosed()) {
                Class.forName("org.postgresql.Driver");
                conexao = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✓ Conexão com banco de dados estabelecida com sucesso!");
            }
            return conexao;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Erro: Driver JDBC do PostgreSQL não encontrado!");
            System.err.println("  Verifique se o arquivo postgresql-XX.jar está na pasta lib/");
            throw new SQLException("Driver JDBC não encontrado", e);
        } catch (SQLException e) {
            System.err.println("✗ Erro ao conectar com o banco de dados!");
            System.err.println("  Mensagem: " + e.getMessage());
            System.err.println("  Verifique:");
            System.err.println("  - Se o PostgreSQL está rodando");
            System.err.println("  - Se o banco 'ecommerce_db' existe");
            System.err.println("  - Se usuário e senha estão corretos");
            throw e;
        }
    }
    
    // Método para fechar a conexão com o banco
    public static void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("✓ Conexão com banco de dados fechada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao fechar conexão: " + e.getMessage());
        }
    }
    
    // Método para testar se a conexão está funcionando
    public static boolean testarConexao() {
        try {
            Connection conn = getConexao();
            boolean valida = conn != null && !conn.isClosed();
            
            if (valida) {
                System.out.println("✓ Teste de conexão: SUCESSO");
                System.out.println("  Banco: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("  Versão: " + conn.getMetaData().getDatabaseProductVersion());
            }
            return valida;
        } catch (SQLException e) {
            System.err.println("✗ Teste de conexão: FALHOU");
            System.err.println("  Erro: " + e.getMessage());
            return false;
        }
    }
  
    // Método para exibir informações da conexão atual
    public static void exibirInformacoesConexao() {
        try {
            Connection conn = getConexao();
            System.out.println("========================================");
            System.out.println("    INFORMAÇÕES DA CONEXÃO");
            System.out.println("========================================");
            System.out.println("URL: " + URL);
            System.out.println("Usuário: " + USER);
            System.out.println("Status: " + (conn.isClosed() ? "Fechada" : "Aberta"));
            System.out.println("Banco: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Versão: " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + conn.getMetaData().getDriverName());
            System.out.println("========================================");
        } catch (SQLException e) {
            System.err.println("✗ Erro ao obter informações: " + e.getMessage());
        }
    }
}