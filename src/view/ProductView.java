package view;

import java.util.ArrayList;
import java.util.List;

import entities.Category;
import entities.Product;
import entities.Supplier;
import service.CategoryService;
import service.ProductService;
import service.SupplierService;
import util.ScannerUtil;

// Classe responsável pela interface de usuário para gestão de produtos
public class ProductView extends BaseView<Product>{
    
    // Atributos
    static ProductService service = new ProductService();
    static CategoryService categoriaService = new CategoryService();
    static SupplierService fornecedorService = new SupplierService();

    // Métodos
    @Override
    public void menu() {
        System.out.println("\n-----Gestão de Produtos-----");
        System.out.println("1. Cadastrar Produto");
        System.out.println("2. Listar Produto");
        System.out.println("3. Atualizar Produto");
        System.out.println("4. Excluir Produto");
        System.out.println("5. Gerenciar estoque");
        System.out.println("6. Desvincular Categoria/Fornecedor");
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
                case 5: gerenciarEstoque(); break;
                case 6: desvincular(); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!"); break;
            }
        } while (opcao != 0);
    }

    @Override
    public void cadastrar() {
        System.out.println("\n-----Cadastrar Produto-----");

        String nome;
        while (true) {
            System.out.print("Digite o nome do produto: ");
            nome = ScannerUtil.getScanner().nextLine();
            try {
                service.validarNome(nome);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        String descricao;
        while (true) {
            System.out.print("Digite a descrição do produto: ");
            descricao = ScannerUtil.getScanner().nextLine();
            try {
                service.validarDescri(descricao);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        Double preco;
        while (true) {
            System.out.print("Digite o preço do produto: ");
            try {
                preco = Double.parseDouble(ScannerUtil.getScanner().nextLine().replace(",", "."));
                service.validarPreco(preco);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite um valor numérico válido. Tente novamente.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        Integer qtdEstoque;
        while (true) {
            System.out.print("Digite a quantidade em estoque: ");
            try {
                qtdEstoque = Integer.parseInt(ScannerUtil.getScanner().nextLine());
                service.validarEstoque(qtdEstoque);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite um número inteiro válido. Tente novamente.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }

        List<Category> categoriasSelecionadas = new ArrayList<>();
        System.out.println("\n-----Selecionar Categorias-----");
        new CategoryView().listar();

        while (true) {
            System.out.print("\nDigite o ID da categoria (0 para finalizar): ");
            long idCategoria = ScannerUtil.getScanner().nextLong();
            ScannerUtil.getScanner().nextLine();

            if (idCategoria == 0) {
                if (categoriasSelecionadas.isEmpty()) {
                    System.out.println("Erro: Selecione pelo menos 1 categoria.");
                    continue;
                }
                break;
            }

            Category categoria = categoriaService.buscarPorId(idCategoria);
            if (categoria == null) {
                System.out.println("Categoria não encontrada. Tente novamente.");
                continue;
            }

            categoriasSelecionadas.add(categoria);
            System.out.println("Categoria " + categoria.getName() + " adicionada!");
        }

        List<Supplier> fornecedoresSelecionados = new ArrayList<>();
        System.out.println("\n-----Selecionar Fornecedores-----");
        new SupplierView().listar();

        while (true) {
            System.out.print("\nDigite o ID do fornecedor (0 para finalizar): ");
            long idFornecedor = ScannerUtil.getScanner().nextLong();
            ScannerUtil.getScanner().nextLine();

            if (idFornecedor == 0) {
                if (fornecedoresSelecionados.isEmpty()) {
                    System.out.println("Erro: Selecione pelo menos 1 fornecedor.");
                    continue;
                }
                break;
            }

            Supplier fornecedor = fornecedorService.buscarPorId(idFornecedor);
            if (fornecedor == null) {
                System.out.println("Fornecedor não encontrado. Tente novamente.");
                continue;
            }

            fornecedoresSelecionados.add(fornecedor);
            System.out.println("Fornecedor " + fornecedor.getName() + " adicionado!");
        }

        try {
            Product novoProduto = new Product(nome, descricao, preco, qtdEstoque);

            for (Category c : categoriasSelecionadas)
                novoProduto.addCategoria(c);

            for (Supplier s : fornecedoresSelecionados)
                novoProduto.addFornecedor(s);

            service.salvar(novoProduto);
            System.out.println("Produto cadastrado com sucesso! ID: " + novoProduto.getId());
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    @Override
    public void listar() {
        List<Product> produtos = service.listarTodos();
        
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado");
            return;
        }
        
        System.out.println("\n-----Lista de Produtos-----");
        for (Product p : produtos) {
            System.out.println(exibir(p));
        }   
    }
    
    @Override
    public void atualizar() {
        System.out.println("\n-----Atualizar Produto-----");

        System.out.print("Digite o nome do produto: ");
        String name = ScannerUtil.getScanner().nextLine();

        List<Product> produtos = service.buscarPorNome(name);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado com o nome: " + name);
            return;
        }

        for (Product p : produtos) {
            System.out.println(exibir(p));
        }

        System.out.print("\nDigite o ID do produto que deseja atualizar: ");
        long id = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Product produto = service.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado com o ID: " + id);
            return;
        }
        
        String nome;
        while (true) {
            System.out.print("\nNovo nome (" + produto.getName() + "): ");
            nome = ScannerUtil.getScanner().nextLine();
            
            if (nome.trim().isEmpty()) {
                nome = produto.getName();
                break;
            }
            
            try { service.validarNome(nome); break; }
            catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }

        String descricao;
        while (true) {
            System.out.print("Nova descrição (" + produto.getDescricao() + "): ");
            descricao = ScannerUtil.getScanner().nextLine();
            
            if (descricao.trim().isEmpty()) {
                descricao = produto.getDescricao();
                break;
            }
            
            try { service.validarDescri(descricao); break; }
            catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }

        Double preco;
        while (true) {
            System.out.print("Novo preço (" + produto.getPreco() + "): ");
            String inputPreco = ScannerUtil.getScanner().nextLine();
            
            if (inputPreco.trim().isEmpty()) {
                preco = produto.getPreco();
                break;
            }
            
            try { 
                preco = Double.parseDouble(inputPreco.replace(",", "."));
                service.validarPreco(preco); break; 
            }
            catch (NumberFormatException e) { System.out.println("Erro: Digite um valor numérico válido."); }
            catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }
        
        produto.setName(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);

        try {
            service.atualizar(produto);
            System.out.println("Produto atualizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    @Override
    public void excluir() {
        System.out.println("\n-----Excluir Produto-----");

        System.out.print("Digite o nome do produto: ");
        String nome = ScannerUtil.getScanner().nextLine();

        List<Product> produtos = service.buscarPorNome(nome);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado com o nome: " + nome);
            return;
        }

        for (Product p : produtos)
            System.out.println(exibir(p));

        System.out.print("Digite o ID do produto que deseja excluir: ");
        long idProduto = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Product produto = service.buscarPorId(idProduto);

        if (produto == null) {
            System.out.println("Produto não encontrado com o ID: " + idProduto);
            return;
        }

        System.out.println(exibir(produto));

        System.out.print("Digite o SKU para confirmar a exclusão do produto " + produto.getName() + ": ");
        String confirmacaoSku = ScannerUtil.getScanner().nextLine();

        if (!confirmacaoSku.equals(produto.getSku())) {
            System.out.println("SKU incorreto. Operação cancelada.");
            return;
        }

        try {
            service.excluir(produto);
            System.out.println("Produto excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado ao excluir: " + e.getMessage());
        }
    }
    
    @Override
    public String exibir(Product p) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(String.format("%03d", p.getId())).append(" | ");
        sb.append("Nome: ").append(p.getName()).append(" | ");
        sb.append("Descrição: ").append(p.getDescricao() != null ? p.getDescricao() : "Sem descrição").append(" | ");
        sb.append("Preço: R$ ").append(String.format("%.2f", p.getPreco())).append(" | ");
        sb.append("Estoque: ").append(p.getQtdEstoque()).append(" | ");
        sb.append("SKU: ").append(p.getSku()).append(" | ");

        sb.append("Categorias: ");
        if (p.getCategorias().isEmpty()) {
            sb.append("Nenhuma");
        } else {
            for (Category c : p.getCategorias())
                sb.append(c.getName()).append(" | ");
        }
        sb.append("\n");
        
        sb.append("Fornecedores: ");
        if (p.getFornecedores().isEmpty()) {
            sb.append("Nenhum");
        } else {
            for (Supplier s : p.getFornecedores())
                sb.append(s.getName()).append(" ");
        }
        return sb.toString();
    }
    
    
    public void gerenciarEstoque() {
        System.out.println("\n----- Gerenciar Estoque -----");

        System.out.print("Digite o nome do produto: ");
        String name = ScannerUtil.getScanner().nextLine();

        List<Product> produtos = service.buscarPorNome(name);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado com o nome: " + name);
            return;
        }

        for (Product p : produtos) {
            System.out.println(exibir(p));
        }

        System.out.print("Digite o ID do produto para movimentar o estoque: ");
        long id = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Product produto = service.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado com o ID: " + id);
            return;
        }

        System.out.println("\nProduto selecionado: " + produto.getName() + " | Estoque atual: " + produto.getQtdEstoque());
        System.out.println("1 - Entrada de Estoque (Adicionar)");
        System.out.println("2 - Saída de Estoque (Baixar)");
        System.out.print("Escolha a operação: ");
        int opcao = ScannerUtil.getScanner().nextInt();
        ScannerUtil.getScanner().nextLine();

        if (opcao != 1 && opcao != 2) {
            System.out.println("Opção inválida. Operação cancelada.");
            return;
        }

        System.out.print("Digite a quantidade: ");
        int quantidade = ScannerUtil.getScanner().nextInt();
        ScannerUtil.getScanner().nextLine();

        try {
            if (opcao == 1) {
                service.adicionarEstoque(produto, quantidade);
                System.out.println("Entrada registrada com sucesso! Novo saldo: " + produto.getQtdEstoque());
            } else {
                service.baixarEstoque(produto, quantidade);
                System.out.println("Saída registrada com sucesso! Novo saldo: " + produto.getQtdEstoque());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nErro inesperado ao atualizar estoque: " + e.getMessage());
        }
    }
    
    public void desvincular() {
        System.out.print("Digite o ID do produto: ");
        long idProduto = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();
        
        Product produto = null;
        for (Product p : service.listarTodos()) {
            if (p.getId() == idProduto) {
                produto = p;
                break;
            }
        }

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println(exibir(produto));
        
        System.out.println("O que deseja desvincular?");
        System.out.println("1 - Fornecedor");
        System.out.println("2 - Categoria");
        System.out.print("Opção: ");
        int escolha = ScannerUtil.getScanner().nextInt();
        ScannerUtil.getScanner().nextLine();
        
        try {
            if (escolha == 1) {
                System.out.print("Digite o ID do fornecedor para desvincular: ");
                long idFornecedor = ScannerUtil.getScanner().nextLong();
                ScannerUtil.getScanner().nextLine();
                
                service.removerFornecedor(produto, idFornecedor);
                System.out.println("Fornecedor desvinculado com sucesso!");
                
            } else if (escolha == 2) {
                System.out.print("Digite o ID da categoria para desvincular: ");
                long idCategoria = ScannerUtil.getScanner().nextLong();
                ScannerUtil.getScanner().nextLine();
                
                service.removerCategoria(produto, idCategoria);
                System.out.println("Categoria desvinculada com sucesso!");
                
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }  
}