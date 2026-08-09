package dao;

import java.util.List;

// Classe abstrata que define os métodos comuns para os DAOs de todas as entidades
public abstract class BaseDAO<E> {
    public abstract boolean inserir(E entidade);
    public abstract List<E> listarTodos();
    public abstract boolean atualizar(E entidade);
    public abstract boolean excluir(long id);
    public abstract E buscarPorId(long id);
    public abstract int contarTotal();
    public abstract boolean existe(long id);
}