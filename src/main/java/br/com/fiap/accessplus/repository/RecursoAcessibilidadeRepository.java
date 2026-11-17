package br.com.fiap.accessplus.repository;

import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoAcessibilidadeRepository extends JpaRepository<RecursoAcessibilidade, Long> {
    List<RecursoAcessibilidade> findByCategoria(CategoriaAcessibilidade categoria);
}
