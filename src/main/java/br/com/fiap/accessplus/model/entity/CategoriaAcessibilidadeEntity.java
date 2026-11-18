package br.com.fiap.accessplus.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIA_ACESSIBILIDADE")
public class CategoriaAcessibilidadeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categoria_acessibilidade")
    @SequenceGenerator(name = "seq_categoria_acessibilidade", sequenceName = "SEQ_CATEGORIA_ACESSIBILIDADE", allocationSize = 1)
    @Column(name = "id_categoria")
    private Long idCategoria;
    @Enumerated(EnumType.STRING)
    @Column(name = "codigo", nullable = false)
    private CategoriaAcessibilidade codigo;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "descricao")
    private String descricao;
    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
    public CategoriaAcessibilidade getCodigo() { return codigo; }
    public void setCodigo(CategoriaAcessibilidade codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
