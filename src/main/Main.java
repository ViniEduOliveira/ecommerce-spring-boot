package main;

import java.util.Scanner;

import dao.ConexaoBD;
import service.CategoryService;
import view.gestaoCatalogoView;
import view.gestaoPessoasView;
import view.gestaoVendasView;

// Classe principal para iniciar a aplicação
public class Main {

    public static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        ConexaoBD.testarConexao();
        new CategoryService().inicializarCategorias();
        
        int opcaoEntrada;

        do {
            menuPrincipal();
            opcaoEntrada = lerOpcao();
            processarOpcaoEntrada(opcaoEntrada);
        } while(opcaoEntrada != 0);

    }

    public static int lerOpcao() {
        try { return Integer.parseInt(sc.nextLine()); }
        catch (NumberFormatException e) { return -1; }
    }

    public static void menuPrincipal() {
        System.out.println("\n-------E-COMMERCE------");
        System.out.println("1. Gestão de Pessoas");
        System.out.println("2. Gestão de Catálogo");
        System.out.println("3. Gestão de Vendas");
        System.out.println("0. Encerrar Programa");
        System.out.print("Digite o número da escolha: ");
    }

    public static void processarOpcaoEntrada(int opcao) {
        switch (opcao) {
            case 1:
            	gestaoPessoasView.processarMenu();
                break;
            case 2:
               gestaoCatalogoView.processarMenu();
               break;
            case 3: 
            	gestaoVendasView.processarMenu();
            	break;
            case 0:
            	System.out.println("Encerrando o servidor..."); break;
            default:
                break;
        }
    }
}