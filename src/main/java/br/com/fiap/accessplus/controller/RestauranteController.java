package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.RestauranteBO;
import br.com.fiap.accessplus.model.dto.RestauranteAtualizacaoDTO;
import br.com.fiap.accessplus.model.dto.RestauranteDTO;
import br.com.fiap.accessplus.model.dto.RestauranteRespostaDTO;
import br.com.fiap.accessplus.model.entity.Restaurante;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteBO restauranteBO;

    public RestauranteController(RestauranteBO restauranteBO) {
        this.restauranteBO = restauranteBO;
    }
    @GetMapping
    public List<RestauranteRespostaDTO> listarTodos() {
        return restauranteBO.listarPublicos().stream()
                .map(RestauranteRespostaDTO::from)
                .toList();
    }
    @GetMapping("/{id}")
    public RestauranteRespostaDTO buscarPorId(@PathVariable Long id) {git status
        git branch
        return RestauranteRespostaDTO.from(restauranteBO.buscarPublicoPorId(id));
    }


    @PostMapping
    public ResponseEntity<RestauranteRespostaDTO> cadastrar(@RequestBody RestauranteDTO restauranteDTO) {
        Restaurante restauranteCadastrado = restauranteBO.cadastrar(paraEntidade(restauranteDTO));
        return ResponseEntity.status(201).body(RestauranteRespostaDTO.from(restauranteCadastrado));
    }

    @PutMapping("/{id}")
    public RestauranteRespostaDTO atualizarCompleto(@PathVariable Long id, @RequestBody RestauranteDTO dadosAtualizados) {
        return RestauranteRespostaDTO.from(restauranteBO.atualizarCompleto(id, paraEntidade(dadosAtualizados)));
    }

    @PatchMapping("/{id}")
    public RestauranteRespostaDTO atualizar(@PathVariable Long id, @RequestBody RestauranteAtualizacaoDTO dadosAtualizados) {
        return RestauranteRespostaDTO.from(restauranteBO.atualizar(id, paraEntidade(dadosAtualizados)));
    }


    @PatchMapping("/{id}/desativar")
    public RestauranteRespostaDTO desativar(@PathVariable Long id) {
        return RestauranteRespostaDTO.from(restauranteBO.desativar(id));
    }
    @PatchMapping("/{id}/publicar")
    public RestauranteRespostaDTO publicar(@PathVariable Long id) {
        return RestauranteRespostaDTO.from(restauranteBO.publicar(id));
    }
    @PatchMapping("/{id}/despublicar")
    public RestauranteRespostaDTO despublicar(@PathVariable Long id) {
        return RestauranteRespostaDTO.from(restauranteBO.despublicar(id));
    }
    @PatchMapping("/{id}/reativar")
    public RestauranteRespostaDTO reativar(@PathVariable Long id) {
        return RestauranteRespostaDTO.from(restauranteBO.reativar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        restauranteBO.desativar(id);
        return ResponseEntity.noContent().build();
    }

    private Restaurante paraEntidade(RestauranteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do restaurante não informados");
        }
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setBairro(dto.getBairro());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setDescricao(dto.getDescricao());
        return restaurante;
    }

    private Restaurante paraEntidade(RestauranteAtualizacaoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados para atualização não informados");
        }
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setBairro(dto.getBairro());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setDescricao(dto.getDescricao());
        return restaurante;
    }
}
