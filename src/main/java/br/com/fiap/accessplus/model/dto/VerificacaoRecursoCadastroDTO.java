package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.ResultadoVerificacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VerificacaoRecursoCadastroDTO {

    @NotNull(message = "Declaração de recurso é obrigatória")
    private Long idRestauranteRecurso;

    @NotNull(message = "Resultado da verificação é obrigatório")
    private ResultadoVerificacao resultado;

    @Size(max = 500, message = "Comentário deve ter no máximo 500 caracteres")
    private String comentario;

    public Long getIdRestauranteRecurso() {
        return idRestauranteRecurso;
    }

    public void setIdRestauranteRecurso(Long idRestauranteRecurso) {
        this.idRestauranteRecurso = idRestauranteRecurso;
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
}
