package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AvaliacaoCategoriaDTO {

    @NotNull(message = "Categoria de acessibilidade é obrigatória")
    private CategoriaAcessibilidade categoria;

    @NotNull(message = "Nota da categoria é obrigatória")
    @DecimalMin(value = "0.0", message = "Nota da categoria deve ser maior ou igual a zero")
    @DecimalMax(value = "5.0", message = "Nota da categoria deve ser menor ou igual a cinco")
    private BigDecimal nota;

    public CategoriaAcessibilidade getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAcessibilidade categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }
}
