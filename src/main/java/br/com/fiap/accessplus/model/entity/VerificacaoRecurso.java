package br.com.fiap.accessplus.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(name = "VERIFICACAO_RECURSO", uniqueConstraints =
        @UniqueConstraint(columnNames = {"avaliacao_id", "restaurante_recurso_id"}))
public class VerificacaoRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_verificacao_recurso")
    @SequenceGenerator(name = "seq_verificacao_recurso", sequenceName = "SEQ_VERIFICACAO_RECURSO", allocationSize = 1)
    @Column(name = "id_verificacao")
    private Long idVerificacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", nullable = false, length = 20)
    private ResultadoVerificacao resultado;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "data_verificacao", nullable = false)
    private LocalDateTime dataVerificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_recurso_id", nullable = false)
    private RestauranteRecurso restauranteRecurso;

    public Long getIdVerificacao() {
        return idVerificacao;
    }

    public void setIdVerificacao(Long idVerificacao) {
        this.idVerificacao = idVerificacao;
    }

    public ResultadoVerificacao getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoVerificacao resultado) {
        this.resultado = resultado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataVerificacao() {
        return dataVerificacao;
    }

    public void setDataVerificacao(LocalDateTime dataVerificacao) {
        this.dataVerificacao = dataVerificacao;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public RestauranteRecurso getRestauranteRecurso() {
        return restauranteRecurso;
    }

    public void setRestauranteRecurso(RestauranteRecurso restauranteRecurso) {
        this.restauranteRecurso = restauranteRecurso;
    }
}
