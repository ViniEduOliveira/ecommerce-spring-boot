package service;

import java.util.List;

// Classe abstrata que define a interface básica para os serviços
public abstract class BaseService<E> {
	public abstract void salvar(E entidade);
	public abstract List<E> listarTodos();
	public abstract void atualizar(E entidade);
	public abstract void excluir(E entidade);
	public abstract E buscarPorId(long id);
}