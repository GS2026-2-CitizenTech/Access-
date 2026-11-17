package br.com.fiap.accessplus.repository;

import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.StatusDeclaracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestauranteRecursoRepository extends JpaRepository<RestauranteRecurso, Long> {
    List<RestauranteRecurso> findByRestauranteIdRestaurante(Long idRestaurante);

    Optional<RestauranteRecurso> findByRestauranteIdRestauranteAndRecursoIdRecurso(Long idRestaurante, Long idRecurso);

    List<RestauranteRecurso> findByRecursoIdRecursoAndStatusDeclaracao(Long idRecurso, StatusDeclaracao statusDeclaracao);
}
