package br.com.fiap.accessplus.model.entity;
import jakarta.persistence.*;

@Entity
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRestaurante;
    private String nome;
    private String endereco;
    private String bairro;
    private String descricao;
    private boolean publicado;
    private boolean ativo;
    @ManyToOne
    private Restaurante restauranteAnterior;

    public Restaurante() {
    }

    public Restaurante(String nome, String endereco, String bairro, String descricao, boolean publicado, boolean ativo, Restaurante restauranteAnterior) {
        this.nome = nome;
        this.endereco = endereco;
        this.bairro = bairro;
        this.descricao = descricao;
        this.publicado = publicado;
        this.ativo = ativo;
        this.restauranteAnterior = restauranteAnterior;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Restaurante getRestauranteAnterior() {
        return restauranteAnterior;
    }

    public void setRestauranteAnterior(Restaurante restauranteAnterior) {
        this.restauranteAnterior = restauranteAnterior;
    }
}
