package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.RestauranteBO;
import br.com.fiap.accessplus.model.entity.Restaurante;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private RestauranteBO restauranteBO;

    public RestauranteController(RestauranteBO restauranteBO) {
        this.restauranteBO = restauranteBO;
    }
    @GetMapping
    public List<Restaurante> listarTodos() {
        return restauranteBO.listarTodos();
    }
    @GetMapping("/{id}")
    public Restaurante buscarPorId(@PathVariable Long id) {
        return restauranteBO.buscarPorId(id);
    }
    @PostMapping
    public ResponseEntity<Restaurante> cadastrar(@RequestBody Restaurante restaurante) {
        Restaurante restauranteCadastrado = restauranteBO.cadastrar(restaurante);
        return ResponseEntity.status(201).body(restauranteCadastrado);
    }
    @PatchMapping("/{id}")
    public Restaurante atualizar(@PathVariable Long id, @RequestBody Restaurante dadosAtualizados) {
        return restauranteBO.atualizar(id,dadosAtualizados);
    }
    @PatchMapping("/{id}/desativar")
    public Restaurante desativar(@PathVariable Long id) {
        return restauranteBO.desativar(id);
    }
    @PatchMapping("/{id}/publicar")
    public Restaurante publicar(@PathVariable Long id) {
        return restauranteBO.publicar(id);
    }
    @PatchMapping("/{id}/despublicar")
    public Restaurante despublicar(@PathVariable Long id) {
        return restauranteBO.despublicar(id);
    }
    @PatchMapping("/{id}/reativar")
    public Restaurante reativar(@PathVariable Long id) {
        return restauranteBO.reativar(id);
    }
}