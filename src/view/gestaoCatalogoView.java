package view;

import util.ScannerUtil;

// Classe responsável pela interface de usuário para gestão do catálogo (categorias e produtos)
public class gestaoCatalogoView {
	
	// Métodos
	public static void menu() {
		System.out.println("\n-----Gestão de Catálogo-----");
        System.out.println("1. Menu Categoria");
        System.out.println("2. Menu Produto");
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
                case 1: new CategoryView().processarMenu(); break;
                case 2: new ProductView().processarMenu(); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!"); break;
            }
        } while (opcao != 0);
    }

}
