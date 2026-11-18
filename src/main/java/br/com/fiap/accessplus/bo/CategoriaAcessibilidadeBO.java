package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.exception.RecursoAcessibilidadeNaoEncontradoException;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidadeEntity;
import br.com.fiap.accessplus.repository.CategoriaAcessibilidadeRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriaAcessibilidadeBO {
    private final CategoriaAcessibilidadeRepository categoriaRepository;
    public CategoriaAcessibilidadeBO(CategoriaAcessibilidadeRepository categoriaRepository) { this.categoriaRepository = categoriaRepository; }
    public CategoriaAcessibilidadeEntity buscarPorCodigo(CategoriaAcessibilidade codigo) {
        if (codigo == null) throw new IllegalArgumentException("Categoria de acessibilidade inválida");
        return categoriaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoAcessibilidadeNaoEncontradoException("Categoria de acessibilidade não encontrada"));
    }
}
