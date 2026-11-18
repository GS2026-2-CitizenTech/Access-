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
@Table(name = "RESTAURANTE_RECURSO", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurante_id", "recurso_id"}))
public class RestauranteRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_restaurante_recurso")
    @SequenceGenerator(name = "seq_restaurante_recurso", sequenceName = "SEQ_RESTAURANTE_RECURSO", allocationSize = 1)
    @Column(name = "id_restaurante_recurso")
    private Long idRestauranteRecurso;

    @Column(name = "descricao_adicional")
    private String descricaoAdicional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id", nullable = false)
    private RecursoAcessibilidade recurso;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_declaracao", nullable = false)
    private StatusDeclaracao statusDeclaracao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @Column(name = "autodeclarado", nullable = false, columnDefinition = "NUMBER(1)")
    private boolean autodeclarado;

    public RestauranteRecurso() {
    }

    public Long getIdRestauranteRecurso() {
        return idRestauranteRecurso;
    }

    public void setIdRestauranteRecurso(Long idRestauranteRecurso) {
        this.idRestauranteRecurso = idRestauranteRecurso;
    }

    public String getDescricaoAdicional() { return descricaoAdicional; }
    public void setDescricaoAdicional(String descricaoAdicional) { this.descricaoAdicional = descricaoAdicional; }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public RecursoAcessibilidade getRecurso() {
        return recurso;
    }

    public void setRecurso(RecursoAcessibilidade recurso) {
        this.recurso = recurso;
    }

    public StatusDeclaracao getStatusDeclaracao() {
        return statusDeclaracao;
    }

    public void setStatusDeclaracao(StatusDeclaracao statusDeclaracao) {
        this.statusDeclaracao = statusDeclaracao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public boolean isAutodeclarado() {
        return autodeclarado;
    }

    public void setAutodeclarado(boolean autodeclarado) {
        this.autodeclarado = autodeclarado;
    }
}
