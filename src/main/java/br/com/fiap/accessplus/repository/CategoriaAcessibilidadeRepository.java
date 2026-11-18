package br.com.fiap.accessplus.repository;

import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaAcessibilidadeRepository extends JpaRepository<CategoriaAcessibilidadeEntity, Long> {
    Optional<CategoriaAcessibilidadeEntity> findByCodigo(CategoriaAcessibilidade codigo);
}
