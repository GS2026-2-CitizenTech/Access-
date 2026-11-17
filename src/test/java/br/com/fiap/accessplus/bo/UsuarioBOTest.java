package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.EmailJaCadastradoException;
import br.com.fiap.accessplus.exception.UsuarioNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.TipoUsuario;
import br.com.fiap.accessplus.model.entity.Usuario;
import br.com.fiap.accessplus.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioBOTest {

    private UsuarioRepository repository;
    private UsuarioBO usuarioBO;

    @BeforeEach
    void setup() {
        repository = mock(UsuarioRepository.class);
        usuarioBO = new UsuarioBO(repository);
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void cadastraUsuarioComSenhaEmHash() {
        when(repository.existsByEmailIgnoreCase("ana@exemplo.com")).thenReturn(false);

        Usuario resultado = usuarioBO.cadastrar(novoUsuario(), "senha-segura");

        assertNotNull(resultado.getDataCadastro());
        assertNotEquals("senha-segura", resultado.getSenhaHash());
        assertTrue(new BCryptPasswordEncoder().matches("senha-segura", resultado.getSenhaHash()));
    }

    @Test
    void rejeitaEmailInvalido() {
        Usuario usuario = novoUsuario();
        usuario.setEmail("ana-sem-dominio");

        assertThrows(IllegalArgumentException.class, () -> usuarioBO.cadastrar(usuario, "senha-segura"));
    }

    @Test
    void rejeitaEmailDuplicadoSemDiferenciarMaiusculas() {
        when(repository.existsByEmailIgnoreCase("ana@exemplo.com")).thenReturn(true);

        assertThrows(EmailJaCadastradoException.class,
                () -> usuarioBO.cadastrar(novoUsuario(), "senha-segura"));
    }

    @Test
    void rejeitaCamposObrigatoriosETipoInvalido() {
        Usuario semNome = novoUsuario();
        semNome.setNome(" ");
        assertThrows(IllegalArgumentException.class, () -> usuarioBO.cadastrar(semNome, "senha-segura"));

        Usuario semEmail = novoUsuario();
        semEmail.setEmail(null);
        assertThrows(IllegalArgumentException.class, () -> usuarioBO.cadastrar(semEmail, "senha-segura"));

        Usuario semTipo = novoUsuario();
        semTipo.setTipoUsuario(null);
        assertThrows(IllegalArgumentException.class, () -> usuarioBO.cadastrar(semTipo, "senha-segura"));

        assertThrows(IllegalArgumentException.class, () -> usuarioBO.cadastrar(novoUsuario(), " "));
    }

    @Test
    void informaQuandoUsuarioNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioBO.buscarPorId(99L));
    }

    @Test
    void normalizaEmailAntesDeSalvar() {
        Usuario usuario = novoUsuario();
        usuario.setEmail(" ANA@EXEMPLO.COM ");
        when(repository.existsByEmailIgnoreCase("ana@exemplo.com")).thenReturn(false);

        Usuario resultado = usuarioBO.cadastrar(usuario, "senha-segura");

        assertFalse(resultado.getEmail().chars().anyMatch(Character::isUpperCase));
    }

    private Usuario novoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Ana Silva");
        usuario.setEmail("ana@exemplo.com");
        usuario.setTipoUsuario(TipoUsuario.CONSUMIDOR);
        return usuario;
    }
}
