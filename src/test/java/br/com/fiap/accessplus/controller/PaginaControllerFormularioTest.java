package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.AvaliacaoBO;
import br.com.fiap.accessplus.bo.FormularioAvaliacaoBO;
import br.com.fiap.accessplus.bo.RecursoAcessibilidadeBO;
import br.com.fiap.accessplus.bo.RestauranteBO;
import br.com.fiap.accessplus.bo.RestauranteRecursoBO;
import br.com.fiap.accessplus.bo.UsuarioBO;
import br.com.fiap.accessplus.bo.VerificacaoRecursoBO;
import br.com.fiap.accessplus.model.dto.FormularioAvaliacaoDTO;
import br.com.fiap.accessplus.model.entity.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class PaginaControllerFormularioTest {

    private RestauranteBO restauranteBO;
    private RestauranteRecursoBO restauranteRecursoBO;
    private UsuarioBO usuarioBO;
    private FormularioAvaliacaoBO formularioBO;
    private PaginaController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        restauranteBO = mock(RestauranteBO.class);
        restauranteRecursoBO = mock(RestauranteRecursoBO.class);
        usuarioBO = mock(UsuarioBO.class);
        formularioBO = mock(FormularioAvaliacaoBO.class);
        controller = new PaginaController(restauranteBO, mock(RecursoAcessibilidadeBO.class), restauranteRecursoBO,
                mock(AvaliacaoBO.class), mock(VerificacaoRecursoBO.class), usuarioBO, formularioBO);

        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(10L);
        when(restauranteBO.buscarPublicoPorId(10L)).thenReturn(restaurante);
        when(restauranteRecursoBO.listarDeclaracoesPublicasDoRestaurante(10L)).thenReturn(List.of());
        when(usuarioBO.listarTodos()).thenReturn(List.of());

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setValidator(validator).build();
    }

    @Test
    void redirecionaDepoisDoCadastroComSucesso() {
        FormularioAvaliacaoDTO formulario = formularioValido();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String view = controller.salvarAvaliacao(10L, formulario,
                new BeanPropertyBindingResult(formulario, "formularioAvaliacao"),
                new ExtendedModelMap(), redirectAttributes);

        assertEquals("redirect:/app/restaurantes/10", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("sucesso"));
        verify(formularioBO).cadastrar(10L, formulario);
    }

    @Test
    void reapresentaFormularioComDadosQuandoHaErroDeValidacao() {
        FormularioAvaliacaoDTO formulario = formularioValido();
        formulario.setComentario("relato");
        BeanPropertyBindingResult erros = new BeanPropertyBindingResult(formulario, "formularioAvaliacao");
        erros.rejectValue("notaGeral", "nota", "A nota geral é obrigatória");
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.salvarAvaliacao(10L, formulario, erros, model, new RedirectAttributesModelMap());

        assertEquals("avaliar-restaurante", view);
        assertEquals(formulario, model.getAttribute("formularioAvaliacao"));
        assertTrue(((List<?>) model.getAttribute("errosFormulario")).contains("A nota geral é obrigatória"));
        verify(formularioBO, never()).cadastrar(any(), any());
    }

    @Test
    void reapresentaErroDeRegraDeNegocioSemRedirecionar() {
        FormularioAvaliacaoDTO formulario = formularioValido();
        when(formularioBO.cadastrar(eq(10L), any(FormularioAvaliacaoDTO.class)))
                .thenThrow(new IllegalArgumentException("Nota geral deve utilizar incrementos de 0,5"));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.salvarAvaliacao(10L, formulario,
                new BeanPropertyBindingResult(formulario, "formularioAvaliacao"), model,
                new RedirectAttributesModelMap());

        assertEquals("avaliar-restaurante", view);
        assertTrue(((List<?>) model.getAttribute("errosFormulario"))
                .contains("Nota geral deve utilizar incrementos de 0,5"));
    }

    @Test
    void beanValidationRejeitaDadosObrigatoriosAusentes() throws Exception {
        mockMvc.perform(post("/app/restaurantes/10/avaliar")
                        .param("idUsuario", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("avaliar-restaurante"))
                .andExpect(model().attributeExists("errosFormulario"));

        verify(formularioBO, never()).cadastrar(any(), any());
    }

    @Test
    void bindingRejeitaDataInvalidaSemCadastrar() throws Exception {
        mockMvc.perform(post("/app/restaurantes/10/avaliar")
                        .param("idUsuario", "2")
                        .param("notaGeral", "4.5")
                        .param("dataVisita", "data-invalida"))
                .andExpect(status().isOk())
                .andExpect(view().name("avaliar-restaurante"))
                .andExpect(model().attributeExists("errosFormulario"));

        verify(formularioBO, never()).cadastrar(any(), any());
    }

    private FormularioAvaliacaoDTO formularioValido() {
        FormularioAvaliacaoDTO formulario = new FormularioAvaliacaoDTO();
        formulario.setIdUsuario(2L);
        formulario.setNotaGeral(new BigDecimal("4.5"));
        return formulario;
    }
}
