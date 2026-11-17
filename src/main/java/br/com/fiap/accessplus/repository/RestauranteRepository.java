package br.com.fiap.accessplus.repository;
import br.com.fiap.accessplus.model.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByAtivoTrueAndPublicadoTrue();

    Optional<Restaurante> findByIdRestauranteAndAtivoTrueAndPublicadoTrue(Long idRestaurante);
}
