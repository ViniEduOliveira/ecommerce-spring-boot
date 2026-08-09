package service;

import java.util.List;
import java.util.Random;

import dao.ProductDAO;
import entities.Category;
import entities.Product;
import entities.Supplier;

// Serviço para gerenciar as regras de negócio da entidade Produto
public class ProductService extends BaseService<Product> {
    private ProductDAO dao = new ProductDAO();

    // Métodos

    @Override
    public void salvar(Product produto) {
        validarNome(produto.getName());
        validarDescri(produto.getDescricao());
        validarPreco(produto.getPreco());
        validarEstoque(produto.getQtdEstoque());

        if (produto.getFornecedores() == null || produto.getFornecedores().isEmpty())
            throw new IllegalArgumentException("Erro: O produto deve ter pelo menos 1 fornecedor vinculado.");

        if (produto.getCategorias() == null || produto.getCategorias().isEmpty())
            throw new IllegalArgumentException("Erro: O produto deve ter pelo menos 1 categoria vinculada.");

        produto.setSku(gerarSkuAleatorio());
        dao.inserir(produto);
    }
    
    // Método para listar todos os produtos
    @Override
    public List<Product> listarTodos() {
        return dao.listarTodos();
    }

    // Método para atualizar produto existente
    @Override
    public void atualizar(Product produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Erro: Produto não encontrado");
        }

        validarNome(produto.getName());
        validarDescri(produto.getDescricao());
        validarPreco(produto.getPreco());

        dao.atualizar(produto);
    }
    
    // Método para excluir produto após validações
    @Override
    public void excluir(Product produto) {
        if (produto == null)
            throw new IllegalArgumentException("Erro: Produto não encontrado.");

        if (dao.temPedidosEmAndamento(produto.getId()))
            throw new IllegalArgumentException("Erro: O produto não pode ser excluído pois está em um pedido em andamento.");

        for (int i = 0; i < produto.getFornecedores().size(); i++)
            dao.desvincularFornecedor(produto.getId(), produto.getFornecedores().get(i).getId());

        for (int i = 0; i < produto.getCategorias().size(); i++)
            dao.desvincularCategoria(produto.getId(), produto.getCategorias().get(i).getId());

        dao.excluirItensPorProduto(produto.getId()); // remove itens de pedidos EM_ABERTO e CANCELADO

        dao.excluir(produto.getId());
    }
    
    // Método para buscar produto por ID
    @Override
    public Product buscarPorId(long id) {
	    return dao.buscarPorId(id);
	}

    
    // Método para buscar produtos por nome
    public List<Product> buscarPorNome(String nome) {
	    return dao.buscarPorNome(nome);
	}
    
    // Método para remover fornecedor do produto
    public void removerFornecedor(Product produto, long idFornecedor) {
        if (produto.getFornecedores().size() <= 1) {
            throw new IllegalArgumentException("Erro: O produto deve ter pelo menos 1 fornecedor vinculado.");
        }
        
        boolean removidoNoBanco = dao.desvincularFornecedor(produto.getId(), idFornecedor);
        
        if (removidoNoBanco) {
            for (int i = 0; i < produto.getFornecedores().size(); i++) {
                Supplier fornecedor = produto.getFornecedores().get(i);
                if (fornecedor.getId() == idFornecedor) {
                    produto.removeFornecedor(fornecedor);
                    break; 
                }
            }
        }
    }

    // Método para remover categoria do produto
    public void removerCategoria(Product produto, long idCategoria) {
        if (produto.getCategorias().size() <= 1) {
            throw new IllegalArgumentException("Erro: O produto deve ter pelo menos 1 categoria vinculada.");
        }
        
        boolean removidoNoBanco = dao.desvincularCategoria(produto.getId(), idCategoria);
        
        if (removidoNoBanco) {
            for (int i = 0; i < produto.getCategorias().size(); i++) {
                Category categoria = produto.getCategorias().get(i);
                if (categoria.getId() == idCategoria) {
                    produto.removeCategoria(categoria);
                    break;
                }
            }
        }
    }
    
    // Método para adicionar estoque ao produto
    public void adicionarEstoque(Product produto, int qtd) {
        if (qtd <= 0)
            throw new IllegalArgumentException("Erro: Informe uma quantidade positiva para a entrada");

        produto.addQtdEstoque(qtd);
        dao.atualizarEstoque(produto.getId(), produto.getQtdEstoque());
    }

    // Método para baixar estoque do produto
    public void baixarEstoque(Product produto, int qtd) {
        if (qtd <= 0)
            throw new IllegalArgumentException("Erro: Quantidade inválida");

        if (qtd > produto.getQtdEstoque())
            throw new IllegalArgumentException("Erro: Estoque insuficiente! Saldo atual: " + produto.getQtdEstoque());

        produto.removeQtdEstoque(qtd);
        dao.atualizarEstoque(produto.getId(), produto.getQtdEstoque());
    }

    // Método para validar nome do produto
    public void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3)
            throw new IllegalArgumentException("Erro: o nome deve ter pelo menos 3 caracteres");
    }

    // Método para validar descrição do produto
    public void validarDescri(String descri) {
        if (descri != null && descri.length() > 300)
            throw new IllegalArgumentException("Erro: A descrição não pode ultrapassar 300 caracteres");
    }

    // Método para validar preço do produto
    public void validarPreco(Double preco) {
        if (preco == null || preco <= 0)
            throw new IllegalArgumentException("Erro: O preço tem que ser maior que 0");
    }

    // Método para validar estoque do produto
    public void validarEstoque(Integer estoque) {
        if (estoque == null || estoque < 0)
            throw new IllegalArgumentException("Erro: O estoque não pode ser negativo");
    }

    // Método auxiliar para gerar SKU aleatório
    private String gerarSkuAleatorio() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder skuGerado = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            skuGerado.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return skuGerado.toString();
    }
}