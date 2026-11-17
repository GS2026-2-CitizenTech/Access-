package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.StatusDeclaracao;

import java.time.LocalDateTime;

public class RestauranteRecursoRespostaDTO {

    private Long idRestauranteRecurso;
    private Long idRecurso;
    private String nomeRecurso;
    private StatusDeclaracao statusDeclaracao;
    private LocalDateTime dataAtualizacao;
    private boolean autodeclarado;

    public Long getIdRestauranteRecurso() {
        return idRestauranteRecurso;
    }

    public void setIdRestauranteRecurso(Long idRestauranteRecurso) {
        this.idRestauranteRecurso = idRestauranteRecurso;
    }

    public Long getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Long idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getNomeRecurso() {
        return nomeRecurso;
    }

    public void setNomeRecurso(String nomeRecurso) {
        this.nomeRecurso = nomeRecurso;
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

    public static RestauranteRecursoRespostaDTO from(RestauranteRecurso declaracao) {
        RestauranteRecursoRespostaDTO resposta = new RestauranteRecursoRespostaDTO();
        resposta.setIdRestauranteRecurso(declaracao.getIdRestauranteRecurso());
        resposta.setIdRecurso(declaracao.getRecurso().getIdRecurso());
        resposta.setNomeRecurso(declaracao.getRecurso().getNome());
        resposta.setStatusDeclaracao(declaracao.getStatusDeclaracao());
        resposta.setDataAtualizacao(declaracao.getDataAtualizacao());
        resposta.setAutodeclarado(declaracao.isAutodeclarado());
        return resposta;
    }
}
