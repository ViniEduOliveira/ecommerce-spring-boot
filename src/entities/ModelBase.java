package entities;

// Classe abstrata que serve de base para todas as entidades do sistema
public abstract class ModelBase {
	// Atributo
	protected long id;
	
	// Construtor vazio
	public ModelBase() {
	}
	
	// Construtor parametrizado
	public ModelBase(long id) {
		this.setId(id);
	}

	// Getters e Setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}