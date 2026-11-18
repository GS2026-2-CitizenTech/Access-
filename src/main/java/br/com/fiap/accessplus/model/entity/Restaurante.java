package br.com.fiap.accessplus.model.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "RESTAURANTE")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_restaurante")
    @SequenceGenerator(name = "seq_restaurante", sequenceName = "SEQ_RESTAURANTE", allocationSize = 1)
    @Column(name = "id_restaurante")
    private Long idRestaurante;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "endereco", nullable = false)
    private String endereco;
    @Column(name = "bairro", nullable = false)
    private String bairro;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @Column(name = "publicado", nullable = false, columnDefinition = "NUMBER(1)")
    private boolean publicado;
    @Column(name = "ativo", nullable = false, columnDefinition = "NUMBER(1)")
    private boolean ativo;
    @Column(name = "cidade")
    private String cidade;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_anterior_id")
    private Restaurante restauranteAnterior;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario responsavel;

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

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public Usuario getResponsavel() { return responsavel; }
    public void setResponsavel(Usuario responsavel) { this.responsavel = responsavel; }
}
