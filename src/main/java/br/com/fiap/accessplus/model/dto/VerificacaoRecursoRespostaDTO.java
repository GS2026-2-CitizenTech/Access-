package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;

import java.time.LocalDateTime;

public class VerificacaoRecursoRespostaDTO {

    private Long idVerificacao;
    private Long idAvaliacao;
    private Long idRestauranteRecurso;
    private Long idRecurso;
    private String nomeRecurso;
    private String resultado;
    private String comentario;
    private LocalDateTime dataVerificacao;

    public static VerificacaoRecursoRespostaDTO from(VerificacaoRecurso verificacao) {
        VerificacaoRecursoRespostaDTO dto = new VerificacaoRecursoRespostaDTO();
        dto.setIdVerificacao(verificacao.getIdVerificacao());
        dto.setIdAvaliacao(verificacao.getAvaliacao().getIdAvaliacao());
        dto.setIdRestauranteRecurso(verificacao.getRestauranteRecurso().getIdRestauranteRecurso());
        dto.setIdRecurso(verificacao.getRestauranteRecurso().getRecurso().getIdRecurso());
        dto.setNomeRecurso(verificacao.getRestauranteRecurso().getRecurso().getNome());
        dto.setResultado(verificacao.getResultado().name());
        dto.setComentario(verificacao.getComentario());
        dto.setDataVerificacao(verificacao.getDataVerificacao());
        return dto;
    }

    public Long getIdVerificacao() {
        return idVerificacao;
    }

    public void setIdVerificacao(Long idVerificacao) {
        this.idVerificacao = idVerificacao;
    }

    public Long getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(Long idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

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

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
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
}
