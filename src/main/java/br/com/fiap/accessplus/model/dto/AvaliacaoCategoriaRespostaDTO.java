package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.AvaliacaoCategoria;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;

import java.math.BigDecimal;

public class AvaliacaoCategoriaRespostaDTO {

    private CategoriaAcessibilidade categoria;
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

    public static AvaliacaoCategoriaRespostaDTO from(AvaliacaoCategoria avaliacaoCategoria) {
        AvaliacaoCategoriaRespostaDTO resposta = new AvaliacaoCategoriaRespostaDTO();
        resposta.setCategoria(avaliacaoCategoria.getCategoria().getCodigo());
        resposta.setNota(avaliacaoCategoria.getNota());
        return resposta;
    }
}
