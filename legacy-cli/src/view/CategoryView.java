package view;

import java.util.List;
import entities.Category;
import service.CategoryService;
import util.ScannerUtil;

// Classe responsável pela interface de usuário para gestão de categorias
public class CategoryView extends BaseView<Category>{

    // Atributos
    static CategoryService service = new CategoryService();

    // Métodos
    @Override
    public void menu() {
        System.out.println("\n-----Gestão de Categorias-----");
        System.out.println("1. Listar Categorias");
        System.out.println("2. Atualizar Descrição");
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
                case 1: listar(); break;
                case 2: atualizar(); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!"); break;
            }
        } while (opcao != 0);
    }

    @Override
	public void cadastrar() {}
    
    @Override
    public void listar() {
        List<Category> categorias = service.listarTodos();

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
            return;
        }

        System.out.println("\n-----Lista de Categorias-----");
        for (Category c : categorias) {
            System.out.println(exibir(c));
        }
    }

    @Override
    public void atualizar() {
        System.out.println("\n-----Atualizar Categoria-----");
        
        List<Category> categorias = service.listarTodos();
        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
            return;
        }
        
        listar();

        System.out.print("\nDigite o ID da categoria que deseja atualizar: ");
        long id = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Category categoria = service.buscarPorId(id);

        if (categoria == null) {
            System.out.println("Categoria não encontrada com o ID: " + id);
            return;
        }

        String descricao;
        while (true) {
            System.out.print("\nNova descrição (" + categoria.getDescricao() + "): ");
            descricao = ScannerUtil.getScanner().nextLine();
            try {
                service.validarDescri(descricao);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        categoria.setDescricao(descricao);

        try {
            service.atualizar(categoria);
            System.out.println("Categoria atualizada com sucesso!" + categoria.getId());
        } catch (Exception e) {
            System.err.println("Erro ao atualizar categoria: " + e.getMessage());
        }
    }
    
    @Override
	public void excluir() {}

    @Override
    public String exibir(Category c) {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------\n");
        sb.append("ID: ").append(String.format("%03d", c.getId())).append(" | ");
        sb.append("Categoria: ").append(c.getName()).append(" | ");
        sb.append("Descrição: ").append(c.getDescricao() != null ? c.getDescricao() : "Sem descrição");
        return sb.toString();
    }
}