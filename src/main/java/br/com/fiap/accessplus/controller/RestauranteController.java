package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.RestauranteBO;
import br.com.fiap.accessplus.model.dto.RestauranteAtualizacaoDTO;
import br.com.fiap.accessplus.model.dto.RestauranteDTO;
import br.com.fiap.accessplus.model.dto.RestauranteRespostaDTO;
import br.com.fiap.accessplus.model.entity.Restaurante;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private RestauranteBO restauranteBO;

    public RestauranteController(RestauranteBO restauranteBO) {
        this.restauranteBO = restauranteBO;
    }
    @GetMapping
    public List<RestauranteRespostaDTO> listarTodos() {
        List<Restaurante> restaurantes = restauranteBO.listarTodos();
        List<RestauranteRespostaDTO> respostas = new ArrayList<>();

        for (Restaurante restaurante : restaurantes) {
            respostas.add(converterParaResposta(restaurante));
        }

        return respostas;
    }
    @GetMapping("/{id}")
    public RestauranteRespostaDTO buscarPorId(@PathVariable Long id) {
        Restaurante restaurante = restauranteBO.buscarPorId(id);
        return converterParaResposta(restaurante);
    }


    @PostMapping
    public ResponseEntity<RestauranteRespostaDTO> cadastrar(@RequestBody RestauranteDTO restauranteDTO) {
        Restaurante restaurante = new Restaurante();

        restaurante.setNome(restauranteDTO.getNome());
        restaurante.setBairro(restauranteDTO.getBairro());
        restaurante.setEndereco(restauranteDTO.getEndereco());
        restaurante.setDescricao(restauranteDTO.getDescricao());

        Restaurante restauranteCadastrado = restauranteBO.cadastrar(restaurante);
        return ResponseEntity.status(201).body(restauranteCadastrado);
    }
    @PatchMapping("/{id}")
    public Restaurante atualizar(@PathVariable Long id, @RequestBody RestauranteAtualizacaoDTO dadosAtualizados) {
        Restaurante restaurante = new Restaurante();

        restaurante.setNome(dadosAtualizados.getNome());
        restaurante.setBairro(dadosAtualizados.getBairro());
        restaurante.setEndereco(dadosAtualizados.getEndereco());
        restaurante.setDescricao(dadosAtualizados.getDescricao());

        return restauranteBO.atualizar(id,restaurante);
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

    private RestauranteRespostaDTO converterParaResposta(Restaurante restaurante) {

        RestauranteRespostaDTO resposta = new RestauranteRespostaDTO();
        resposta.setIdRestaurante(restaurante.getIdRestaurante());
        resposta.setNome(restaurante.getNome());
        resposta.setBairro(restaurante.getBairro());
        resposta.setEndereco(restaurante.getEndereco());
        resposta.setDescricao(restaurante.getDescricao());
        resposta.setAtivo(restaurante.isAtivo());
        resposta.setPublicado(restaurante.isPublicado());

        return resposta;

    }
}