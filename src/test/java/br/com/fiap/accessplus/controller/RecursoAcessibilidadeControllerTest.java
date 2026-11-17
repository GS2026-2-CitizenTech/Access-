package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.RecursoAcessibilidadeBO;
import br.com.fiap.accessplus.bo.RestauranteRecursoBO;
import br.com.fiap.accessplus.exception.GlobalExceptionHandler;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecursoAcessibilidadeControllerTest {

    private RecursoAcessibilidadeBO recursoBO;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        recursoBO = mock(RecursoAcessibilidadeBO.class);
        RestauranteRecursoBO restauranteRecursoBO = mock(RestauranteRecursoBO.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new RecursoAcessibilidadeController(recursoBO, restauranteRecursoBO))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listaRecursosDoCatalogo() throws Exception {
        when(recursoBO.listarTodos()).thenReturn(List.of(recurso(1L, "Rampa de acesso", CategoriaAcessibilidade.FISICA)));

        mockMvc.perform(get("/recursos-acessibilidade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRecurso").value(1))
                .andExpect(jsonPath("$[0].categoria").value("FISICA"));
    }

    @Test
    void filtraRecursosPorCategoriaERejeitaCategoriaInvalida() throws Exception {
        when(recursoBO.listarPorCategoria(CategoriaAcessibilidade.VISUAL))
                .thenReturn(List.of(recurso(2L, "Cardápio em braile", CategoriaAcessibilidade.VISUAL)));

        mockMvc.perform(get("/recursos-acessibilidade").param("categoria", "VISUAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Cardápio em braile"));

        mockMvc.perform(get("/recursos-acessibilidade").param("categoria", "INVALIDA"))
                .andExpect(status().isBadRequest());
    }

    private RecursoAcessibilidade recurso(Long id, String nome, CategoriaAcessibilidade categoria) {
        RecursoAcessibilidade recurso = new RecursoAcessibilidade();
        recurso.setIdRecurso(id);
        recurso.setNome(nome);
        recurso.setCategoria(categoria);
        return recurso;
    }
}
