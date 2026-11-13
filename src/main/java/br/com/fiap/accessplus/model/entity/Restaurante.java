package br.com.fiap.accessplus.model.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Restaurante {
    private Long idRestaurante;
    private String nome;
    private String endereco;
    private String bairro;
    private String descricao;
    private boolean publicado;
    private boolean ativo;
    @ManyToOne
    private Restaurante restauranteAnterior;
}
