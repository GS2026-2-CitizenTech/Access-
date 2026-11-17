package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.StatusDeclaracao;
import jakarta.validation.constraints.NotNull;

public class RestauranteRecursoDTO {

    @NotNull(message = "Status da declaração é obrigatório")
    private StatusDeclaracao statusDeclaracao;

    public StatusDeclaracao getStatusDeclaracao() {
        return statusDeclaracao;
    }

    public void setStatusDeclaracao(StatusDeclaracao statusDeclaracao) {
        this.statusDeclaracao = statusDeclaracao;
    }
}
