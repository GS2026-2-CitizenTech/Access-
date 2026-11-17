package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.RestauranteRecursoBO;
import br.com.fiap.accessplus.model.dto.RestauranteRecursoDTO;
import br.com.fiap.accessplus.model.dto.RestauranteRecursoRespostaDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteRecursoController {

    private final RestauranteRecursoBO restauranteRecursoBO;

    public RestauranteRecursoController(RestauranteRecursoBO restauranteRecursoBO) {
        this.restauranteRecursoBO = restauranteRecursoBO;
    }

    @GetMapping("/{idRestaurante}/recursos-acessibilidade")
    public List<RestauranteRecursoRespostaDTO> listarDeclaracoes(@PathVariable Long idRestaurante) {
        return restauranteRecursoBO.listarDeclaracoesPublicasDoRestaurante(idRestaurante).stream()
                .map(RestauranteRecursoRespostaDTO::from)
                .toList();
    }

    @PutMapping("/{idRestaurante}/recursos-acessibilidade/{idRecurso}")
    public RestauranteRecursoRespostaDTO declarar(@PathVariable Long idRestaurante,
                                                   @PathVariable Long idRecurso,
                                                   @Valid @RequestBody RestauranteRecursoDTO declaracaoDTO) {
        return RestauranteRecursoRespostaDTO.from(restauranteRecursoBO.declarar(
                idRestaurante, idRecurso, declaracaoDTO.getStatusDeclaracao()));
    }
}
