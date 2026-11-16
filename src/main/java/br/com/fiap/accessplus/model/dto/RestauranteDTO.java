package br.com.fiap.accessplus.model.dto;

public class RestauranteDTO {

    private String nome;
    private String bairro;
    private String endereco;
    private String descricao;

    public RestauranteDTO() {
    }
    public RestauranteDTO(String nome, String bairro,
                          String endereco, String descricao) {
        this.nome = nome;
        this.bairro = bairro;
        this.endereco = endereco;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}