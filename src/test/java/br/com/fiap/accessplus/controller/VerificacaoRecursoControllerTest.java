package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.VerificacaoRecursoBO;
import br.com.fiap.accessplus.exception.AvaliacaoNaoEncontradaException;
import br.com.fiap.accessplus.exception.GlobalExceptionHandler;
import br.com.fiap.accessplus.exception.VerificacaoRecursoNaoEncontradaException;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.ResultadoVerificacao;
import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

class VerificacaoRecursoControllerTest {

    private VerificacaoRecursoBO verificacaoBO;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        verificacaoBO = mock(VerificacaoRecursoBO.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new VerificacaoRecursoController(verificacaoBO))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void cadastraVerificacaoValidaERetornaDto() throws Exception {
        when(verificacaoBO.cadastrar(eq(1L), any(VerificacaoRecurso.class))).thenReturn(verificacao());

        mockMvc.perform(post("/avaliacoes/1/verificacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idRestauranteRecurso\":2,\"resultado\":\"CONFIRMADO\",\"comentario\":\"Rampa encontrada\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVerificacao").value(3))
                .andExpect(jsonPath("$.idAvaliacao").value(1))
                .andExpect(jsonPath("$.idRecurso").value(5))
                .andExpect(jsonPath("$.resultado").value("CONFIRMADO"));
    }

    @Test
    void beanValidationRejeitaResultadoAusenteEComentarioGrande() throws Exception {
        mockMvc.perform(post("/avaliacoes/1/verificacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idRestauranteRecurso\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Resultado da verificação é obrigatório")));

        mockMvc.perform(post("/avaliacoes/1/verificacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idRestauranteRecurso\":2,\"resultado\":\"CONFIRMADO\",\"comentario\":\"" + "a".repeat(501) + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Comentário deve ter no máximo 500 caracteres")));
    }

    @Test
    void corpoComResultadoInvalidoRetornaBadRequest() throws Exception {
        mockMvc.perform(post("/avaliacoes/1/verificacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idRestauranteRecurso\":2,\"resultado\":\"VALIDADO_PELA_COMUNIDADE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listaSomenteVerificacoesDeAvaliacaoPublica() throws Exception {
        when(verificacaoBO.listarPublicasDaAvaliacao(1L)).thenReturn(List.of(verificacao()));

        mockMvc.perform(get("/avaliacoes/1/verificacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resultado").value("CONFIRMADO"));

        when(verificacaoBO.listarPublicasDaAvaliacao(99L))
                .thenThrow(new AvaliacaoNaoEncontradaException("Avaliação não encontrada"));
        mockMvc.perform(get("/avaliacoes/99/verificacoes"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscaPorIdERetornaNotFoundQuandoNecessario() throws Exception {
        when(verificacaoBO.buscarPublicaPorId(3L)).thenReturn(verificacao());
        mockMvc.perform(get("/verificacoes/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeRecurso").value("Rampa de acesso"));

        when(verificacaoBO.buscarPublicaPorId(99L))
                .thenThrow(new VerificacaoRecursoNaoEncontradaException("Verificação não encontrada"));
        mockMvc.perform(get("/verificacoes/99"))
                .andExpect(status().isNotFound());
    }

    private VerificacaoRecurso verificacao() {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1L);
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(1L);
        avaliacao.setRestaurante(restaurante);
        RecursoAcessibilidade recurso = new RecursoAcessibilidade();
        recurso.setIdRecurso(5L);
        recurso.setNome("Rampa de acesso");
        recurso.setCategoria(CategoriaAcessibilidade.FISICA);
        RestauranteRecurso declaracao = new RestauranteRecurso();
        declaracao.setIdRestauranteRecurso(2L);
        declaracao.setRestaurante(restaurante);
        declaracao.setRecurso(recurso);
        VerificacaoRecurso verificacao = new VerificacaoRecurso();
        verificacao.setIdVerificacao(3L);
        verificacao.setAvaliacao(avaliacao);
        verificacao.setRestauranteRecurso(declaracao);
        verificacao.setResultado(ResultadoVerificacao.CONFIRMADO);
        verificacao.setComentario("Rampa encontrada");
        verificacao.setDataVerificacao(LocalDateTime.of(2026, 11, 18, 10, 30));
        return verificacao;
    }
}
