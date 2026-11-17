package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.RestauranteNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.repository.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestauranteBOTest {

    private RestauranteRepository repository;
    private RestauranteBO restauranteBO;

    @BeforeEach
    void setup() {
        repository = mock(RestauranteRepository.class);
        restauranteBO = new RestauranteBO(repository);
        when(repository.save(any(Restaurante.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void cadastraRestauranteAtivoENaoPublicado() {
        Restaurante resultado = restauranteBO.cadastrar(novoRestaurante());

        assertTrue(resultado.isAtivo());
        assertFalse(resultado.isPublicado());
    }

    @Test
    void rejeitaCadastroSemCamposObrigatorios() {
        Restaurante restaurante = novoRestaurante();
        restaurante.setNome(" ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> restauranteBO.cadastrar(restaurante));

        assertEquals("Nome não pode estar vazio", ex.getMessage());
    }

    @Test
    void informaQuandoRestauranteNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RestauranteNaoEncontradoException.class, () -> restauranteBO.buscarPorId(99L));
    }

    @Test
    void atualizaParcialmenteSemApagarCamposOmitidos() {
        Restaurante existente = restauranteComId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        Restaurante alteracao = new Restaurante();
        alteracao.setDescricao("Novo cardápio acessível");

        Restaurante resultado = restauranteBO.atualizar(1L, alteracao);

        assertEquals("Novo cardápio acessível", resultado.getDescricao());
        assertEquals("Centro", resultado.getBairro());
        assertEquals("Rua das Flores, 10", resultado.getEndereco());
    }

    @Test
    void atualizaCompletamenteComCamposObrigatorios() {
        Restaurante existente = restauranteComId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        Restaurante atualizacao = new Restaurante("Novo Nome", "Av. Brasil, 50", "Jardins",
                "Ambiente atualizado", false, false, null);

        Restaurante resultado = restauranteBO.atualizarCompleto(1L, atualizacao);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("Jardins", resultado.getBairro());
        assertEquals("Av. Brasil, 50", resultado.getEndereco());
        assertEquals("Ambiente atualizado", resultado.getDescricao());
    }

    @Test
    void rejeitaAtualizacaoCompletaInvalida() {
        Restaurante atualizacao = novoRestaurante();
        atualizacao.setEndereco(null);

        assertThrows(IllegalArgumentException.class, () -> restauranteBO.atualizarCompleto(1L, atualizacao));
    }

    @Test
    void controlaAtivacaoEPublicacaoDeFormaIndependente() {
        Restaurante existente = restauranteComId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        restauranteBO.desativar(1L);
        assertFalse(existente.isAtivo());
        assertThrows(IllegalArgumentException.class, () -> restauranteBO.publicar(1L));

        restauranteBO.reativar(1L);
        restauranteBO.publicar(1L);
        assertTrue(existente.isAtivo());
        assertTrue(existente.isPublicado());

        restauranteBO.despublicar(1L);
        assertFalse(existente.isPublicado());
    }

    @Test
    void consultaPublicaFiltraEstadosENaoExibeInexistente() {
        Restaurante publico = restauranteComId(1L);
        publico.setPublicado(true);
        when(repository.findByAtivoTrueAndPublicadoTrue()).thenReturn(List.of(publico));
        when(repository.findByIdRestauranteAndAtivoTrueAndPublicadoTrue(2L)).thenReturn(Optional.empty());

        assertEquals(List.of(publico), restauranteBO.listarPublicos());
        assertThrows(RestauranteNaoEncontradoException.class, () -> restauranteBO.buscarPublicoPorId(2L));
    }

    private Restaurante novoRestaurante() {
        return new Restaurante("Bistrô Central", "Rua das Flores, 10", "Centro",
                "Cozinha brasileira", false, false, null);
    }

    private Restaurante restauranteComId(Long id) {
        Restaurante restaurante = novoRestaurante();
        restaurante.setIdRestaurante(id);
        restaurante.setAtivo(true);
        return restaurante;
    }
}
