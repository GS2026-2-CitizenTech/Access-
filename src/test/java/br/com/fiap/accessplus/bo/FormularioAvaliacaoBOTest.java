package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.model.dto.FormularioAvaliacaoDTO;
import br.com.fiap.accessplus.exception.RestauranteNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.ResultadoVerificacao;
import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FormularioAvaliacaoBOTest {

    private AvaliacaoBO avaliacaoBO;
    private VerificacaoRecursoBO verificacaoRecursoBO;
    private FormularioAvaliacaoBO formularioBO;

    @BeforeEach
    void setup() {
        avaliacaoBO = mock(AvaliacaoBO.class);
        verificacaoRecursoBO = mock(VerificacaoRecursoBO.class);
        formularioBO = new FormularioAvaliacaoBO(avaliacaoBO, verificacaoRecursoBO);
        when(avaliacaoBO.cadastrar(eq(10L), eq(2L), any(Avaliacao.class))).thenAnswer(invocation -> {
            Avaliacao avaliacao = invocation.getArgument(2);
            avaliacao.setIdAvaliacao(50L);
            return avaliacao;
        });
    }

    @Test
    void cadastraNotasInteirasEMeiasCategoriasOpcionais() {
        FormularioAvaliacaoDTO formulario = formulario("4.5");
        formulario.setNotaFisica(new BigDecimal("0"));
        formulario.setNotaVisual(new BigDecimal("3.5"));

        Avaliacao resultado = formularioBO.cadastrar(10L, formulario);

        assertEquals(new BigDecimal("4.5"), resultado.getNotaGeral());
        assertEquals(2, resultado.getNotasCategorias().size());
        assertEquals(CategoriaAcessibilidade.FISICA, resultado.getNotasCategorias().getFirst().getCodigoCategoria());
        assertEquals(new BigDecimal("0"), resultado.getNotasCategorias().getFirst().getNota());
        assertEquals(CategoriaAcessibilidade.VISUAL, resultado.getNotasCategorias().get(1).getCodigoCategoria());
        assertEquals(new BigDecimal("3.5"), resultado.getNotasCategorias().get(1).getNota());
        assertEquals(resultado, resultado.getNotasCategorias().getFirst().getAvaliacao());
    }

    @Test
    void mantemCategoriaNaoInformadaForaDaAvaliacao() {
        Avaliacao resultado = formularioBO.cadastrar(10L, formulario("0"));

        assertEquals(BigDecimal.ZERO, resultado.getNotaGeral());
        assertEquals(0, resultado.getNotasCategorias().size());
        verify(verificacaoRecursoBO, never()).cadastrar(any(), any());
    }

    @Test
    void cadastraVerificacoesConfirmadaEContestadaAposAvaliacao() {
        FormularioAvaliacaoDTO formulario = formulario("3");
        formulario.getVerificacoes().add(verificacao(7L, ResultadoVerificacao.CONFIRMADO));
        formulario.getVerificacoes().add(verificacao(8L, ResultadoVerificacao.CONTESTADO));

        formularioBO.cadastrar(10L, formulario);

        org.mockito.ArgumentCaptor<VerificacaoRecurso> captor = org.mockito.ArgumentCaptor.forClass(VerificacaoRecurso.class);
        verify(verificacaoRecursoBO, org.mockito.Mockito.times(2)).cadastrar(eq(50L), captor.capture());
        assertEquals(ResultadoVerificacao.CONFIRMADO, captor.getAllValues().getFirst().getResultado());
        assertEquals(7L, captor.getAllValues().getFirst().getRestauranteRecurso().getIdRestauranteRecurso());
        assertEquals(ResultadoVerificacao.CONTESTADO, captor.getAllValues().get(1).getResultado());
        assertEquals(8L, captor.getAllValues().get(1).getRestauranteRecurso().getIdRestauranteRecurso());
    }

    @Test
    void propagaFalhaDaVerificacaoParaQueATransacaoSejaRevertida() {
        FormularioAvaliacaoDTO formulario = formulario("4");
        formulario.getVerificacoes().add(verificacao(7L, ResultadoVerificacao.CONFIRMADO));
        when(verificacaoRecursoBO.cadastrar(eq(50L), any(VerificacaoRecurso.class)))
                .thenThrow(new IllegalArgumentException("Declaração inválida"));

        assertThrows(IllegalArgumentException.class, () -> formularioBO.cadastrar(10L, formulario));
        assertNotNull(buscarMetodoCadastrar().getAnnotation(Transactional.class));
    }

    @Test
    void naoRegistraVerificacoesQuandoORestauranteNaoExiste() {
        when(avaliacaoBO.cadastrar(eq(99L), eq(2L), any(Avaliacao.class)))
                .thenThrow(new RestauranteNaoEncontradoException("Restaurante não encontrado"));

        assertThrows(RestauranteNaoEncontradoException.class,
                () -> formularioBO.cadastrar(99L, formulario("4.5")));
        verify(verificacaoRecursoBO, never()).cadastrar(any(), any());
    }

    private java.lang.reflect.Method buscarMetodoCadastrar() {
        try {
            return FormularioAvaliacaoBO.class.getDeclaredMethod("cadastrar", Long.class, FormularioAvaliacaoDTO.class);
        } catch (NoSuchMethodException ex) {
            throw new AssertionError(ex);
        }
    }

    private FormularioAvaliacaoDTO formulario(String notaGeral) {
        FormularioAvaliacaoDTO formulario = new FormularioAvaliacaoDTO();
        formulario.setIdUsuario(2L);
        formulario.setNotaGeral(new BigDecimal(notaGeral));
        return formulario;
    }

    private FormularioAvaliacaoDTO.VerificacaoFormularioDTO verificacao(Long idDeclaracao,
                                                                           ResultadoVerificacao resultado) {
        FormularioAvaliacaoDTO.VerificacaoFormularioDTO verificacao =
                new FormularioAvaliacaoDTO.VerificacaoFormularioDTO();
        verificacao.setIdRestauranteRecurso(idDeclaracao);
        verificacao.setResultado(resultado);
        return verificacao;
    }
}
