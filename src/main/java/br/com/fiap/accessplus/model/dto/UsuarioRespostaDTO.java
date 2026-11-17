package br.com.fiap.accessplus.model.dto;

import br.com.fiap.accessplus.model.entity.TipoUsuario;
import br.com.fiap.accessplus.model.entity.Usuario;

import java.time.LocalDateTime;

public class UsuarioRespostaDTO {

    private Long idUsuario;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private LocalDateTime dataCadastro;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public static UsuarioRespostaDTO from(Usuario usuario) {
        UsuarioRespostaDTO resposta = new UsuarioRespostaDTO();
        resposta.setIdUsuario(usuario.getIdUsuario());
        resposta.setNome(usuario.getNome());
        resposta.setEmail(usuario.getEmail());
        resposta.setTipoUsuario(usuario.getTipoUsuario());
        resposta.setDataCadastro(usuario.getDataCadastro());
        return resposta;
    }
}
