package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.RestauranteNaoEncontradoException;
import br.com.fiap.accessplus.exception.UsuarioNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.AvaliacaoCategoria;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.Usuario;
import br.com.fiap.accessplus.repository.AvaliacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AvaliacaoBOTest {

    private AvaliacaoRepository repository;
    private RestauranteBO restauranteBO;
    private UsuarioBO usuarioBO;
    private AvaliacaoBO avaliacaoBO;

    @BeforeEach
    void setup() {
        repository = mock(AvaliacaoRepository.class);
        restauranteBO = mock(RestauranteBO.class);
        usuarioBO = mock(UsuarioBO.class);
        avaliacaoBO = new AvaliacaoBO(repository, restauranteBO, usuarioBO);
        when(repository.save(any(Avaliacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void cadastraAvaliacaoValidaComNotaQuatroECincoECategorias() {
        Restaurante restaurante = restaurante(1L, true, true);
        Usuario usuario = usuario(2L);
        Avaliacao avaliacao = avaliacao("4.5");
        avaliacao.setComentario("Ambiente acolhedor");
        avaliacao.setNotasCategorias(List.of(
                notaCategoria(CategoriaAcessibilidade.FISICA, "0"),
                notaCategoria(CategoriaAcessibilidade.VISUAL, "4.5")));
        when(restauranteBO.buscarPublicoPorId(1L)).thenReturn(restaurante);
        when(usuarioBO.buscarPorId(2L)).thenReturn(usuario);

        Avaliacao resultado = avaliacaoBO.cadastrar(1L, 2L, avaliacao);

        assertEquals(new BigDecimal("4.5"), resultado.getNotaGeral());
        assertEquals("Ambiente acolhedor", resultado.getComentario());
        assertNotNull(resultado.getDataCriacao());
        assertSame(restaurante, resultado.getRestaurante());
        assertSame(usuario, resultado.getUsuario());
        assertSame(resultado, resultado.getNotasCategorias().getFirst().getAvaliacao());
        assertEquals(new BigDecimal("0"), resultado.getNotasCategorias().getFirst().getNota());
    }

    @Test
    void aceitaNotaZeroEComentarioOpcional() {
        Restaurante restaurante = restaurante(1L, true, true);
        when(restauranteBO.buscarPublicoPorId(1L)).thenReturn(restaurante);
        when(usuarioBO.buscarPorId(2L)).thenReturn(usuario(2L));

        Avaliacao resultado = avaliacaoBO.cadastrar(1L, 2L, avaliacao("0"));

        assertEquals(BigDecimal.ZERO, resultado.getNotaGeral());
        assertEquals(null, resultado.getComentario());
    }

    @Test
    void rejeitaNotasGeraisForaDoIntervaloOuIncremento() {
        assertThrows(IllegalArgumentException.class, () -> avaliacaoBO.cadastrar(1L, 2L, avaliacao("-0.5")));
        assertThrows(IllegalArgumentException.class, () -> avaliacaoBO.cadastrar(1L, 2L, avaliacao("5.5")));
        assertThrows(IllegalArgumentException.class, () -> avaliacaoBO.cadastrar(1L, 2L, avaliacao("3.2")));

        Avaliacao semNota = new Avaliacao();
        assertThrows(IllegalArgumentException.class, () -> avaliacaoBO.cadastrar(1L, 2L, semNota));
    }

    @Test
    void rejeitaCategoriasDuplicadasOuSemNota() {
        Avaliacao duplicada = avaliacao("4.0");
        duplicada.setNotasCategorias(List.of(
                notaCategoria(CategoriaAcessibilidade.FISICA, "3.0"),
                notaCategoria(CategoriaAcessibilidade.FISICA, "4.0")));
        assertThrows(IllegalArgumentException.class, () -> avaliacaoBO.cadastrar(1L, 2L, duplicada));

        Avaliacao semNotaCategoria = avaliacao("4.0");
        semNotaCategoria.setNotasCategorias(List.of(notaCategoria(CategoriaAcessibilidade.AUDITIVA, null)));
        assertThrows(IllegalArgumentException.class, () -> avaliacaoBO.cadastrar(1L, 2L, semNotaCategoria));
    }

    @Test
    void trataUsuarioInexistenteERestauranteSemVisibilidadePublica() {
        when(restauranteBO.buscarPublicoPorId(1L)).thenReturn(restaurante(1L, true, true));
        when(usuarioBO.buscarPorId(99L)).thenThrow(new UsuarioNaoEncontradoException("Usuário não encontrado"));
        assertThrows(UsuarioNaoEncontradoException.class, () -> avaliacaoBO.cadastrar(1L, 99L, avaliacao("4.0")));

        when(restauranteBO.buscarPublicoPorId(2L))
                .thenThrow(new RestauranteNaoEncontradoException("Restaurante não encontrado"));
        assertThrows(RestauranteNaoEncontradoException.class, () -> avaliacaoBO.cadastrar(2L, 2L, avaliacao("4.0")));
    }

    @Test
    void preservaVinculoComRestauranteOriginal() {
        Restaurante restauranteOriginal = restaurante(1L, true, true);
        Avaliacao avaliacao = avaliacao("3.5");
        avaliacao.setRestaurante(restaurante(99L, true, true));
        when(restauranteBO.buscarPublicoPorId(1L)).thenReturn(restauranteOriginal);
        when(usuarioBO.buscarPorId(2L)).thenReturn(usuario(2L));

        Avaliacao resultado = avaliacaoBO.cadastrar(1L, 2L, avaliacao);

        assertSame(restauranteOriginal, resultado.getRestaurante());
        assertEquals(1L, resultado.getRestaurante().getIdRestaurante());
    }

    @Test
    void mantemColecaoMutavelEVinculoBidirecionalDasCategorias() {
        Avaliacao avaliacao = new Avaliacao();
        AvaliacaoCategoria fisica = notaCategoria(CategoriaAcessibilidade.FISICA, "3.0");
        AvaliacaoCategoria visual = notaCategoria(CategoriaAcessibilidade.VISUAL, "4.0");

        avaliacao.setNotasCategorias(List.of(fisica));
        avaliacao.adicionarNotaCategoria(visual);
        avaliacao.getNotasCategorias().add(notaCategoria(CategoriaAcessibilidade.AUDITIVA, "2.0"));

        assertEquals(3, avaliacao.getNotasCategorias().size());
        assertSame(avaliacao, fisica.getAvaliacao());
        assertSame(avaliacao, visual.getAvaliacao());
    }

    private Avaliacao avaliacao(String nota) {
        Avaliacao avaliacao = new Avaliacao();
        if (nota != null) {
            avaliacao.setNotaGeral(new BigDecimal(nota));
        }
        return avaliacao;
    }

    private AvaliacaoCategoria notaCategoria(CategoriaAcessibilidade categoria, String nota) {
        AvaliacaoCategoria avaliacaoCategoria = new AvaliacaoCategoria();
        avaliacaoCategoria.setCategoria(categoria);
        if (nota != null) {
            avaliacaoCategoria.setNota(new BigDecimal(nota));
        }
        return avaliacaoCategoria;
    }

    private Restaurante restaurante(Long id, boolean ativo, boolean publicado) {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(id);
        restaurante.setAtivo(ativo);
        restaurante.setPublicado(publicado);
        return restaurante;
    }

    private Usuario usuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        return usuario;
    }
}
