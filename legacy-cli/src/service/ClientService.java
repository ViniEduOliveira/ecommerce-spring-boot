package service;

import java.util.List;

import dao.ClientDAO;
import entities.Client;
import entities.Order;
import entities.enuns.OrderStatus;

// Serviço para gerenciar as regras de negócio da entidade Cliente
public class ClientService extends BaseService<Client>{
    private ClientDAO dao = new ClientDAO();

    // Métodos
	
	@Override
	public void salvar(Client cliente) {
		validarNome(cliente.getName());
	    validarCpf(cliente.getCpf());
	    validarEmail(cliente.getEmail());
	    validarTelefone(cliente.getTelefone());
	    
	    if (!dao.inserir(cliente))
	        throw new RuntimeException("Erro ao cadastrar cliente no banco");
                
	}
	
	@Override
	public List<Client> listarTodos() {
	    return dao.listarTodos();
	}
	
	@Override
	public void atualizar(Client cliente) {
	    if (cliente == null)
	        throw new IllegalArgumentException("Erro: Cliente não encontrado");

	    validarNome(cliente.getName());
	    validarCpf(cliente.getCpf());
	    validarEmail(cliente.getEmail());
	    validarTelefone(cliente.getTelefone());

	    if (!dao.atualizar(cliente))
	        throw new RuntimeException("Erro ao atualizar cliente no banco");
	}
	
	@Override
	public void excluir(Client cliente) {
		if (cliente == null)
	        throw new IllegalArgumentException("Erro: Cliente não encontrado");

	    for (Order pedido : cliente.getPedidos()) {
	        if (pedido.getStatus() != OrderStatus.EM_ABERTO &&
	            pedido.getStatus() != OrderStatus.CANCELADO)
	            throw new IllegalArgumentException("Erro: Cliente possui pedido(s) em andamento e não pode ser excluído");
	    }

	    dao.excluir(cliente.getId());
		
	}
	
	@Override
	public Client buscarPorId(long id) {
	    return dao.buscarPorId(id);
	}

	
	public List<Client> buscarPorNome(String nome) {
	    return dao.buscarPorNome(nome);
	}
		
	public void validarNome(String nome) {
		 if (nome == null || nome.trim().length() < 3) {
	            throw new IllegalArgumentException ("Erro: o nome pode ser menor que 3 caracteres");
	        }
	}
	
	public void validarCpf(String cpf) {
        if (cpf == null) throw new IllegalArgumentException ("Erro: CPF não pode ser nulo");
        String limpo = cpf.replaceAll("[^0-9]", "");
        if (limpo.length() != 11) {
        	throw new IllegalArgumentException("Erro: CPF precisa de 11 números");
        }
    }
	
	public void validarEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Erro: E-mail não pode ser nulo");
        email = email.trim().toLowerCase();
        if (!email.contains("@") || !(email.lastIndexOf(".") > email.indexOf("@"))) {
        	 throw new IllegalArgumentException("Erro: Formato de e-mail inválido");
        }
    }
	
	public void validarTelefone(String tel) {
        if (tel == null) throw new IllegalArgumentException("Erro: Telefone não pode ser nulo");
        String limpo = tel.replaceAll("[^0-9]", "");
        if (limpo.length() != 11 || !limpo.substring(2).startsWith("9")) {
        	 throw new IllegalArgumentException("Erro: Telefone inválido (Use DDD + 9 + número)");
        }
    }
}