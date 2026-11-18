package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.Restaurante;

public class RestauranteRespostaDTO {
    private Long idRestaurante;
    private String nome;
    private String bairro;
    private String endereco;
    private String descricao;
    private boolean ativo;
    private boolean publicado;
    private String cidade;
    private String telefone;
    private Long idResponsavel;

    public RestauranteRespostaDTO() {
    }

    public Long getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(Long idRestaurante) {
        this.idRestaurante = idRestaurante;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public Long getIdResponsavel() { return idResponsavel; }
    public void setIdResponsavel(Long idResponsavel) { this.idResponsavel = idResponsavel; }

    public static RestauranteRespostaDTO from(Restaurante restaurante) {
        RestauranteRespostaDTO resposta = new RestauranteRespostaDTO();
        resposta.setIdRestaurante(restaurante.getIdRestaurante());
        resposta.setNome(restaurante.getNome());
        resposta.setBairro(restaurante.getBairro());
        resposta.setEndereco(restaurante.getEndereco());
        resposta.setDescricao(restaurante.getDescricao());
        resposta.setAtivo(restaurante.isAtivo());
        resposta.setPublicado(restaurante.isPublicado());
        resposta.setCidade(restaurante.getCidade());
        resposta.setTelefone(restaurante.getTelefone());
        resposta.setIdResponsavel(restaurante.getResponsavel() == null ? null : restaurante.getResponsavel().getIdUsuario());
        return resposta;
    }
}
