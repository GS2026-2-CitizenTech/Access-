package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.RecursoAcessibilidadeBO;
import br.com.fiap.accessplus.bo.RestauranteRecursoBO;
import br.com.fiap.accessplus.model.dto.RecursoAcessibilidadeRespostaDTO;
import br.com.fiap.accessplus.model.dto.RestauranteRespostaDTO;
import br.com.fiap.accessplus.model.entity.CategoriaAcessibilidade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recursos-acessibilidade")
public class RecursoAcessibilidadeController {

    private final RecursoAcessibilidadeBO recursoBO;
    private final RestauranteRecursoBO restauranteRecursoBO;

    public RecursoAcessibilidadeController(RecursoAcessibilidadeBO recursoBO,
                                           RestauranteRecursoBO restauranteRecursoBO) {
        this.recursoBO = recursoBO;
        this.restauranteRecursoBO = restauranteRecursoBO;
    }

    @GetMapping
    public List<RecursoAcessibilidadeRespostaDTO> listar(
            @RequestParam(required = false) CategoriaAcessibilidade categoria) {
        if (categoria == null) {
            return recursoBO.listarTodos().stream()
                    .map(RecursoAcessibilidadeRespostaDTO::from)
                    .toList();
        }

        return recursoBO.listarPorCategoria(categoria).stream()
                .map(RecursoAcessibilidadeRespostaDTO::from)
                .toList();
    }

    @GetMapping("/{idRecurso}/restaurantes")
    public List<RestauranteRespostaDTO> listarRestaurantesComRecurso(@PathVariable Long idRecurso) {
        return restauranteRecursoBO.listarRestaurantesPublicosComRecurso(idRecurso).stream()
                .map(RestauranteRespostaDTO::from)
                .toList();
    }
}
