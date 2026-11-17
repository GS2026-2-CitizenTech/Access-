package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.AvaliacaoBO;
import br.com.fiap.accessplus.exception.AvaliacaoNaoEncontradaException;
import br.com.fiap.accessplus.exception.GlobalExceptionHandler;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.AvaliacaoCategoria;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AvaliacaoControllerTest {

    private AvaliacaoBO avaliacaoBO;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        avaliacaoBO = mock(AvaliacaoBO.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new AvaliacaoController(avaliacaoBO))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void cadastraAvaliacaoValidaERetornaDto() throws Exception {
        when(avaliacaoBO.cadastrar(eq(1L), eq(2L), any(Avaliacao.class))).thenReturn(avaliacao());

        mockMvc.perform(post("/restaurantes/1/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idUsuario\":2,\"notaGeral\":4.5,\"comentario\":\"Muito bom\",\"notasCategorias\":[{\"categoria\":\"FISICA\",\"nota\":4.0}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAvaliacao").value(10))
                .andExpect(jsonPath("$.notaGeral").value(4.5))
                .andExpect(jsonPath("$.notasCategorias[0].categoria").value("FISICA"));
    }

    @Test
    void beanValidationRejeitaNotaAusenteENegativa() throws Exception {
        mockMvc.perform(post("/restaurantes/1/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idUsuario\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Nota geral é obrigatória")));

        mockMvc.perform(post("/restaurantes/1/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idUsuario\":2,\"notaGeral\":-0.5}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void trataAvaliacaoInexistenteEListaPublica() throws Exception {
        when(avaliacaoBO.buscarPublicaPorId(99L))
                .thenThrow(new AvaliacaoNaoEncontradaException("Avaliação não encontrada"));
        when(avaliacaoBO.listarPublicasDoRestaurante(1L)).thenReturn(List.of(avaliacao()));

        mockMvc.perform(get("/avaliacoes/99"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/restaurantes/1/avaliacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRestaurante").value(1));
    }

    private Avaliacao avaliacao() {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1L);
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(2L);
        AvaliacaoCategoria categoria = new AvaliacaoCategoria();
        categoria.setCategoria(CategoriaAcessibilidade.FISICA);
        categoria.setNota(new BigDecimal("4.0"));
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(10L);
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
        avaliacao.setNotaGeral(new BigDecimal("4.5"));
        avaliacao.setComentario("Muito bom");
        avaliacao.setDataCriacao(LocalDateTime.of(2026, 11, 17, 14, 0));
        avaliacao.setNotasCategorias(List.of(categoria));
        return avaliacao;
    }
}
