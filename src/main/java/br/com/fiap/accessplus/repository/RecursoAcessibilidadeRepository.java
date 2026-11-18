package br.com.fiap.accessplus.repository;

import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidadeEntity;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoAcessibilidadeRepository extends JpaRepository<RecursoAcessibilidade, Long> {
    List<RecursoAcessibilidade> findByCategoria(CategoriaAcessibilidadeEntity categoria);
    List<RecursoAcessibilidade> findByCategoriaCodigo(CategoriaAcessibilidade codigo);
    default List<RecursoAcessibilidade> findByCategoria(CategoriaAcessibilidade codigo) {
        return findByCategoriaCodigo(codigo);
    }
}
