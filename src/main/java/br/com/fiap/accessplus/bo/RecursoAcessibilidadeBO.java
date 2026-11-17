package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.RecursoAcessibilidadeNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.repository.RecursoAcessibilidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoAcessibilidadeBO {

    private final RecursoAcessibilidadeRepository recursoRepository;

    public RecursoAcessibilidadeBO(RecursoAcessibilidadeRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    public List<RecursoAcessibilidade> listarTodos() {
        return recursoRepository.findAll();
    }

    public List<RecursoAcessibilidade> listarPorCategoria(CategoriaAcessibilidade categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria de acessibilidade inválida");
        }
        return recursoRepository.findByCategoria(categoria);
    }

    public RecursoAcessibilidade buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id do recurso não informado");
        }
        return recursoRepository.findById(id)
                .orElseThrow(() -> new RecursoAcessibilidadeNaoEncontradoException("Recurso de acessibilidade não encontrado"));
    }

    public RecursoAcessibilidade cadastrarNoCatalogo(RecursoAcessibilidade recurso) {
        validarRecurso(recurso);
        return recursoRepository.save(recurso);
    }

    private void validarRecurso(RecursoAcessibilidade recurso) {
        if (recurso == null) {
            throw new IllegalArgumentException("Dados do recurso não informados");
        }
        if (recurso.getNome() == null || recurso.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do recurso não pode estar vazio");
        }
        if (recurso.getCategoria() == null) {
            throw new IllegalArgumentException("Categoria de acessibilidade inválida");
        }
    }
}
