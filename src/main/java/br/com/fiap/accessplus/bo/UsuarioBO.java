package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.EmailJaCadastradoException;
import br.com.fiap.accessplus.exception.UsuarioNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.Usuario;
import br.com.fiap.accessplus.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class UsuarioBO {

    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioBO(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario cadastrar(Usuario usuario, String senha) {
        validarDadosCadastro(usuario, senha);

        String emailNormalizado = usuario.getEmail().trim().toLowerCase(Locale.ROOT);
        if (usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)) {
            throw new EmailJaCadastradoException("E-mail já cadastrado");
        }

        usuario.setEmail(emailNormalizado);
        usuario.setSenhaHash(passwordEncoder.encode(senha));
        usuario.setDataCadastro(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id não informado");
        }

        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    private void validarDadosCadastro(Usuario usuario, String senha) {
        if (usuario == null) {
            throw new IllegalArgumentException("Dados do usuário não informados");
        }
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("E-mail não pode estar vazio");
        }
        if (!usuario.getEmail().trim().matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("E-mail inválido");
        }
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha não pode estar vazia");
        }
        if (usuario.getTipoUsuario() == null) {
            throw new IllegalArgumentException("Tipo de usuário inválido");
        }
    }
}
