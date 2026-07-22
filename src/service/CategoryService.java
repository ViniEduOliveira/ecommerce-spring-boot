package service;

import java.util.List;
import dao.CategoryDAO;
import entities.Category;
import entities.enuns.CategoryType;

// Serviço para gerenciar as regras de negócio da entidade Categoria
public class CategoryService extends BaseService<Category> {
    private CategoryDAO dao = new CategoryDAO();

    // Métodos

    @Override
    public void salvar(Category categoria) {
        if (categoria.getName() == null)
            throw new IllegalArgumentException("Categoria não pode ser nula");

        validarDescri(categoria.getDescricao());

        if (!dao.inserir(categoria))
            throw new RuntimeException("Erro ao cadastrar categoria no banco");
    }
    
    // Método para listar todas as categorias
    @Override
    public List<Category> listarTodos() {
        return dao.listarTodos();
    }
    
    // Método para atualizar uma categoria existente
    @Override
    public void atualizar(Category categoria) {
        if (categoria == null)
            throw new IllegalArgumentException("Categoria não encontrada");

        validarDescri(categoria.getDescricao());
        dao.atualizar(categoria);
    }

    // Método para excluir categoria após validacoes
    @Override
    public void excluir(Category categoria) {
        if (categoria == null)
            throw new IllegalArgumentException("Categoria não encontrada");

        if (categoria.getProduto() != null && !categoria.getProduto().isEmpty())
            throw new IllegalArgumentException("Categoria possui produtos vinculados e não pode ser excluída");

        dao.excluir(categoria.getId());
    }
  
    // Método para buscar categoria por ID
    @Override
    public Category buscarPorId(long id) {
        return dao.buscarPorId(id);
    }

    // Método para inicializar categorias padrão no banco
    public void inicializarCategorias() {
        for (CategoryType tipo : CategoryType.values()) {
            if (!dao.existePorTipo(tipo)) {
                Category categoria = new Category(tipo, "");
                dao.inserir(categoria);
            }
        }
    }

    // Método para validar tamanho da descrição da categoria
    public void validarDescri(String descri) {
        if (descri != null && descri.length() > 300)
            throw new IllegalArgumentException("A descrição não pode ultrapassar 300 caracteres");
    }
}