package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.RestauranteRecursoBO;
import br.com.fiap.accessplus.exception.GlobalExceptionHandler;
import br.com.fiap.accessplus.exception.RestauranteNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.StatusDeclaracao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

class RestauranteRecursoControllerTest {

    private RestauranteRecursoBO restauranteRecursoBO;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        restauranteRecursoBO = mock(RestauranteRecursoBO.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new RestauranteRecursoController(restauranteRecursoBO))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void cadastraDeclaracaoValida() throws Exception {
        when(restauranteRecursoBO.declarar(eq(1L), eq(2L), eq(StatusDeclaracao.SIM)))
                .thenReturn(declaracao(StatusDeclaracao.SIM));

        mockMvc.perform(put("/restaurantes/1/recursos-acessibilidade/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statusDeclaracao\":\"SIM\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDeclaracao").value("SIM"))
                .andExpect(jsonPath("$.autodeclarado").value(true));
    }

    @Test
    void rejeitaStatusAusenteETrataRestauranteInexistente() throws Exception {
        mockMvc.perform(put("/restaurantes/1/recursos-acessibilidade/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Status da declaração é obrigatório")));

        when(restauranteRecursoBO.declarar(eq(99L), eq(2L), eq(StatusDeclaracao.SIM)))
                .thenThrow(new RestauranteNaoEncontradoException("Restaurante não encontrado"));
        mockMvc.perform(put("/restaurantes/99/recursos-acessibilidade/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statusDeclaracao\":\"SIM\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listaDeclaracaoExplicitaNaoInformado() throws Exception {
        when(restauranteRecursoBO.listarDeclaracoesPublicasDoRestaurante(1L))
                .thenReturn(List.of(declaracao(StatusDeclaracao.NAO_INFORMADO)));

        mockMvc.perform(get("/restaurantes/1/recursos-acessibilidade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statusDeclaracao").value("NAO_INFORMADO"));
    }

    private RestauranteRecurso declaracao(StatusDeclaracao status) {
        RecursoAcessibilidade recurso = new RecursoAcessibilidade();
        recurso.setIdRecurso(2L);
        recurso.setNome("Rampa de acesso");
        recurso.setCategoria(CategoriaAcessibilidade.FISICA);
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1L);
        RestauranteRecurso declaracao = new RestauranteRecurso();
        declaracao.setIdRestauranteRecurso(3L);
        declaracao.setRestaurante(restaurante);
        declaracao.setRecurso(recurso);
        declaracao.setStatusDeclaracao(status);
        declaracao.setAutodeclarado(true);
        return declaracao;
    }
}
