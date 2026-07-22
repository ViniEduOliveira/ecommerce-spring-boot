package view;

import java.util.List;

import entities.Client;
import entities.Order;
import entities.OrderItem;
import entities.Product;
import entities.enuns.OrderStatus;
import service.ClientService;
import service.OrderItemService;
import service.OrderService;
import service.ProductService;
import util.ScannerUtil;

// Classe responsável pela interface de usuário para gestão de pedidos
public class OrderView extends BaseView<Order>{

    // Atributos
    private static OrderService service = new OrderService();
    private static OrderItemService itemService = new OrderItemService();
    private static ClientService clienteService = new ClientService();
    private static ProductService productService = new ProductService();

    // Métodos
    @Override
    public void menu() {
        System.out.println("\n----- Gestão de Pedidos -----");
        System.out.println("1. Abrir novo pedido");
        System.out.println("2. Listar todos os pedidos");
        System.out.println("3. Gerenciar pedido existente");
        System.out.println("4. Excluir pedido");
        System.out.println("5. Atualizar status do pedido");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
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
                case 3: gerenciarPedido(); break;
                case 4: excluir(); break;
                case 5: atualizarStatus(); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!"); break;
            }
        } while (opcao != 0);
    }

    @Override
    public void cadastrar() {
        System.out.println("\n-----Abrir Novo Pedido-----");

        System.out.print("Digite o nome do cliente: ");
        String nomeCliente = ScannerUtil.getScanner().nextLine();

        List<Client> clientes = clienteService.buscarPorNome(nomeCliente);
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado com o nome: " + nomeCliente);
            return;
        }

        for (Client c : clientes)
            System.out.println("ID: " + String.format("%03d", c.getId()) + " | Nome: " + c.getName() + " | CPF: " + c.getCpf());

        System.out.print("Digite o ID do cliente: ");
        long idCliente = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Client clienteSelecionado = clienteService.buscarPorId(idCliente);
        if (clienteSelecionado == null) {
            System.out.println("Cliente não encontrado com o ID: " + idCliente);
            return;
        }

        try {
            Order novoPedido = new Order(clienteSelecionado);
            service.salvar(novoPedido);
            System.out.println("Pedido aberto com sucesso! ID: " + novoPedido.getId());
        } catch (Exception e) {
            System.err.println("Erro ao abrir pedido: " + e.getMessage());
        }
    }

    @Override
    public void listar() {
        System.out.println("\n-----Todos os Pedidos-----");

        List<Order> pedidos = service.listarTodos();
        if (pedidos == null || pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }

        for (Order p : pedidos)
            System.out.println("Pedido #" + String.format("%03d", p.getId())
                   	+ " | Status: " + p.getStatus()
                    + " | Cliente: " + p.getCliente().getName()
                    + " | Total: R$ " + String.format("%.2f", p.valorTotal()));
    }
    
    @Override public void atualizar() {}

    
    @Override
    public void excluir() {
        System.out.println("\n-----Excluir Pedido-----");
        System.out.print("Digite o ID do pedido: ");
        long idPedido = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Order pedido = service.buscarPorId(idPedido);
        if (pedido == null) {
            System.out.println("Pedido não encontrado com o ID: " + idPedido);
            return;
        }

        System.out.println(exibir(pedido));
        System.out.print("Confirme digitando o ID do pedido novamente: ");
        long confirmacao = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        if (confirmacao != pedido.getId()) {
            System.out.println("ID incorreto. Operação cancelada.");
            return;
        }

        try {
            service.excluir(pedido);
            System.out.println("Pedido excluído com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String exibir(Order p) {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------\n");
        sb.append("Pedido ID: ").append(String.format("%03d", p.getId())).append("\n");
        sb.append("Data: ").append(p.getDataCriacao() != null ? p.getDataCriacao() : "Sem Data").append("\n");
        sb.append("Status: ").append(p.getStatus() != null ? p.getStatus() : "Sem Status").append("\n");

        if (p.getCliente() != null)
            sb.append("Cliente: ").append(p.getCliente().getName()).append(" (CPF: ").append(p.getCliente().getCpf()).append(")\n");
        else
            sb.append("Cliente: Não vinculado\n");

        sb.append("Total: R$ ").append(String.format("%.2f", p.valorTotal())).append("\n");
        sb.append("---------------------------");

        if (p.getItens() != null && !p.getItens().isEmpty()) {
            sb.append("\nItens:\n");
            for (OrderItem item : p.getItens()) {
                sb.append(" - ").append(item.getProduto().getName())
                  .append(" | Qtd: ").append(item.getQuantidade())
                  .append(" | Preço Unit: R$ ").append(String.format("%.2f", item.getPrecoProduto()))
                  .append(" | Subtotal: R$ ").append(String.format("%.2f", item.subtotal()))
                  .append("\n");
            }
            sb.append("---------------------------");
        }

        return sb.toString();
    }

    
    public void gerenciarPedido() {
        System.out.println("\n----- Gerenciar Pedido -----");
        System.out.print("Digite o ID do pedido: ");
        long idPedido = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Order pedido = service.buscarPorId(idPedido);
        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            return;
        }

        int opcaoSub;
        do {
            System.out.println("\n" + exibir(pedido));
            System.out.println("\n--- Submenu Pedido #" + String.format("%03d", pedido.getId()) + " ---");
            System.out.println("1. Adicionar produto");
            System.out.println("2. Alterar quantidade");
            System.out.println("3. Remover produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcaoSub = lerOpcao();
            ScannerUtil.getScanner().nextLine();

            switch (opcaoSub) {
                case 1: adicionarItem(pedido); break;
                case 2: alterarQuantidadeItem(pedido); break;
                case 3: removerItem(pedido); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!"); break;
            }
        } while (opcaoSub != 0);
    }

    private void adicionarItem(Order pedido) {
        System.out.print("Digite o nome do produto: ");
        String nomeProd = ScannerUtil.getScanner().nextLine();
        List<Product> produtos = productService.buscarPorNome(nomeProd);

        if (produtos.isEmpty()) {
            System.out.println("Produto não encontrado.");
            return;
        }

        for (Product p : produtos)
            System.out.println("ID: " + String.format("%03d", p.getId()) + " | " + p.getName() + " | Preço: R$ " + String.format("%.2f", p.getPreco()) + " | Estoque: " + p.getQtdEstoque());

        System.out.print("Digite o ID do produto: ");
        long idProd = ScannerUtil.getScanner().nextLong();
        System.out.print("Digite a quantidade: ");
        int qtd = ScannerUtil.getScanner().nextInt();
        ScannerUtil.getScanner().nextLine();

        Product produto = productService.buscarPorId(idProd);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        try {
            OrderItem item = new OrderItem(qtd, produto.getPreco(), produto, pedido);
            itemService.adicionarNovoItem(pedido, item);
            System.out.println("Produto adicionado ao carrinho!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void alterarQuantidadeItem(Order pedido) {
        if (pedido.getItens().isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return;
        }

        System.out.print("Digite o ID do produto: ");
        long idProd = ScannerUtil.getScanner().nextLong();
        System.out.print("Nova quantidade: ");
        int novaQtd = ScannerUtil.getScanner().nextInt();
        ScannerUtil.getScanner().nextLine();

        try {
            Product produto = productService.buscarPorId(idProd);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }
            itemService.atualizarQuantidadeItem(pedido, idProd, novaQtd, produto.getQtdEstoque());
            System.out.println("Quantidade atualizada!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removerItem(Order pedido) {
        if (pedido.getItens().isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return;
        }

        System.out.print("Digite o ID do produto que deseja remover: ");
        long idProd = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        try {
            itemService.removerItem(pedido, idProd);
            System.out.println("Produto removido do carrinho!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }    
    
    public void atualizarStatus() {
        System.out.println("\n-----Atualizar Status do Pedido-----");
        System.out.print("Digite o ID do pedido: ");
        long idPedido = ScannerUtil.getScanner().nextLong();
        ScannerUtil.getScanner().nextLine();

        Order pedido = service.buscarPorId(idPedido);
        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            return;
        }

        System.out.println("Status atual: " + pedido.getStatus());
        System.out.println("Escolha o novo status:");
        OrderStatus[] statuses = OrderStatus.values();
        for (int i = 0; i < statuses.length; i++)
            System.out.println((i + 1) + ". " + statuses[i]);

        System.out.print("Digite o número: ");
        int opcao = lerOpcao();
        ScannerUtil.getScanner().nextLine();

        if (opcao < 1 || opcao > statuses.length) {
            System.out.println("Opção inválida!");
            return;
        }

        try {
            service.atualizarStatus(pedido, statuses[opcao - 1]);
            System.out.println("Status atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}