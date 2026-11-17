package br.com.fiap.accessplus.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"avaliacao_id", "categoria"}))
public class AvaliacaoCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvaliacaoCategoria;

    @ManyToOne
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaAcessibilidade categoria;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal nota;

    public AvaliacaoCategoria() {
    }

    public Long getIdAvaliacaoCategoria() {
        return idAvaliacaoCategoria;
    }

    public void setIdAvaliacaoCategoria(Long idAvaliacaoCategoria) {
        this.idAvaliacaoCategoria = idAvaliacaoCategoria;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public CategoriaAcessibilidade getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAcessibilidade categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }
}
