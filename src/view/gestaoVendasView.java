package view;

import util.ScannerUtil;

// Classe responsável pela interface de usuário para gestão de vendas (pedidos)
public class gestaoVendasView {
	
	// Métodos
	public static void menu() {
		System.out.println("\n-----Gestão de Vendas-----");
        System.out.println("1. Novo pedido");
        System.out.println("2. Listar pedidos");
        System.out.println("3. Gerenciar pedido");
        System.out.println("4. Fechar pedido");
        System.out.println("5. Excluir pedido");
        System.out.println("0. Voltar");
        System.out.print("Digite o número da escolha: ");		
	}
	
	public static int lerOpcao() {
        try { return ScannerUtil.getScanner().nextInt();}
        catch (NumberFormatException e) { return -1; }
    }
    
    public static void processarMenu() {
        int opcao;
        do {
            menu();
            opcao = lerOpcao();
            ScannerUtil.getScanner().nextLine();
            switch (opcao) {
                case 1: new OrderView().cadastrar(); break;
                case 2: new OrderView().listar(); break;
                case 3: new OrderView().gerenciarPedido(); break;
                case 4: new OrderView().atualizarStatus(); break;
                case 5: new OrderView().excluir(); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!"); break;
            }
        } while (opcao != 0);
    }
}