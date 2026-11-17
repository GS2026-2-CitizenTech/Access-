package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.UsuarioBO;
import br.com.fiap.accessplus.model.dto.UsuarioCadastroDTO;
import br.com.fiap.accessplus.model.dto.UsuarioRespostaDTO;
import br.com.fiap.accessplus.model.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioBO usuarioBO;

    public UsuarioController(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }

    @PostMapping
    public ResponseEntity<UsuarioRespostaDTO> cadastrar(@RequestBody UsuarioCadastroDTO dadosCadastro) {
        Usuario usuarioCadastrado = usuarioBO.cadastrar(paraEntidade(dadosCadastro), dadosCadastro.getSenha());
        return ResponseEntity.status(201).body(UsuarioRespostaDTO.from(usuarioCadastrado));
    }

    @GetMapping
    public List<UsuarioRespostaDTO> listarTodos() {
        return usuarioBO.listarTodos().stream()
                .map(UsuarioRespostaDTO::from)
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioRespostaDTO buscarPorId(@PathVariable Long id) {
        return UsuarioRespostaDTO.from(usuarioBO.buscarPorId(id));
    }

    private Usuario paraEntidade(UsuarioCadastroDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do usuário não informados");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTipoUsuario(dto.getTipoUsuario());
        return usuario;
    }
}
