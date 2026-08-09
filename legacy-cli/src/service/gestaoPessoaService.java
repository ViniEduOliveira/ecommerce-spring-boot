package service;

// Classe de exibição para menu de gestão de pessoas
public class gestaoPessoaService {

    // Método para exibir menu principal de pessoas
    public static void menuPrincipal() {
        System.out.println("-----Gestão de Pessoas-----");
        System.out.println("1. Menu Cliente");
        System.out.println("2. Menu Fornecedor");
        System.out.println("0. Voltar");
        System.out.print("Digite o número da escolha:");
    }



    public static void subMenuFornecedor() {
        System.out.println("-----Gestão Fornecedores-----");
        System.out.println("1. Cadastrar Fornecedor");
        System.out.println("2. Listar Fornecedor");
        System.out.println("3. Atualizar Fornecedor");
        System.out.println("0. Voltar");
        System.out.print("Digite o número da escolha:");
    }


}
