package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.UsuarioBO;
import br.com.fiap.accessplus.exception.GlobalExceptionHandler;
import br.com.fiap.accessplus.model.entity.TipoUsuario;
import br.com.fiap.accessplus.model.entity.Usuario;
import br.com.fiap.accessplus.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerTest {

    private UsuarioRepository repository;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        repository = mock(UsuarioRepository.class);
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UsuarioController controller = new UsuarioController(new UsuarioBO(repository));
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void cadastraESeguroNaoRetornarSenhaOuHash() throws Exception {
        when(repository.existsByEmailIgnoreCase("ana@exemplo.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setIdUsuario(1L);
            return usuario;
        });

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana Silva\",\"email\":\"ana@exemplo.com\",\"senha\":\"senha-segura\",\"tipoUsuario\":\"CONSUMIDOR\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.email").value("ana@exemplo.com"))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(jsonPath("$.senhaHash").doesNotExist());
    }

    @Test
    void retornaBadRequestParaEmailInvalido() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana Silva\",\"email\":\"email-invalido\",\"senha\":\"senha-segura\",\"tipoUsuario\":\"CONSUMIDOR\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void retornaConflictParaEmailDuplicado() throws Exception {
        when(repository.existsByEmailIgnoreCase("ana@exemplo.com")).thenReturn(true);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana Silva\",\"email\":\"ana@exemplo.com\",\"senha\":\"senha-segura\",\"tipoUsuario\":\"CONSUMIDOR\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void retornaNotFoundParaUsuarioInexistente() throws Exception {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listaUsuariosComDtoSemSenha() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNome("Ana Silva");
        usuario.setEmail("ana@exemplo.com");
        usuario.setSenhaHash("hash-nao-exposto");
        usuario.setTipoUsuario(TipoUsuario.CONSUMIDOR);
        usuario.setDataCadastro(LocalDateTime.of(2026, 11, 17, 10, 0));
        when(repository.findAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].senha").doesNotExist())
                .andExpect(jsonPath("$[0].senhaHash").doesNotExist());
    }
}
