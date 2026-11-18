package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.AvaliacaoNaoEncontradaException;
import br.com.fiap.accessplus.exception.DeclaracaoRecursoNaoEncontradaException;
import br.com.fiap.accessplus.exception.VerificacaoRecursoDuplicadaException;
import br.com.fiap.accessplus.exception.VerificacaoRecursoNaoEncontradaException;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;
import br.com.fiap.accessplus.repository.AvaliacaoRepository;
import br.com.fiap.accessplus.repository.RestauranteRecursoRepository;
import br.com.fiap.accessplus.repository.VerificacaoRecursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificacaoRecursoBO {

    private final VerificacaoRecursoRepository verificacaoRecursoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final RestauranteRecursoRepository restauranteRecursoRepository;
    private final AvaliacaoBO avaliacaoBO;

    public VerificacaoRecursoBO(VerificacaoRecursoRepository verificacaoRecursoRepository,
                                AvaliacaoRepository avaliacaoRepository,
                                RestauranteRecursoRepository restauranteRecursoRepository,
                                AvaliacaoBO avaliacaoBO) {
        this.verificacaoRecursoRepository = verificacaoRecursoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.restauranteRecursoRepository = restauranteRecursoRepository;
        this.avaliacaoBO = avaliacaoBO;
    }

    @Transactional
    public VerificacaoRecurso cadastrar(Long idAvaliacao, VerificacaoRecurso verificacao) {
        if (idAvaliacao == null) {
            throw new IllegalArgumentException("Id da avaliação não informado");
        }
        if (verificacao == null) {
            throw new IllegalArgumentException("Dados da verificação não informados");
        }
        if (verificacao.getResultado() == null) {
            throw new IllegalArgumentException("Resultado da verificação é obrigatório");
        }
        if (verificacao.getComentario() != null && verificacao.getComentario().length() > 500) {
            throw new IllegalArgumentException("Comentário deve ter no máximo 500 caracteres");
        }
        if (verificacao.getRestauranteRecurso() == null
                || verificacao.getRestauranteRecurso().getIdRestauranteRecurso() == null) {
            throw new IllegalArgumentException("Declaração de recurso é obrigatória");
        }

        Long idRestauranteRecurso = verificacao.getRestauranteRecurso().getIdRestauranteRecurso();
        Avaliacao avaliacao = avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new AvaliacaoNaoEncontradaException("Avaliação não encontrada"));
        RestauranteRecurso restauranteRecurso = restauranteRecursoRepository.findById(idRestauranteRecurso)
                .orElseThrow(() -> new DeclaracaoRecursoNaoEncontradaException("Declaração de recurso não encontrada"));

        if (!avaliacao.getRestaurante().getIdRestaurante()
                .equals(restauranteRecurso.getRestaurante().getIdRestaurante())) {
            throw new IllegalArgumentException("Avaliação e declaração de recurso devem pertencer ao mesmo restaurante");
        }

        if (verificacaoRecursoRepository
                .existsByAvaliacaoIdAvaliacaoAndRestauranteRecursoIdRestauranteRecurso(idAvaliacao, idRestauranteRecurso)) {
            throw new VerificacaoRecursoDuplicadaException(
                    "Já existe uma verificação para esta avaliação e declaração de recurso");
        }

        verificacao.setAvaliacao(avaliacao);
        verificacao.setRestauranteRecurso(restauranteRecurso);
        verificacao.setDataVerificacao(LocalDateTime.now());
        return verificacaoRecursoRepository.save(verificacao);
    }

    @Transactional(readOnly = true)
    public List<VerificacaoRecurso> listarPublicasDaAvaliacao(Long idAvaliacao) {
        avaliacaoBO.buscarPublicaPorId(idAvaliacao);
        return verificacaoRecursoRepository.findByAvaliacaoIdAvaliacao(idAvaliacao);
    }

    @Transactional(readOnly = true)
    public VerificacaoRecurso buscarPublicaPorId(Long idVerificacao) {
        if (idVerificacao == null) {
            throw new IllegalArgumentException("Id da verificação não informado");
        }

        VerificacaoRecurso verificacao = verificacaoRecursoRepository.findById(idVerificacao)
                .orElseThrow(() -> new VerificacaoRecursoNaoEncontradaException("Verificação não encontrada"));
        avaliacaoBO.buscarPublicaPorId(verificacao.getAvaliacao().getIdAvaliacao());
        return verificacao;
    }
}
