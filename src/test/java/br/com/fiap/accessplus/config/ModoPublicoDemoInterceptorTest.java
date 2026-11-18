package br.com.fiap.accessplus.config;

import br.com.fiap.accessplus.bo.AvaliacaoBO;
import br.com.fiap.accessplus.bo.FormularioAvaliacaoBO;
import br.com.fiap.accessplus.bo.RecursoAcessibilidadeBO;
import br.com.fiap.accessplus.bo.RestauranteBO;
import br.com.fiap.accessplus.bo.RestauranteRecursoBO;
import br.com.fiap.accessplus.bo.UsuarioBO;
import br.com.fiap.accessplus.bo.VerificacaoRecursoBO;
import br.com.fiap.accessplus.controller.PaginaController;
import br.com.fiap.accessplus.controller.RestauranteController;
import br.com.fiap.accessplus.controller.UsuarioController;
import br.com.fiap.accessplus.model.entity.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ModoPublicoDemoInterceptorTest {

    private RestauranteBO restauranteBO;
    private UsuarioBO usuarioBO;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        restauranteBO = mock(RestauranteBO.class);
        usuarioBO = mock(UsuarioBO.class);
        when(restauranteBO.listarPublicos()).thenReturn(List.of(restaurantePublico()));

        PaginaController paginaController = new PaginaController(restauranteBO,
                mock(RecursoAcessibilidadeBO.class), mock(RestauranteRecursoBO.class),
                mock(AvaliacaoBO.class), mock(VerificacaoRecursoBO.class), usuarioBO,
                mock(FormularioAvaliacaoBO.class));

        mockMvc = MockMvcBuilders.standaloneSetup(
                        new RestauranteController(restauranteBO),
                        new UsuarioController(usuarioBO),
                        paginaController)
                .addInterceptors(new ModoPublicoDemoInterceptor(true))
                .build();
    }

    @Test
    void permiteConsultaPublicaDeRestaurantes() throws Exception {
        mockMvc.perform(get("/restaurantes"))
                .andExpect(status().isOk());
    }

    @Test
    void bloqueiaEscritaRestEmModoPublico() throws Exception {
        mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        verify(restauranteBO, never()).cadastrar(any(), any());
    }

    @Test
    void bloqueiaPostDoFormularioThymeleafEmModoPublico() throws Exception {
        mockMvc.perform(post("/app/restaurantes/1/avaliar")
                        .param("idUsuario", "2")
                        .param("notaGeral", "4.5"))
                .andExpect(status().isForbidden());
    }

    @Test
    void bloqueiaConsultasAdministrativasDeUsuarios() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isForbidden());

        verify(usuarioBO, never()).listarTodos();
    }

    @Test
    void modoLocalSemFlagMantemEscritaDisponivel() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/restaurantes");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(new ModoPublicoDemoInterceptor(false).preHandle(request, response, new Object()));
    }

    private Restaurante restaurantePublico() {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1L);
        restaurante.setNome("Restaurante público");
        restaurante.setBairro("Centro");
        restaurante.setEndereco("Rua A");
        restaurante.setDescricao("Descrição");
        restaurante.setAtivo(true);
        restaurante.setPublicado(true);
        return restaurante;
    }
}
