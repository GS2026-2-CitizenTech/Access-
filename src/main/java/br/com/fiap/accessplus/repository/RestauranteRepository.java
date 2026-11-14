package br.com.fiap.accessplus.repository;
import br.com.fiap.accessplus.model.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}
