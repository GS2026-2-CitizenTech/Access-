package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.ResultadoVerificacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Dados recebidos pelo formulário Thymeleaf de avaliação.
 *
 * <p>Este DTO pertence ao fluxo MVC e não altera o contrato da API REST.</p>
 */
public class FormularioAvaliacaoDTO {

    @NotNull(message = "Selecione um perfil de demonstração")
    private Long idUsuario;

    @NotNull(message = "A nota geral é obrigatória")
    @DecimalMin(value = "0.0", message = "A nota geral deve ser maior ou igual a zero")
    @DecimalMax(value = "5.0", message = "A nota geral deve ser menor ou igual a cinco")
    private BigDecimal notaGeral;

    private BigDecimal notaFisica;
    private BigDecimal notaVisual;
    private BigDecimal notaAuditiva;
    private BigDecimal notaSensorial;

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    private String comentario;

    private LocalDate dataVisita;

    private List<@Valid VerificacaoFormularioDTO> verificacoes = new ArrayList<>();

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public BigDecimal getNotaGeral() { return notaGeral; }
    public void setNotaGeral(BigDecimal notaGeral) { this.notaGeral = notaGeral; }
    public BigDecimal getNotaFisica() { return notaFisica; }
    public void setNotaFisica(BigDecimal notaFisica) { this.notaFisica = notaFisica; }
    public BigDecimal getNotaVisual() { return notaVisual; }
    public void setNotaVisual(BigDecimal notaVisual) { this.notaVisual = notaVisual; }
    public BigDecimal getNotaAuditiva() { return notaAuditiva; }
    public void setNotaAuditiva(BigDecimal notaAuditiva) { this.notaAuditiva = notaAuditiva; }
    public BigDecimal getNotaSensorial() { return notaSensorial; }
    public void setNotaSensorial(BigDecimal notaSensorial) { this.notaSensorial = notaSensorial; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDate getDataVisita() { return dataVisita; }
    public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }
    public List<VerificacaoFormularioDTO> getVerificacoes() { return verificacoes; }
    public void setVerificacoes(List<VerificacaoFormularioDTO> verificacoes) {
        this.verificacoes = verificacoes == null ? new ArrayList<>() : verificacoes;
    }

    public static class VerificacaoFormularioDTO {

        private Long idRestauranteRecurso;
        private ResultadoVerificacao resultado;

        @Size(max = 500, message = "O comentário da verificação deve ter no máximo 500 caracteres")
        private String comentario;

        public Long getIdRestauranteRecurso() { return idRestauranteRecurso; }
        public void setIdRestauranteRecurso(Long idRestauranteRecurso) {
            this.idRestauranteRecurso = idRestauranteRecurso;
        }
        public ResultadoVerificacao getResultado() { return resultado; }
        public void setResultado(ResultadoVerificacao resultado) { this.resultado = resultado; }
        public String getComentario() { return comentario; }
        public void setComentario(String comentario) { this.comentario = comentario; }
    }
}
