package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.model.dto.FormularioAvaliacaoDTO;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.AvaliacaoCategoria;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * Orquestra o envio do formulário MVC em uma única transação.
 */
@Service
public class FormularioAvaliacaoBO {

    private final AvaliacaoBO avaliacaoBO;
    private final VerificacaoRecursoBO verificacaoRecursoBO;

    public FormularioAvaliacaoBO(AvaliacaoBO avaliacaoBO,
                                 VerificacaoRecursoBO verificacaoRecursoBO) {
        this.avaliacaoBO = avaliacaoBO;
        this.verificacaoRecursoBO = verificacaoRecursoBO;
    }

    @Transactional
    public Avaliacao cadastrar(Long idRestaurante, FormularioAvaliacaoDTO formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("Dados da avaliação não informados");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNotaGeral(formulario.getNotaGeral());
        avaliacao.setComentario(formulario.getComentario());
        avaliacao.setDataVisita(formulario.getDataVisita());
        adicionarNotasCategorias(avaliacao, formulario);

        Avaliacao cadastrada = avaliacaoBO.cadastrar(idRestaurante, formulario.getIdUsuario(), avaliacao);
        for (FormularioAvaliacaoDTO.VerificacaoFormularioDTO dadosVerificacao : formulario.getVerificacoes()) {
            if (dadosVerificacao != null && dadosVerificacao.getResultado() != null) {
                verificacaoRecursoBO.cadastrar(cadastrada.getIdAvaliacao(), paraVerificacao(dadosVerificacao));
            }
        }
        return cadastrada;
    }

    private void adicionarNotasCategorias(Avaliacao avaliacao, FormularioAvaliacaoDTO formulario) {
        Stream.of(
                        nota(CategoriaAcessibilidade.FISICA, formulario.getNotaFisica()),
                        nota(CategoriaAcessibilidade.VISUAL, formulario.getNotaVisual()),
                        nota(CategoriaAcessibilidade.AUDITIVA, formulario.getNotaAuditiva()),
                        nota(CategoriaAcessibilidade.SENSORIAL, formulario.getNotaSensorial()))
                .flatMap(java.util.Optional::stream)
                .forEach(avaliacao::adicionarNotaCategoria);
    }

    private java.util.Optional<AvaliacaoCategoria> nota(CategoriaAcessibilidade categoria, BigDecimal nota) {
        if (nota == null) {
            return java.util.Optional.empty();
        }
        AvaliacaoCategoria avaliacaoCategoria = new AvaliacaoCategoria();
        avaliacaoCategoria.setCategoria(categoria);
        avaliacaoCategoria.setNota(nota);
        return java.util.Optional.of(avaliacaoCategoria);
    }

    private VerificacaoRecurso paraVerificacao(FormularioAvaliacaoDTO.VerificacaoFormularioDTO dados) {
        if (dados.getIdRestauranteRecurso() == null) {
            throw new IllegalArgumentException("Declaração de recurso é obrigatória");
        }
        RestauranteRecurso declaracao = new RestauranteRecurso();
        declaracao.setIdRestauranteRecurso(dados.getIdRestauranteRecurso());

        VerificacaoRecurso verificacao = new VerificacaoRecurso();
        verificacao.setRestauranteRecurso(declaracao);
        verificacao.setResultado(dados.getResultado());
        verificacao.setComentario(dados.getComentario());
        return verificacao;
    }
}
