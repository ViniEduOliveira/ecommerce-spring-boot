package view;

import java.time.format.DateTimeFormatter;
import java.util.List;

import entities.Client;
import service.ClientService;
import util.ScannerUtil;

// Classe responsável pela interface de usuário para gestão de clientes
public class ClientView extends BaseView<Client>{

    // Atributos
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static ClientService service = new ClientService();
    
    // Métodos
    @Override
    public void menu() {
        System.out.println("\n-----Gestão de Clientes-----");
        System.out.println("1. Cadastrar Cliente");
        System.out.println("2. Listar Clientes");
        System.out.println("3. Atualizar Cliente");
        System.out.println("4. Excluir Cliente");
        System.out.println("0. Voltar");
        System.out.print("Digite o número da escolha: ");
    }
    
    @Override
    public void processarMenu() {
        int opcao;
        do {
            menu();
            opcao = lerOpcao();
            ScannerUtil.getScanner().nextLine();
            switch (opcao) {
                case 1: cadastrar(); break;
                case 2: listar(); break;
                case 3: atualizar(); break;
                case 4: excluir(); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!"); break;
            }
        } while (opcao != 0);
    }
    
    @Override
    public void cadastrar() {
        System.out.println("\n-----Cadastrar Cliente-----");

        String nome;
        while (true) {
            System.out.print("Digite o nome do cliente: ");
            nome = ScannerUtil.getScanner().nextLine();
            try {
                service.validarNome(nome);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        String cpf;
        while (true) {
            System.out.print("Digite o CPF do cliente: ");
            cpf = ScannerUtil.getScanner().nextLine();
            try {
                service.validarCpf(cpf);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        String email;
        while (true) {
            System.out.print("Digite o email do cliente: ");
            email = ScannerUtil.getScanner().nextLine();
            try {
                service.validarEmail(email);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        String telefone;
        while (true) {
            System.out.print("Digite o telefone do cliente: ");
            telefone = ScannerUtil.getScanner().nextLine();
            try {
                service.validarTelefone(telefone);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        try {
            Client novoCliente = new Client(nome, cpf, email, telefone);
            service.salvar(novoCliente);
            System.out.println("Cliente inserido com sucesso! ID: " + novoCliente.getId());
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar o cliente: " + e.getMessage());
        }
    }
    
    @Override
    public void listar() {
    	List<Client> clientes = service.listarTodos();
    	
    	if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        System.out.println("\n-----Lista de Clientes-----");
        for (Client c : clientes) {
            System.out.println(exibir(c));
        }
        System.out.println("----------------------------");
    }
    
    @Override
    public void atualizar() {
        System.out.println("\n-----Atualizar Cliente-----");

        System.out.print("Digite o nome do cliente: ");
        String name = ScannerUtil.getScanner().nextLine();

        List<Client> clientes = service.buscarPorNome(name);

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado com o nome: " + name);
            return;
        }
        
        System.out.println("\n");
        for (Client c : clientes) {
            System.out.println(exibir(c));
        }

        System.out.print("\nDigite o ID do cliente que deseja atualizar: ");
        long id = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Client cliente = service.buscarPorId(id);

        if (cliente == null) {
            System.out.println("Cliente não encontrado com o ID: " + id);
            return;
        }

        String nome;
        while (true) {
            System.out.print("\nNovo nome (" + cliente.getName() + "): ");
            nome = ScannerUtil.getScanner().nextLine();
            try { service.validarNome(nome); break; }
            catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }

        String cpf;
        while (true) {
            System.out.print("Novo CPF (" + cliente.getCpf() + "): ");
            cpf = ScannerUtil.getScanner().nextLine();
            try { service.validarCpf(cpf); break; }
            catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }

        String email;
        while (true) {
            System.out.print("Novo email (" + cliente.getEmail() + "): ");
            email = ScannerUtil.getScanner().nextLine();
            try { service.validarEmail(email); break; }
            catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }

        String telefone;
        while (true) {
            System.out.print("Novo telefone (" + cliente.getTelefone() + "): ");
            telefone = ScannerUtil.getScanner().nextLine();
            try { service.validarTelefone(telefone); break; }
            catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }

        cliente.setName(nome);
        cliente.setCpf(cpf);
        cliente.setEmail(email);
        cliente.setTelefone(telefone);

        try {
            service.atualizar(cliente);
            System.out.println("Cliente atualizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }
    
    @Override
    public void excluir() {
    	System.out.println("\n-----Excluir cliente-----");
    	
    	System.out.print("Digite o nome do cliente: ");
    	String name = ScannerUtil.getScanner().nextLine();
    	
    	List<Client> clientes = service.buscarPorNome(name);

    	if (clientes.isEmpty()) {
    	    System.out.println("Nenhum cliente encontrado com o nome: " + name);
    	    return;
    	}

    	System.out.println("\n");
    	for (Client c : clientes) {
    	    System.out.println(exibir(c));
    	}
    	
    	System.out.print("\nDigite o ID do cliente que deseja excluir: ");
    	long id = ScannerUtil.getScanner().nextLong();
    	ScannerUtil.getScanner().nextLine();
    	
    	Client cliente = service.buscarPorId(id);

    	if (cliente == null) {
    	    System.out.println("Cliente não encontrado com o ID: " + id);
    	    return;
    	}
    	
    	System.out.print("\nDigite o CPF para a confirmação da exclusão do cliente " + cliente.getName() + ": ");
    	String cpf = ScannerUtil.getScanner().nextLine();
    	
    	try {
    	    service.validarCpf(cpf);
    	} catch (IllegalArgumentException e) {
    	    System.out.println(e.getMessage());
    	    return;
    	}
    	
    	if (!cliente.getCpf().equals(cpf)) {
    		System.out.println("CPF diferente do cliente que vai ser excluído");
    		return;
    	}
    	
    	try {    
            service.excluir(cliente);
            System.out.println("Cliente excluído com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao excluir o cliente: " + e.getMessage());
        }	
    }
    
    @Override
    public String exibir(Client c) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(String.format("%03d", c.getId())).append(" | ");
        sb.append("Nome: ").append(c.getName()).append(" | ");
        sb.append("CPF: ").append(c.getCpf()).append(" | ");
        sb.append("Email: ").append(c.getEmail()).append(" | ");
        sb.append("Telefone: ").append(c.getTelefone()).append(" | ");
        sb.append("Data de Cadastro: ").append(c.getDataCadastro().format(dtf));
        return sb.toString();
    }
}
