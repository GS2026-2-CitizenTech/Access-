package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.repository.RestauranteRepository;
import org.springframework.stereotype.Service;

@Service
public class RestauranteBO {
    private RestauranteRepository restauranteRepository;

    public RestauranteBO(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante cadastrar(Restaurante restaurante){
        if (restaurante == null) {
            throw new IllegalArgumentException("Dados do restaurante não informados");
        }
        if (restaurante.getNome() == null || restaurante.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        if (restaurante.getBairro() == null || restaurante.getBairro().isBlank()) {
            throw new IllegalArgumentException("Bairro não pode estar vazio");
        }
        restaurante.setAtivo(true);
        restaurante.setPublicado(false);
        return restauranteRepository.save(restaurante);
    }
}
