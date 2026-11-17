package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.AvaliacaoNaoEncontradaException;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.AvaliacaoCategoria;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.Usuario;
import br.com.fiap.accessplus.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AvaliacaoBO {

    private static final BigDecimal NOTA_MINIMA = BigDecimal.ZERO;
    private static final BigDecimal NOTA_MAXIMA = new BigDecimal("5.0");
    private static final BigDecimal INCREMENTO_NOTA = new BigDecimal("0.5");

    private final AvaliacaoRepository avaliacaoRepository;
    private final RestauranteBO restauranteBO;
    private final UsuarioBO usuarioBO;

    public AvaliacaoBO(AvaliacaoRepository avaliacaoRepository,
                       RestauranteBO restauranteBO,
                       UsuarioBO usuarioBO) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.restauranteBO = restauranteBO;
        this.usuarioBO = usuarioBO;
    }

    public Avaliacao cadastrar(Long idRestaurante, Long idUsuario, Avaliacao avaliacao) {
        if (avaliacao == null) {
            throw new IllegalArgumentException("Dados da avaliação não informados");
        }

        validarNota(avaliacao.getNotaGeral(), "Nota geral");
        validarNotasCategorias(avaliacao.getNotasCategorias());

        Restaurante restaurante = restauranteBO.buscarPublicoPorId(idRestaurante);
        Usuario usuario = usuarioBO.buscarPorId(idUsuario);

        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
        avaliacao.setDataCriacao(LocalDateTime.now());
        for (AvaliacaoCategoria notaCategoria : avaliacao.getNotasCategorias()) {
            notaCategoria.setAvaliacao(avaliacao);
        }

        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> listarPublicasDoRestaurante(Long idRestaurante) {
        restauranteBO.buscarPublicoPorId(idRestaurante);
        return avaliacaoRepository.findByRestauranteIdRestaurante(idRestaurante);
    }

    public Avaliacao buscarPublicaPorId(Long idAvaliacao) {
        if (idAvaliacao == null) {
            throw new IllegalArgumentException("Id da avaliação não informado");
        }

        Avaliacao avaliacao = avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new AvaliacaoNaoEncontradaException("Avaliação não encontrada"));

        if (!avaliacao.getRestaurante().isAtivo() || !avaliacao.getRestaurante().isPublicado()) {
            throw new AvaliacaoNaoEncontradaException("Avaliação não encontrada");
        }
        return avaliacao;
    }

    private void validarNotasCategorias(List<AvaliacaoCategoria> notasCategorias) {
        Set<CategoriaAcessibilidade> categorias = new HashSet<>();
        for (AvaliacaoCategoria notaCategoria : notasCategorias) {
            if (notaCategoria == null || notaCategoria.getCategoria() == null) {
                throw new IllegalArgumentException("Categoria de acessibilidade inválida");
            }
            if (!categorias.add(notaCategoria.getCategoria())) {
                throw new IllegalArgumentException("Não é permitido repetir categoria na mesma avaliação");
            }
            validarNota(notaCategoria.getNota(), "Nota da categoria");
        }
    }

    private void validarNota(BigDecimal nota, String campo) {
        if (nota == null) {
            throw new IllegalArgumentException(campo + " é obrigatória");
        }
        if (nota.compareTo(NOTA_MINIMA) < 0 || nota.compareTo(NOTA_MAXIMA) > 0) {
            throw new IllegalArgumentException(campo + " deve estar entre zero e cinco");
        }
        if (nota.remainder(INCREMENTO_NOTA).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException(campo + " deve utilizar incrementos de 0,5");
        }
    }
}
