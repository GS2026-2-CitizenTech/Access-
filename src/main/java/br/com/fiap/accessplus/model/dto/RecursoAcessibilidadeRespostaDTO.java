package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;

public class RecursoAcessibilidadeRespostaDTO {

    private Long idRecurso;
    private String nome;
    private String descricao;
    private CategoriaAcessibilidade categoria;

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

    public CategoriaAcessibilidade getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAcessibilidade categoria) {
        this.categoria = categoria;
    }

    public static RecursoAcessibilidadeRespostaDTO from(RecursoAcessibilidade recurso) {
        RecursoAcessibilidadeRespostaDTO resposta = new RecursoAcessibilidadeRespostaDTO();
        resposta.setIdRecurso(recurso.getIdRecurso());
        resposta.setNome(recurso.getNome());
        resposta.setDescricao(recurso.getDescricao());
        resposta.setCategoria(recurso.getCategoria().getCodigo());
        return resposta;
    }
}
