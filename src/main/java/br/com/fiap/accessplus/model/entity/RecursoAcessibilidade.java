package br.com.fiap.accessplus.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "RECURSO_ACESSIBILIDADE", uniqueConstraints = @UniqueConstraint(columnNames = {"categoria_id", "nome"}))
public class RecursoAcessibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recurso_acessibilidade")
    @SequenceGenerator(name = "seq_recurso_acessibilidade", sequenceName = "SEQ_RECURSO_ACESSIBILIDADE", allocationSize = 1)
    @Column(name = "id_recurso")
    private Long idRecurso;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao") private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaAcessibilidadeEntity categoria;

    public RecursoAcessibilidade() {
    }

    public Long getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Long idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaAcessibilidadeEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAcessibilidadeEntity categoria) {
        this.categoria = categoria;
    }
    public void setCategoria(CategoriaAcessibilidade codigo) {
        if (codigo == null) {
            this.categoria = null;
            return;
        }
        CategoriaAcessibilidadeEntity categoriaTemporaria = new CategoriaAcessibilidadeEntity();
        categoriaTemporaria.setCodigo(codigo);
        this.categoria = categoriaTemporaria;
    }
}
