package br.com.fiap.accessplus.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "AVALIACAO_CATEGORIA", uniqueConstraints = @UniqueConstraint(columnNames = {"avaliacao_id", "categoria_id"}))
public class AvaliacaoCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_avaliacao_categoria")
    @SequenceGenerator(name = "seq_avaliacao_categoria", sequenceName = "SEQ_AVALIACAO_CATEGORIA", allocationSize = 1)
    @Column(name = "id_avaliacao_categoria")
    private Long idAvaliacaoCategoria;

    @ManyToOne
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaAcessibilidadeEntity categoria;

    @Transient
    private CategoriaAcessibilidade codigoCategoria;

    @Column(name = "nota", nullable = false, precision = 2, scale = 1)
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

    public CategoriaAcessibilidadeEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAcessibilidadeEntity categoria) {
        this.categoria = categoria;
    }

    public CategoriaAcessibilidade getCodigoCategoria() { return categoria != null ? categoria.getCodigo() : codigoCategoria; }
    public void setCategoria(CategoriaAcessibilidade codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
        if (codigoCategoria == null) {
            this.categoria = null;
            return;
        }
        CategoriaAcessibilidadeEntity categoriaTemporaria = new CategoriaAcessibilidadeEntity();
        categoriaTemporaria.setCodigo(codigoCategoria);
        this.categoria = categoriaTemporaria;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }
}
