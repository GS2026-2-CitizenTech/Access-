package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.RecursoAcessibilidadeNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.repository.RecursoAcessibilidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecursoAcessibilidadeBOTest {

    private RecursoAcessibilidadeRepository repository;
    private RecursoAcessibilidadeBO recursoBO;

    @BeforeEach
    void setup() {
        repository = mock(RecursoAcessibilidadeRepository.class);
        recursoBO = new RecursoAcessibilidadeBO(repository);
        when(repository.save(any(RecursoAcessibilidade.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void listaRecursosEFiltraPorCategoria() {
        RecursoAcessibilidade rampa = recurso(1L, "Rampa de acesso", CategoriaAcessibilidade.FISICA);
        when(repository.findAll()).thenReturn(List.of(rampa));
        when(repository.findByCategoria(CategoriaAcessibilidade.FISICA)).thenReturn(List.of(rampa));

        assertEquals(List.of(rampa), recursoBO.listarTodos());
        assertEquals(List.of(rampa), recursoBO.listarPorCategoria(CategoriaAcessibilidade.FISICA));
    }

    @Test
    void validaRecursoDoCatalogoETrataIdInexistente() {
        RecursoAcessibilidade semNome = recurso(null, " ", CategoriaAcessibilidade.FISICA);
        assertThrows(IllegalArgumentException.class, () -> recursoBO.cadastrarNoCatalogo(semNome));

        RecursoAcessibilidade semCategoria = recurso(null, "Rampa", null);
        assertThrows(IllegalArgumentException.class, () -> recursoBO.cadastrarNoCatalogo(semCategoria));

        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoAcessibilidadeNaoEncontradoException.class, () -> recursoBO.buscarPorId(99L));
    }

    private RecursoAcessibilidade recurso(Long id, String nome, CategoriaAcessibilidade categoria) {
        RecursoAcessibilidade recurso = new RecursoAcessibilidade();
        recurso.setIdRecurso(id);
        recurso.setNome(nome);
        recurso.setCategoria(categoria);
        return recurso;
    }
}
