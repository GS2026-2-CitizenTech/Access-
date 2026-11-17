package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.Avaliacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AvaliacaoRespostaDTO {

    private Long idAvaliacao;
    private Long idRestaurante;
    private Long idUsuario;
    private BigDecimal notaGeral;
    private String comentario;
    private LocalDate dataVisita;
    private LocalDateTime dataCriacao;
    private List<AvaliacaoCategoriaRespostaDTO> notasCategorias;

    public Long getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(Long idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public Long getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(Long idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<AvaliacaoCategoriaRespostaDTO> getNotasCategorias() {
        return notasCategorias;
    }

    public void setNotasCategorias(List<AvaliacaoCategoriaRespostaDTO> notasCategorias) {
        this.notasCategorias = notasCategorias;
    }

    public static AvaliacaoRespostaDTO from(Avaliacao avaliacao) {
        AvaliacaoRespostaDTO resposta = new AvaliacaoRespostaDTO();
        resposta.setIdAvaliacao(avaliacao.getIdAvaliacao());
        resposta.setIdRestaurante(avaliacao.getRestaurante().getIdRestaurante());
        resposta.setIdUsuario(avaliacao.getUsuario().getIdUsuario());
        resposta.setNotaGeral(avaliacao.getNotaGeral());
        resposta.setComentario(avaliacao.getComentario());
        resposta.setDataVisita(avaliacao.getDataVisita());
        resposta.setDataCriacao(avaliacao.getDataCriacao());
        resposta.setNotasCategorias(avaliacao.getNotasCategorias().stream()
                .map(AvaliacaoCategoriaRespostaDTO::from)
                .toList());
        return resposta;
    }
}
