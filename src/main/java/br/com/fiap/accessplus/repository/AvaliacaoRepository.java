package br.com.fiap.accessplus.repository;

import br.com.fiap.accessplus.model.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByRestauranteIdRestaurante(Long idRestaurante);
}
