package service;

import java.util.List;

import dao.ProductDAO;
import dao.SupplierDAO;
import entities.Product;
import entities.Supplier;

// Serviço para gerenciar as regras de negócio da entidade Fornecedor
public class SupplierService extends BaseService<Supplier>{
    private SupplierDAO dao = new SupplierDAO();
    private ProductDAO produtoDAO = new ProductDAO();

    // Métodos

	@Override
    public void salvar(Supplier fornecedor) {
		
		validarNome(fornecedor.getName());
	    validarCnpj(fornecedor.getCnpj());
	    validarEmail(fornecedor.getEmail());
	    validarTelefone(fornecedor.getTelefone());
     
	    dao.inserir(fornecedor);
    }
	
	// Método para listar todos os fornecedores
	@Override
	public List<Supplier> listarTodos() {
	    return dao.listarTodos();
	}
	
	// Método para atualizar fornecedor existente
	@Override
	public void atualizar(Supplier fornecedor) {
	    if (fornecedor == null)
	        throw new IllegalArgumentException("Erro: Fornecedor não encontrado");

	    validarNome(fornecedor.getName());
	    validarCnpj(fornecedor.getCnpj());
	    validarEmail(fornecedor.getEmail());
	    validarTelefone(fornecedor.getTelefone());

	    if (!dao.atualizar(fornecedor))
	        throw new RuntimeException("Erro ao atualizar fornecedor no banco");
	}
	
	// Método para excluir fornecedor e desvincular produtos
	@Override
	public void excluir(Supplier fornecedor) {
		if (fornecedor == null)
	        throw new IllegalArgumentException("Erro: Fornecedor não encontrado");

		for (Product produto : fornecedor.getProduto()) {
		    produto.removeFornecedor(fornecedor);
		    produtoDAO.desvincularFornecedor(produto.getId(), fornecedor.getId());
	    }

	    dao.excluir(fornecedor.getId());

	}
	
	// Método para buscar fornecedor por ID
	@Override
	public Supplier buscarPorId(long id) {
	    return dao.buscarPorId(id);
	}
	
	
	// Método para buscar fornecedores por nome
	public List<Supplier> buscarPorNome(String nome) {
	    return dao.buscarPorNome(nome);
	}
	
	// Método para validar nome do fornecedor
	public void validarNome(String nome) {
		 if (nome == null || nome.trim().length() < 3) {
	            throw new IllegalArgumentException ("Erro: o nome pode ser menor que 3 caracteres");
	        }
	}
	
	// Método para validar CNPJ do fornecedor
	public void validarCnpj(String cpf) {
       if (cpf == null) throw new IllegalArgumentException ("Erro: CNPJ não pode ser nulo");
       String limpo = cpf.replaceAll("[^0-9]", "");
       if (limpo.length() != 14) {
       	throw new IllegalArgumentException("Erro: CNPJ precisa de 14 números");
       }
   }
	
	// Método para validar e-mail do fornecedor
	public void validarEmail(String email) {
       if (email == null) throw new IllegalArgumentException("Erro: E-mail não pode ser nulo");
       email = email.trim().toLowerCase();
       if (!email.contains("@") || !(email.lastIndexOf(".") > email.indexOf("@"))) {
       	 throw new IllegalArgumentException("Erro: Formato de e-mail inválido");
       }
   }
	
	// Método para validar telefone do fornecedor
	public void validarTelefone(String tel) {
       if (tel == null) throw new IllegalArgumentException("Erro: Telefone não pode ser nulo");
       String limpo = tel.replaceAll("[^0-9]", "");
       if (limpo.length() != 11 || !limpo.substring(2).startsWith("9")) {
       	 throw new IllegalArgumentException("Erro: Telefone inválido (Use DDD + 9 + número)");
       }
   }	 
}