package br.com.fiap.accessplus.repository;

import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerificacaoRecursoRepository extends JpaRepository<VerificacaoRecurso, Long> {

    @EntityGraph(attributePaths = {"avaliacao", "restauranteRecurso", "restauranteRecurso.recurso"})
    List<VerificacaoRecurso> findByAvaliacaoIdAvaliacao(Long idAvaliacao);

    @Override
    @EntityGraph(attributePaths = {"avaliacao", "restauranteRecurso", "restauranteRecurso.recurso"})
    Optional<VerificacaoRecurso> findById(Long idVerificacao);

    boolean existsByAvaliacaoIdAvaliacaoAndRestauranteRecursoIdRestauranteRecurso(
            Long idAvaliacao, Long idRestauranteRecurso);
}
