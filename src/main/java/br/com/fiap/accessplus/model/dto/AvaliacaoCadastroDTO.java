package br.com.fiap.accessplus.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AvaliacaoCadastroDTO {

    @NotNull(message = "Id do usuário é obrigatório")
    private Long idUsuario;

    @NotNull(message = "Nota geral é obrigatória")
    @DecimalMin(value = "0.0", message = "Nota geral deve ser maior ou igual a zero")
    @DecimalMax(value = "5.0", message = "Nota geral deve ser menor ou igual a cinco")
    private BigDecimal notaGeral;

    private String comentario;
    private LocalDate dataVisita;

    private List<@Valid AvaliacaoCategoriaDTO> notasCategorias;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getNotaGeral() {
        return notaGeral;
    }

    public void setNotaGeral(BigDecimal notaGeral) {
        this.notaGeral = notaGeral;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(LocalDate dataVisita) {
        this.dataVisita = dataVisita;
    }

    public List<AvaliacaoCategoriaDTO> getNotasCategorias() {
        return notasCategorias;
    }

    public void setNotasCategorias(List<AvaliacaoCategoriaDTO> notasCategorias) {
        this.notasCategorias = notasCategorias;
    }
}
