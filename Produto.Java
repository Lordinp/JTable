package model;

public class Produto {
    private int id;
    private String nome;
    private float valorUnitario;
    private int qtdEstoque;

    public Produto() {}

    public Produto(int id, String nome, float valorUnitario, int qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.valorUnitario = valorUnitario;
        this.qtdEstoque = qtdEstoque;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public float getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(float valorUnitario) { this.valorUnitario = valorUnitario; }

    public int getQtdEstoque() { return qtdEstoque; }
    public void setQtdEstoque(int qtdEstoque) { this.qtdEstoque = qtdEstoque; }
}