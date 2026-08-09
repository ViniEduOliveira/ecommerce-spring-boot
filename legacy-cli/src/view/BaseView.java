package view;

import java.util.InputMismatchException;

import util.ScannerUtil;

// Classe abstrata que define a interface básica para as views
public abstract class BaseView<E> {

	// Métodos abstratos
	public abstract void menu();
	
    // Método para ler opção do menu
    public static int lerOpcao() {
        try { return ScannerUtil.getScanner().nextInt(); }
        catch (InputMismatchException e) {
            ScannerUtil.getScanner().nextLine();
            return -1;
        }
    }
    
    // Método para processar o menu principal
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

    
    // Métodos abstratos para operações CRUD
    public abstract void cadastrar();
    public abstract void listar();
    public abstract void atualizar();
    public abstract void excluir();
    
    // Método abstrato para exibir entidade
    public abstract String exibir(E entidade);
    

}
