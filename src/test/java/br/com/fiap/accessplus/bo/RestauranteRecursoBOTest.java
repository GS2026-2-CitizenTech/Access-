package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.RestauranteNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.StatusDeclaracao;
import br.com.fiap.accessplus.repository.RestauranteRecursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestauranteRecursoBOTest {

    private RestauranteRecursoRepository repository;
    private RestauranteBO restauranteBO;
    private RecursoAcessibilidadeBO recursoBO;
    private RestauranteRecursoBO restauranteRecursoBO;

    @BeforeEach
    void setup() {
        repository = mock(RestauranteRecursoRepository.class);
        restauranteBO = mock(RestauranteBO.class);
        recursoBO = mock(RecursoAcessibilidadeBO.class);
        restauranteRecursoBO = new RestauranteRecursoBO(repository, restauranteBO, recursoBO);
        when(repository.save(any(RestauranteRecurso.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void cadastraDeclaracaoAutodeclaradaValida() {
        Restaurante restaurante = restaurante(1L, true, true);
        RecursoAcessibilidade recurso = recurso(2L);
        when(restauranteBO.buscarPorId(1L)).thenReturn(restaurante);
        when(recursoBO.buscarPorId(2L)).thenReturn(recurso);
        when(repository.findByRestauranteIdRestauranteAndRecursoIdRecurso(1L, 2L)).thenReturn(Optional.empty());

        RestauranteRecurso resultado = restauranteRecursoBO.declarar(1L, 2L, StatusDeclaracao.SIM);

        assertSame(restaurante, resultado.getRestaurante());
        assertSame(recurso, resultado.getRecurso());
        assertEquals(StatusDeclaracao.SIM, resultado.getStatusDeclaracao());
        assertNotNull(resultado.getDataAtualizacao());
        assertTrue(resultado.isAutodeclarado());
    }

    @Test
    void atualizaDeclaracaoExistenteSemCriarDuplicidade() {
        RestauranteRecurso existente = declaracao(7L, restaurante(1L, true, true), recurso(2L), StatusDeclaracao.NAO);
        when(restauranteBO.buscarPorId(1L)).thenReturn(existente.getRestaurante());
        when(recursoBO.buscarPorId(2L)).thenReturn(existente.getRecurso());
        when(repository.findByRestauranteIdRestauranteAndRecursoIdRecurso(1L, 2L)).thenReturn(Optional.of(existente));

        RestauranteRecurso resultado = restauranteRecursoBO.declarar(1L, 2L, StatusDeclaracao.SIM);

        assertSame(existente, resultado);
        assertEquals(7L, resultado.getIdRestauranteRecurso());
        assertEquals(StatusDeclaracao.SIM, resultado.getStatusDeclaracao());
        verify(repository).save(existente);
    }

    @Test
    void trataRestauranteInexistente() {
        when(restauranteBO.buscarPorId(99L)).thenThrow(new RestauranteNaoEncontradoException("Restaurante não encontrado"));

        assertThrows(RestauranteNaoEncontradoException.class,
                () -> restauranteRecursoBO.declarar(99L, 2L, StatusDeclaracao.SIM));
    }

    @Test
    void filtroPublicoConsideraSomenteSimEEstadosPublicos() {
        RecursoAcessibilidade recurso = recurso(2L);
        Restaurante publico = restaurante(1L, true, true);
        Restaurante inativo = restaurante(3L, false, true);
        Restaurante naoPublicado = restaurante(4L, true, false);
        RestauranteRecurso declaracaoNao = declaracao(10L, restaurante(5L, true, true), recurso, StatusDeclaracao.NAO);
        when(recursoBO.buscarPorId(2L)).thenReturn(recurso);
        when(repository.findByRecursoIdRecursoAndStatusDeclaracao(2L, StatusDeclaracao.SIM))
                .thenReturn(List.of(
                        declaracao(1L, publico, recurso, StatusDeclaracao.SIM),
                        declaracao(2L, inativo, recurso, StatusDeclaracao.SIM),
                        declaracao(3L, naoPublicado, recurso, StatusDeclaracao.SIM),
                        declaracaoNao));

        assertEquals(List.of(publico), restauranteRecursoBO.listarRestaurantesPublicosComRecurso(2L));
    }

    @Test
    void diferenciaAusenciaDeDeclaracaoDeNaoInformado() {
        Restaurante restaurante = restaurante(1L, true, true);
        RecursoAcessibilidade recurso = recurso(2L);
        when(restauranteBO.buscarPublicoPorId(1L)).thenReturn(restaurante);
        when(repository.findByRestauranteIdRestaurante(1L)).thenReturn(List.of());

        assertTrue(restauranteRecursoBO.listarDeclaracoesPublicasDoRestaurante(1L).isEmpty());

        RestauranteRecurso naoInformado = declaracao(1L, restaurante, recurso, StatusDeclaracao.NAO_INFORMADO);
        when(repository.findByRestauranteIdRestaurante(1L)).thenReturn(List.of(naoInformado));
        List<RestauranteRecurso> declaracoes = restauranteRecursoBO.listarDeclaracoesPublicasDoRestaurante(1L);

        assertFalse(declaracoes.isEmpty());
        assertEquals(StatusDeclaracao.NAO_INFORMADO, declaracoes.getFirst().getStatusDeclaracao());
    }

    private Restaurante restaurante(Long id, boolean ativo, boolean publicado) {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(id);
        restaurante.setAtivo(ativo);
        restaurante.setPublicado(publicado);
        return restaurante;
    }

    private RecursoAcessibilidade recurso(Long id) {
        RecursoAcessibilidade recurso = new RecursoAcessibilidade();
        recurso.setIdRecurso(id);
        recurso.setNome("Rampa de acesso");
        recurso.setCategoria(CategoriaAcessibilidade.FISICA);
        return recurso;
    }

    private RestauranteRecurso declaracao(Long id, Restaurante restaurante, RecursoAcessibilidade recurso,
                                           StatusDeclaracao status) {
        RestauranteRecurso declaracao = new RestauranteRecurso();
        declaracao.setIdRestauranteRecurso(id);
        declaracao.setRestaurante(restaurante);
        declaracao.setRecurso(recurso);
        declaracao.setStatusDeclaracao(status);
        declaracao.setAutodeclarado(true);
        return declaracao;
    }
}
