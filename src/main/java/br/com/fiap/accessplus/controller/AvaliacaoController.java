package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.AvaliacaoBO;
import br.com.fiap.accessplus.model.dto.AvaliacaoCadastroDTO;
import br.com.fiap.accessplus.model.dto.AvaliacaoCategoriaDTO;
import br.com.fiap.accessplus.model.dto.AvaliacaoRespostaDTO;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.AvaliacaoCategoria;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AvaliacaoController {

    private final AvaliacaoBO avaliacaoBO;

    public AvaliacaoController(AvaliacaoBO avaliacaoBO) {
        this.avaliacaoBO = avaliacaoBO;
    }

    @PostMapping("/restaurantes/{idRestaurante}/avaliacoes")
    public ResponseEntity<AvaliacaoRespostaDTO> cadastrar(@PathVariable Long idRestaurante,
                                                           @Valid @RequestBody AvaliacaoCadastroDTO dadosCadastro) {
        if (dadosCadastro == null) {
            throw new IllegalArgumentException("Dados da avaliação não informados");
        }

        Avaliacao cadastrada = avaliacaoBO.cadastrar(idRestaurante, dadosCadastro.getIdUsuario(),
                paraEntidade(dadosCadastro));
        return ResponseEntity.status(201).body(AvaliacaoRespostaDTO.from(cadastrada));
    }

    @GetMapping("/restaurantes/{idRestaurante}/avaliacoes")
    public List<AvaliacaoRespostaDTO> listarPorRestaurante(@PathVariable Long idRestaurante) {
        return avaliacaoBO.listarPublicasDoRestaurante(idRestaurante).stream()
                .map(AvaliacaoRespostaDTO::from)
                .toList();
    }

    @GetMapping("/avaliacoes/{idAvaliacao}")
    public AvaliacaoRespostaDTO buscarPorId(@PathVariable Long idAvaliacao) {
        return AvaliacaoRespostaDTO.from(avaliacaoBO.buscarPublicaPorId(idAvaliacao));
    }

    private Avaliacao paraEntidade(AvaliacaoCadastroDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNotaGeral(dto.getNotaGeral());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setDataVisita(dto.getDataVisita());

        if (dto.getNotasCategorias() != null) {
            avaliacao.setNotasCategorias(dto.getNotasCategorias().stream()
                    .map(this::paraEntidade)
                    .toList());
        }
        return avaliacao;
    }

    private AvaliacaoCategoria paraEntidade(AvaliacaoCategoriaDTO dto) {
        AvaliacaoCategoria avaliacaoCategoria = new AvaliacaoCategoria();
        avaliacaoCategoria.setCategoria(dto.getCategoria());
        avaliacaoCategoria.setNota(dto.getNota());
        return avaliacaoCategoria;
    }
}
