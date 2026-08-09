package view;

import java.util.List;

import entities.Supplier;
import service.SupplierService;
import util.ScannerUtil;

// Classe responsável pela interface de usuário para gestão de fornecedores
public class SupplierView extends BaseView<Supplier>{
	
	// Atributos
	static SupplierService service = new SupplierService();
	
	// Métodos
	@Override
    public void menu() {
        System.out.println("\n-----Gestão de Fornecedores-----");
        System.out.println("1. Cadastrar Fornecedor");
        System.out.println("2. Listar Fornecedor");
        System.out.println("3. Atualizar Fornecedor");
        System.out.println("4. Excluir Fornecedor");
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
        System.out.println("\n-----Cadastrar Fornecedor-----");

        String nome;
        while (true) {
            System.out.print("Digite o nome do fornecedor: ");
            nome = ScannerUtil.getScanner().nextLine();
            try {
                service.validarNome(nome);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        String cnpj;
        while (true) {
            System.out.print("Digite o CNPJ do fornecedor: ");
            cnpj = ScannerUtil.getScanner().nextLine();
            try {
                service.validarCnpj(cnpj);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        String email;
        while (true) {
            System.out.print("Digite o email do fornecedor: ");
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
            System.out.print("Digite o telefone do fornecedor: ");
            telefone = ScannerUtil.getScanner().nextLine();
            try {
                service.validarTelefone(telefone);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        try {
        	Supplier novoFornecedor = new Supplier(nome, cnpj, telefone, email);
            service.salvar(novoFornecedor);
            System.out.println("Fornecedor inserido com sucesso! ID: " + novoFornecedor.getId());
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar o fornecedor: " + e.getMessage());
        }
    }
	
	@Override
	public void listar() {
    	List<Supplier> fornecedores = service.listarTodos();
    	
    	if (fornecedores.isEmpty()) {
            System.out.println("Nenhum fornecedor cadastrado.");
            return;
        }

        System.out.println("\n-----Lista de Fornecedores-----");
        for (Supplier s : fornecedores) {
            System.out.println(exibir(s));
        }
        System.out.println("----------------------");
    }
	
	@Override
	public void atualizar() {
	    System.out.println("-----Atualizar Fornecedor-----");

	    System.out.print("Digite o nome do fornecedor: ");
	    String name = ScannerUtil.getScanner().nextLine();

	    List<Supplier> fornecedores = service.buscarPorNome(name);

	    if (fornecedores.isEmpty()) {
	        System.out.println("Nenhum fornecedor encontrado com o nome: " + name);
	        return;
	    }
	    
	    System.out.println("\n");
	    for (Supplier s : fornecedores) {
	        System.out.println(exibir(s));
	    }

	    System.out.print("\nDigite o ID do fornecedor que deseja atualizar: ");
	    long id = ScannerUtil.getScanner().nextLong();
	    ScannerUtil.getScanner().nextLine();

	    Supplier fornecedor = service.buscarPorId(id);

	    if (fornecedor == null) {
	        System.out.println("Fornecedor não encontrado com o ID: " + id);
	        return;
	    }

	    String nome;
	    while (true) {
	        System.out.print("\nNovo nome (" + fornecedor.getName() + "): ");
	        nome = ScannerUtil.getScanner().nextLine();
	        try { service.validarNome(nome); break; }
	        catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
	    }

	    String cnpj;
	    while (true) {
	        System.out.print("Novo CNPJ (" + fornecedor.getCnpj() + "): ");
	        cnpj = ScannerUtil.getScanner().nextLine();
	        try { service.validarCnpj(cnpj); break; }
	        catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
	    }

	    String email;
	    while (true) {
	        System.out.print("Novo email (" + fornecedor.getEmail() + "): ");
	        email = ScannerUtil.getScanner().nextLine();
	        try { service.validarEmail(email); break; }
	        catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
	    }

	    String telefone;
	    while (true) {
	        System.out.print("Novo telefone (" + fornecedor.getTelefone() + "): ");
	        telefone = ScannerUtil.getScanner().nextLine();
	        try { service.validarTelefone(telefone); break; }
	        catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
	    }

	    fornecedor.setName(nome);
	    fornecedor.setCnpj(cnpj);
	    fornecedor.setEmail(email);
	    fornecedor.setTelefone(telefone);

	    try {
	        service.atualizar(fornecedor);
	        System.out.println("Fornecedor atualizado com sucesso!");
	    } catch (Exception e) {
	        System.err.println("Erro ao atualizar fornecedor: " + e.getMessage());
	    }
	}
    
	
	@Override
    public void excluir() {
    	System.out.println("\n-----Excluir fornecedor-----");
    	
    	System.out.print("Digite o nome do fornecedor: ");
    	String name = ScannerUtil.getScanner().nextLine();
    	
    	List<Supplier> fornecedores = service.buscarPorNome(name);

    	if (fornecedores.isEmpty()) {
    	    System.out.println("Nenhum fornecedor encontrado com o nome: " + name);
    	    return;
    	}

    	System.out.println("\n");
    	for (Supplier f : fornecedores) {
    	    System.out.println(exibir(f));
    	}
    	
    	System.out.print("\nDigite o ID do fornecedor que deseja excluir: ");
    	long id = ScannerUtil.getScanner().nextLong();
    	ScannerUtil.getScanner().nextLine();
    	
    	Supplier fornecedor = service.buscarPorId(id);

    	if (fornecedor == null) {
    	    System.out.println("Fornecedor não encontrado com o ID: " + id);
    	    return;
    	}
    	
    	System.out.print("\nDigite o CNPJ para a confirmação da exclusão do fornecedor " + fornecedor.getName() + ": ");
    	String cnpj = ScannerUtil.getScanner().nextLine();
    	
    	try {
    	    service.validarCnpj(cnpj);
    	} catch (IllegalArgumentException e) {
    	    System.out.println(e.getMessage());
    	    return;
    	}
    	
    	if (!fornecedor.getCnpj().equals(cnpj)) {
    		System.out.println("CNPJ diferente do fornecedor que vai ser excluído");
    		return;
    	}
    	
    	try {          
            service.excluir(fornecedor);
            System.out.println("Fornecedor excluído com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao excluir o fornecedor: " + e.getMessage());
        }	
    }
	
	@Override
	public String exibir(Supplier s) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(String.format("%03d", s.getId())).append(" | ");
        sb.append("Nome: ").append(s.getName()).append(" | ");
        sb.append("CNPJ: ").append(s.getCnpj()).append(" | ");
        sb.append("Email: ").append(s.getEmail()).append(" | ");
        sb.append("Telefone: ").append(s.getTelefone());
        return sb.toString();
    }	
}