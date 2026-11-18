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
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.model.entity.StatusDeclaracao;
import br.com.fiap.accessplus.model.entity.TipoUsuario;
import br.com.fiap.accessplus.model.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TemplateAvaliacaoRenderTest {

    @Test
    void renderizaFormularioDeAvaliacaoComModeloDaPagina() {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(1L);
        restaurante.setNome("Restaurante de teste");

        RestauranteBO restauranteBO = mock(RestauranteBO.class);
        RestauranteRecursoBO restauranteRecursoBO = mock(RestauranteRecursoBO.class);
        UsuarioBO usuarioBO = mock(UsuarioBO.class);
        when(restauranteBO.buscarPublicoPorId(1L)).thenReturn(restaurante);
        RestauranteRecurso declaracao = new RestauranteRecurso();
        declaracao.setIdRestauranteRecurso(9L);
        declaracao.setStatusDeclaracao(StatusDeclaracao.SIM);
        RecursoAcessibilidade recurso = new RecursoAcessibilidade();
        recurso.setNome("Entrada acessível");
        declaracao.setRecurso(recurso);
        when(restauranteRecursoBO.listarDeclaracoesPublicasDoRestaurante(1L)).thenReturn(List.of(declaracao));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(2L);
        usuario.setTipoUsuario(TipoUsuario.CONSUMIDOR);
        when(usuarioBO.listarTodos()).thenReturn(List.of(usuario));

        PaginaController paginaController = new PaginaController(restauranteBO,
                mock(RecursoAcessibilidadeBO.class), restauranteRecursoBO,
                mock(AvaliacaoBO.class), mock(VerificacaoRecursoBO.class), usuarioBO,
                mock(FormularioAvaliacaoBO.class));
        ExtendedModelMap model = new ExtendedModelMap();
        FormularioAvaliacaoDTO formulario = new FormularioAvaliacaoDTO();
        assertEquals("avaliar-restaurante", paginaController.avaliar(1L, formulario, model));

        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebContext context = new WebContext(
                JakartaServletWebApplication.buildApplication(servletContext).buildExchange(request, response),
                Locale.forLanguageTag("pt-BR"),
                model);

        String html = templateEngine().process("avaliar-restaurante", context);

        assertTrue(html.contains("Demonstração acadêmica local"));
        assertTrue(html.contains("Enviar avaliação"));
        assertTrue(html.contains("name=\"notaGeral\""));
        assertTrue(html.contains("value=\"0\""));
        assertTrue(html.contains("value=\"0.5\""));
        assertTrue(html.contains("value=\"5\""));
        assertTrue(html.contains("Não informar"));
        assertTrue(html.contains("Não registrar verificação"));
    }

    private SpringTemplateEngine templateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }
}
