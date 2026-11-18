package br.com.fiap.accessplus.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AVALIACAO")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_avaliacao")
    @SequenceGenerator(name = "seq_avaliacao", sequenceName = "SEQ_AVALIACAO", allocationSize = 1)
    @Column(name = "id_avaliacao")
    private Long idAvaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nota", nullable = false, precision = 2, scale = 1)
    private BigDecimal notaGeral;

    @Column(name = "comentario") private String comentario;

    @Column(name = "data_visita") private LocalDate dataVisita;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvaliacaoCategoria> notasCategorias = new ArrayList<>();

    public Avaliacao() {
    }

    public Long getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(Long idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getNotaGeral() {
        return notaGeral;
    }

    public void setNotaGeral(BigDecimal notaGeral) {
        this.notaGeral = notaGeral;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(LocalDate dataVisita) {
        this.dataVisita = dataVisita;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<AvaliacaoCategoria> getNotasCategorias() {
        return notasCategorias;
    }

    public void setNotasCategorias(List<AvaliacaoCategoria> notasCategorias) {
        this.notasCategorias.clear();
        if (notasCategorias != null) {
            notasCategorias.forEach(this::adicionarNotaCategoria);
        }
    }

    public void adicionarNotaCategoria(AvaliacaoCategoria notaCategoria) {
        if (notaCategoria != null) {
            this.notasCategorias.add(notaCategoria);
            notaCategoria.setAvaliacao(this);
        }
    }
}
