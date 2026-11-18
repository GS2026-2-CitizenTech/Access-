package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.VerificacaoRecursoBO;
import br.com.fiap.accessplus.model.dto.VerificacaoRecursoCadastroDTO;
import br.com.fiap.accessplus.model.dto.VerificacaoRecursoRespostaDTO;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VerificacaoRecursoController {

    private final VerificacaoRecursoBO verificacaoRecursoBO;

    public VerificacaoRecursoController(VerificacaoRecursoBO verificacaoRecursoBO) {
        this.verificacaoRecursoBO = verificacaoRecursoBO;
    }

    @PostMapping("/avaliacoes/{idAvaliacao}/verificacoes")
    public ResponseEntity<VerificacaoRecursoRespostaDTO> cadastrar(@PathVariable Long idAvaliacao,
                                                                     @Valid @RequestBody VerificacaoRecursoCadastroDTO dadosCadastro) {
        VerificacaoRecurso cadastrada = verificacaoRecursoBO.cadastrar(idAvaliacao, paraEntidade(dadosCadastro));
        return ResponseEntity.status(201).body(VerificacaoRecursoRespostaDTO.from(cadastrada));
    }

    @GetMapping("/avaliacoes/{idAvaliacao}/verificacoes")
    public List<VerificacaoRecursoRespostaDTO> listarPorAvaliacao(@PathVariable Long idAvaliacao) {
        return verificacaoRecursoBO.listarPublicasDaAvaliacao(idAvaliacao).stream()
                .map(VerificacaoRecursoRespostaDTO::from)
                .toList();
    }

    @GetMapping("/verificacoes/{idVerificacao}")
    public VerificacaoRecursoRespostaDTO buscarPorId(@PathVariable Long idVerificacao) {
        return VerificacaoRecursoRespostaDTO.from(verificacaoRecursoBO.buscarPublicaPorId(idVerificacao));
    }

    private VerificacaoRecurso paraEntidade(VerificacaoRecursoCadastroDTO dto) {
        VerificacaoRecurso verificacao = new VerificacaoRecurso();
        verificacao.setResultado(dto.getResultado());
        verificacao.setComentario(dto.getComentario());

        RestauranteRecurso restauranteRecurso = new RestauranteRecurso();
        restauranteRecurso.setIdRestauranteRecurso(dto.getIdRestauranteRecurso());
        verificacao.setRestauranteRecurso(restauranteRecurso);
        return verificacao;
    }
}
