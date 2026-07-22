CREATE DATABASE ecommerce_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;


-- Configuração inicial para o schema padrão
SET search_path TO public;

-- Criação da tabela Cliente
CREATE TABLE IF NOT EXISTS clientes (
	ID SERIAL PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	CPF VARCHAR(11) UNIQUE NOT NULL,
	Email VARCHAR(100) UNIQUE NOT NULL,
	telefone VARCHAR(11),
	Data_Criacao TIMESTAMP NOT NULL
);

-- Criação da tabela Produto
CREATE TABLE IF NOT EXISTS produtos (
	ID SERIAL PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	descricao VARCHAR(300),
	Preco NUMERIC(10,2) NOT NULL,
	Quantidade_estoque INTEGER NOT NULL,
	SKU VARCHAR(50) UNIQUE NOT NULL
);

-- Criação da tabela Pedido
CREATE TABLE IF NOT EXISTS pedidos (
	ID SERIAL PRIMARY KEY,
	Data_criacao TIMESTAMP NOT NULL,
	Valor_total NUMERIC(10,2) NOT NULL,
	Status VARCHAR(50) NOT NULL,
	ID_Cliente INTEGER NOT NULL,
	CONSTRAINT fk_pedido_cliente FOREIGN KEY (ID_Cliente) REFERENCES clientes(ID)
);

-- Criação da tabela ItemPedido
CREATE TABLE IF NOT EXISTS itens_pedido (
	ID SERIAL PRIMARY KEY,
	quantidade INTEGER NOT NULL,
	Preco_produto NUMERIC (10,2) NOT NULL,
	ID_Pedido INTEGER NOT NULL,
	ID_Produto INTEGER NOT NULL,
	CONSTRAINT fk_itempedido_pedido FOREIGN KEY (ID_Pedido) REFERENCES pedidos(ID),
	CONSTRAINT fk_itempedido_produto FOREIGN KEY (ID_Produto) REFERENCES produtos(ID)
);

-- Criação da tabela Fornecedor
CREATE TABLE IF NOT EXISTS fornecedores (
	ID SERIAL PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	CNPJ VARCHAR(14) UNIQUE NOT NULL,
	telefone VARCHAR(11) NOT NULL,
	Email VARCHAR(100) UNIQUE NOT NULL
);

-- Criação da tabela Categoria
CREATE TABLE IF NOT EXISTS categorias (
	ID SERIAL PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	descricao VARCHAR(300)
);

-- Criação da tabela Fornecedor_Produto
CREATE TABLE IF NOT EXISTS fornecedor_produto (
	ID_For_Prod SERIAL PRIMARY KEY,
	ID_Fornecedor INTEGER NOT NULL,
	ID_Produto INTEGER NOT NULL,
	CONSTRAINT fk_fornprod_fornecedor FOREIGN KEY (ID_Fornecedor) REFERENCES fornecedores(ID),
	CONSTRAINT fk_fornprod_produto FOREIGN KEY (ID_Produto) REFERENCES produtos(ID)
);

-- Criação da tabela Categoria_Produto
CREATE TABLE IF NOT EXISTS categoria_produto (
	ID_Cat_Prod SERIAL PRIMARY KEY,
	ID_Categoria INTEGER NOT NULL,
	ID_Produto INTEGER NOT NULL,
	CONSTRAINT fk_catprod_categoria FOREIGN KEY (ID_Categoria) REFERENCES categorias(ID),
	CONSTRAINT fk_catprod_produto FOREIGN KEY (ID_Produto) REFERENCES produtos(ID)
);