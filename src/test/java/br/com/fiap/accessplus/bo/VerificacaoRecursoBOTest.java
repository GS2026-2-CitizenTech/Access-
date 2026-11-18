package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.AvaliacaoNaoEncontradaException;
import br.com.fiap.accessplus.exception.DeclaracaoRecursoNaoEncontradaException;
import br.com.fiap.accessplus.exception.VerificacaoRecursoDuplicadaException;
import br.com.fiap.accessplus.exception.VerificacaoRecursoNaoEncontradaException;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.ResultadoVerificacao;
import br.com.fiap.accessplus.model.entity.StatusDeclaracao;
import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;
import br.com.fiap.accessplus.repository.AvaliacaoRepository;
import br.com.fiap.accessplus.repository.RestauranteRecursoRepository;
import br.com.fiap.accessplus.repository.VerificacaoRecursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VerificacaoRecursoBOTest {

    private VerificacaoRecursoRepository verificacaoRepository;
    private AvaliacaoRepository avaliacaoRepository;
    private RestauranteRecursoRepository restauranteRecursoRepository;
    private AvaliacaoBO avaliacaoBO;
    private VerificacaoRecursoBO verificacaoBO;

    @BeforeEach
    void setup() {
        verificacaoRepository = mock(VerificacaoRecursoRepository.class);
        avaliacaoRepository = mock(AvaliacaoRepository.class);
        restauranteRecursoRepository = mock(RestauranteRecursoRepository.class);
        avaliacaoBO = mock(AvaliacaoBO.class);
        verificacaoBO = new VerificacaoRecursoBO(verificacaoRepository, avaliacaoRepository,
                restauranteRecursoRepository, avaliacaoBO);
        when(verificacaoRepository.save(any(VerificacaoRecurso.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void cadastraVerificacaoConfirmadaSemAlterarDeclaracaoOriginal() {
        Avaliacao avaliacao = avaliacao(1L, 10L);
        RestauranteRecurso declaracao = declaracao(2L, 10L, StatusDeclaracao.SIM);
        prepararCadastroValido(avaliacao, declaracao);
        VerificacaoRecurso verificacao = verificacao(2L, ResultadoVerificacao.CONFIRMADO);

        VerificacaoRecurso resultado = verificacaoBO.cadastrar(1L, verificacao);

        assertSame(avaliacao, resultado.getAvaliacao());
        assertSame(declaracao, resultado.getRestauranteRecurso());
        assertEquals(ResultadoVerificacao.CONFIRMADO, resultado.getResultado());
        assertEquals(StatusDeclaracao.SIM, declaracao.getStatusDeclaracao());
        assertNotNull(resultado.getDataVerificacao());
    }

    @Test
    void cadastraVerificacaoContestada() {
        Avaliacao avaliacao = avaliacao(1L, 10L);
        RestauranteRecurso declaracao = declaracao(2L, 10L, StatusDeclaracao.SIM);
        prepararCadastroValido(avaliacao, declaracao);

        VerificacaoRecurso resultado = verificacaoBO.cadastrar(1L,
                verificacao(2L, ResultadoVerificacao.CONTESTADO));

        assertEquals(ResultadoVerificacao.CONTESTADO, resultado.getResultado());
    }

    @Test
    void cadastraVerificacaoNaoVerificado() {
        Avaliacao avaliacao = avaliacao(1L, 10L);
        RestauranteRecurso declaracao = declaracao(2L, 10L, StatusDeclaracao.NAO_INFORMADO);
        prepararCadastroValido(avaliacao, declaracao);

        VerificacaoRecurso resultado = verificacaoBO.cadastrar(1L,
                verificacao(2L, ResultadoVerificacao.NAO_VERIFICADO));

        assertEquals(ResultadoVerificacao.NAO_VERIFICADO, resultado.getResultado());
    }

    @Test
    void rejeitaAvaliacaoInexistente() {
        when(avaliacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AvaliacaoNaoEncontradaException.class,
                () -> verificacaoBO.cadastrar(99L, verificacao(2L, ResultadoVerificacao.CONFIRMADO)));
    }

    @Test
    void rejeitaDeclaracaoInexistente() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao(1L, 10L)));
        when(restauranteRecursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DeclaracaoRecursoNaoEncontradaException.class,
                () -> verificacaoBO.cadastrar(1L, verificacao(99L, ResultadoVerificacao.CONFIRMADO)));
    }

    @Test
    void rejeitaAvaliacaoEDeclaracaoDeRestaurantesDiferentes() {
        Avaliacao avaliacao = avaliacao(1L, 10L);
        RestauranteRecurso declaracao = declaracao(2L, 20L, StatusDeclaracao.SIM);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
        when(restauranteRecursoRepository.findById(2L)).thenReturn(Optional.of(declaracao));

        assertThrows(IllegalArgumentException.class,
                () -> verificacaoBO.cadastrar(1L, verificacao(2L, ResultadoVerificacao.CONFIRMADO)));
        verify(verificacaoRepository, never()).save(any());
    }

    @Test
    void rejeitaDuplicidadeDaMesmaAvaliacaoEDeclaracao() {
        Avaliacao avaliacao = avaliacao(1L, 10L);
        RestauranteRecurso declaracao = declaracao(2L, 10L, StatusDeclaracao.SIM);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
        when(restauranteRecursoRepository.findById(2L)).thenReturn(Optional.of(declaracao));
        when(verificacaoRepository.existsByAvaliacaoIdAvaliacaoAndRestauranteRecursoIdRestauranteRecurso(1L, 2L))
                .thenReturn(true);

        assertThrows(VerificacaoRecursoDuplicadaException.class,
                () -> verificacaoBO.cadastrar(1L, verificacao(2L, ResultadoVerificacao.CONFIRMADO)));
        verify(verificacaoRepository, never()).save(any());
    }

    @Test
    void rejeitaResultadoAusenteEComentarioAcimaDoLimite() {
        VerificacaoRecurso semResultado = verificacao(2L, null);
        assertThrows(IllegalArgumentException.class, () -> verificacaoBO.cadastrar(1L, semResultado));

        VerificacaoRecurso comentarioGrande = verificacao(2L, ResultadoVerificacao.CONFIRMADO);
        comentarioGrande.setComentario("a".repeat(501));
        assertThrows(IllegalArgumentException.class, () -> verificacaoBO.cadastrar(1L, comentarioGrande));
    }

    @Test
    void listaVerificacoesSomenteQuandoAvaliacaoEhPublica() {
        Avaliacao avaliacao = avaliacao(1L, 10L);
        VerificacaoRecurso verificacao = verificacao(2L, ResultadoVerificacao.CONFIRMADO);
        verificacao.setAvaliacao(avaliacao);
        verificacao.setRestauranteRecurso(declaracao(2L, 10L, StatusDeclaracao.SIM));
        when(avaliacaoBO.buscarPublicaPorId(1L)).thenReturn(avaliacao);
        when(verificacaoRepository.findByAvaliacaoIdAvaliacao(1L)).thenReturn(List.of(verificacao));

        assertEquals(List.of(verificacao), verificacaoBO.listarPublicasDaAvaliacao(1L));
        verify(avaliacaoBO).buscarPublicaPorId(1L);
    }

    @Test
    void buscaVerificacaoSomenteQuandoRestauranteDaAvaliacaoEhPublico() {
        Avaliacao avaliacao = avaliacao(1L, 10L);
        VerificacaoRecurso verificacao = verificacao(2L, ResultadoVerificacao.CONFIRMADO);
        verificacao.setIdVerificacao(3L);
        verificacao.setAvaliacao(avaliacao);
        verificacao.setRestauranteRecurso(declaracao(2L, 10L, StatusDeclaracao.SIM));
        when(verificacaoRepository.findById(3L)).thenReturn(Optional.of(verificacao));
        when(avaliacaoBO.buscarPublicaPorId(1L)).thenReturn(avaliacao);

        assertSame(verificacao, verificacaoBO.buscarPublicaPorId(3L));
        verify(avaliacaoBO).buscarPublicaPorId(1L);
    }

    @Test
    void trataVerificacaoInexistente() {
        when(verificacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(VerificacaoRecursoNaoEncontradaException.class,
                () -> verificacaoBO.buscarPublicaPorId(99L));
    }

    private void prepararCadastroValido(Avaliacao avaliacao, RestauranteRecurso declaracao) {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
        when(restauranteRecursoRepository.findById(2L)).thenReturn(Optional.of(declaracao));
        when(verificacaoRepository.existsByAvaliacaoIdAvaliacaoAndRestauranteRecursoIdRestauranteRecurso(1L, 2L))
                .thenReturn(false);
    }

    private Avaliacao avaliacao(Long idAvaliacao, Long idRestaurante) {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(idRestaurante);
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(idAvaliacao);
        avaliacao.setRestaurante(restaurante);
        return avaliacao;
    }

    private RestauranteRecurso declaracao(Long idDeclaracao, Long idRestaurante, StatusDeclaracao status) {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(idRestaurante);
        RecursoAcessibilidade recurso = new RecursoAcessibilidade();
        recurso.setIdRecurso(5L);
        recurso.setNome("Rampa de acesso");
        recurso.setCategoria(CategoriaAcessibilidade.FISICA);
        RestauranteRecurso declaracao = new RestauranteRecurso();
        declaracao.setIdRestauranteRecurso(idDeclaracao);
        declaracao.setRestaurante(restaurante);
        declaracao.setRecurso(recurso);
        declaracao.setStatusDeclaracao(status);
        return declaracao;
    }

    private VerificacaoRecurso verificacao(Long idRestauranteRecurso, ResultadoVerificacao resultado) {
        RestauranteRecurso declaracao = new RestauranteRecurso();
        declaracao.setIdRestauranteRecurso(idRestauranteRecurso);
        VerificacaoRecurso verificacao = new VerificacaoRecurso();
        verificacao.setRestauranteRecurso(declaracao);
        verificacao.setResultado(resultado);
        verificacao.setComentario("Recurso observado durante a visita");
        return verificacao;
    }
}
