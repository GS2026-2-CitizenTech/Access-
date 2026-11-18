package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.RestauranteBO;
import br.com.fiap.accessplus.bo.UsuarioBO;
import br.com.fiap.accessplus.exception.GlobalExceptionHandler;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.repository.RestauranteRepository;
import br.com.fiap.accessplus.model.entity.Usuario;
import br.com.fiap.accessplus.model.entity.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestauranteControllerTest {

    private RestauranteRepository repository;
    private UsuarioBO usuarioBO;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        repository = mock(RestauranteRepository.class);
        usuarioBO = mock(UsuarioBO.class);
        when(repository.save(any(Restaurante.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RestauranteController controller = new RestauranteController(new RestauranteBO(repository, usuarioBO));
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void cadastraERetornaDtoComStatusCreated() throws Exception {
        when(repository.save(any(Restaurante.class))).thenAnswer(invocation -> {
            Restaurante restaurante = invocation.getArgument(0);
            restaurante.setIdRestaurante(1L);
            return restaurante;
        });
        Usuario responsavel = new Usuario(); responsavel.setTipoUsuario(TipoUsuario.RESTAURANTE);
        when(usuarioBO.buscarPorId(10L)).thenReturn(responsavel);

        mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Bistrô Central\",\"bairro\":\"Centro\",\"endereco\":\"Rua das Flores, 10\",\"descricao\":\"Cozinha brasileira\",\"idResponsavel\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRestaurante").value(1))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.publicado").value(false));
    }

    @Test
    void retornaBadRequestNoCadastroInvalido() throws Exception {
        mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Bistrô Central\",\"endereco\":\"Rua das Flores, 10\",\"descricao\":\"Cozinha brasileira\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizaPorPutEPatchESempreRetornaDto() throws Exception {
        Restaurante existente = restauranteComId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        mockMvc.perform(put("/restaurantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Novo Nome\",\"bairro\":\"Jardins\",\"endereco\":\"Av. Brasil, 50\",\"descricao\":\"Ambiente atualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Nome"))
                .andExpect(jsonPath("$.endereco").value("Av. Brasil, 50"));

        mockMvc.perform(patch("/restaurantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"Nova descrição\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Nova descrição"))
                .andExpect(jsonPath("$.bairro").value("Jardins"));
    }

    @Test
    void exclusaoLogicaRetornaNoContent() throws Exception {
        Restaurante existente = restauranteComId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        mockMvc.perform(delete("/restaurantes/1"))
                .andExpect(status().isNoContent());

        verify(repository).save(existente);
        verify(repository, never()).deleteById(1L);
        org.junit.jupiter.api.Assertions.assertFalse(existente.isAtivo());
    }

    @Test
    void consultaPublicaOcultaRestauranteNaoVisivel() throws Exception {
        Restaurante publico = restauranteComId(1L);
        publico.setPublicado(true);
        when(repository.findByAtivoTrueAndPublicadoTrue()).thenReturn(List.of(publico));
        when(repository.findByIdRestauranteAndAtivoTrueAndPublicadoTrue(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRestaurante").value(1));

        mockMvc.perform(get("/restaurantes/2"))
                .andExpect(status().isNotFound());
    }

    private Restaurante restauranteComId(Long id) {
        Restaurante restaurante = new Restaurante("Bistrô Central", "Rua das Flores, 10", "Centro",
                "Cozinha brasileira", false, true, null);
        restaurante.setIdRestaurante(id);
        return restaurante;
    }
}
