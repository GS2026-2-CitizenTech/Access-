package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.StatusDeclaracao;
import br.com.fiap.accessplus.repository.RestauranteRecursoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestauranteRecursoBO {

    private final RestauranteRecursoRepository restauranteRecursoRepository;
    private final RestauranteBO restauranteBO;
    private final RecursoAcessibilidadeBO recursoBO;

    public RestauranteRecursoBO(RestauranteRecursoRepository restauranteRecursoRepository,
                                RestauranteBO restauranteBO,
                                RecursoAcessibilidadeBO recursoBO) {
        this.restauranteRecursoRepository = restauranteRecursoRepository;
        this.restauranteBO = restauranteBO;
        this.recursoBO = recursoBO;
    }

    public List<RestauranteRecurso> listarDeclaracoesPublicasDoRestaurante(Long idRestaurante) {
        restauranteBO.buscarPublicoPorId(idRestaurante);
        return restauranteRecursoRepository.findByRestauranteIdRestaurante(idRestaurante);
    }

    public RestauranteRecurso declarar(Long idRestaurante, Long idRecurso, StatusDeclaracao statusDeclaracao) {
        if (statusDeclaracao == null) {
            throw new IllegalArgumentException("Status da declaração é obrigatório");
        }

        Restaurante restaurante = restauranteBO.buscarPorId(idRestaurante);
        RecursoAcessibilidade recurso = recursoBO.buscarPorId(idRecurso);

        RestauranteRecurso declaracao = restauranteRecursoRepository
                .findByRestauranteIdRestauranteAndRecursoIdRecurso(idRestaurante, idRecurso)
                .orElseGet(RestauranteRecurso::new);

        declaracao.setRestaurante(restaurante);
        declaracao.setRecurso(recurso);
        declaracao.setStatusDeclaracao(statusDeclaracao);
        declaracao.setDataAtualizacao(LocalDateTime.now());
        declaracao.setAutodeclarado(true);

        return restauranteRecursoRepository.save(declaracao);
    }

    public List<Restaurante> listarRestaurantesPublicosComRecurso(Long idRecurso) {
        recursoBO.buscarPorId(idRecurso);

        return restauranteRecursoRepository
                .findByRecursoIdRecursoAndStatusDeclaracao(idRecurso, StatusDeclaracao.SIM)
                .stream()
                .filter(declaracao -> declaracao.getStatusDeclaracao() == StatusDeclaracao.SIM)
                .map(RestauranteRecurso::getRestaurante)
                .filter(Restaurante::isAtivo)
                .filter(Restaurante::isPublicado)
                .toList();
    }
}
