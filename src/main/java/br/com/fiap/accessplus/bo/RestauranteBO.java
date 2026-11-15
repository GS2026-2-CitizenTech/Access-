package br.com.fiap.accessplus.bo;

import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.repository.RestauranteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (restaurante.getEndereco() == null || restaurante.getEndereco().isBlank()){
            throw new IllegalArgumentException("Endereço não pode estar vazio");
        }
        if (restaurante.getDescricao() == null || restaurante.getDescricao().isBlank()){
            throw new IllegalArgumentException("Descrição não pode estar vazia");
        }
        restaurante.setAtivo(true);
        restaurante.setPublicado(false);
        return restauranteRepository.save(restaurante);
    }

    public Restaurante buscarPorId(Long id){
        if (id == null){
            throw new IllegalArgumentException("Id não informado");
        }
        Optional<Restaurante> resultado = restauranteRepository.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        }
        throw new IllegalArgumentException("Restaurante não encontrado");
    }

    public List<Restaurante> listarTodos(){
        return restauranteRepository.findAll();
    }

    public Restaurante atualizar(Long id, Restaurante dadosAtualizados) {
        if (id == null) {
            throw new IllegalArgumentException("Id não informado");
        }
        if (dadosAtualizados == null) {
            throw new IllegalArgumentException("Dados para atualização não informados");
        }

        if (dadosAtualizados.getNome() != null &&
                dadosAtualizados.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        if (dadosAtualizados.getBairro() != null &&
                dadosAtualizados.getBairro().isBlank()) {
            throw new IllegalArgumentException("Bairro não pode estar vazio");
        }
        if (dadosAtualizados.getEndereco() != null &&
                dadosAtualizados.getEndereco().isBlank()) {
            throw new IllegalArgumentException("Endereço não pode estar vazio");
        }
        if (dadosAtualizados.getDescricao() != null &&
                dadosAtualizados.getDescricao().isBlank()) {
            throw new IllegalArgumentException("Descrição não pode estar vazia");
        }

        Restaurante restauranteExistente = buscarPorId(id);

        if (dadosAtualizados.getNome() != null) {
            restauranteExistente.setNome(dadosAtualizados.getNome());
        }

        if (dadosAtualizados.getBairro() != null) {
            restauranteExistente.setBairro(dadosAtualizados.getBairro());
        }

        if (dadosAtualizados.getEndereco() != null) {
            restauranteExistente.setEndereco(dadosAtualizados.getEndereco());
        }

        if (dadosAtualizados.getDescricao() != null) {
            restauranteExistente.setDescricao(dadosAtualizados.getDescricao());
        }

        return restauranteRepository.save(restauranteExistente);
    }

    public Restaurante desativar(Long id){
        Restaurante restauranteExistente = buscarPorId(id);
        restauranteExistente.setAtivo(false);

        return restauranteRepository.save(restauranteExistente);
    }

    public Restaurante publicar(Long id){
        Restaurante restauranteExistente = buscarPorId(id);

        if (!restauranteExistente.isAtivo()) {
            throw new IllegalArgumentException("Não é possível publicar um restaurante desativado");
        }
        restauranteExistente.setPublicado(true);

        return restauranteRepository.save(restauranteExistente);
    }
    public Restaurante despublicar(Long id){
        Restaurante restauranteExistente = buscarPorId(id);
        restauranteExistente.setPublicado(false);

        return restauranteRepository.save(restauranteExistente);
    }

    public Restaurante reativar(Long id){
        Restaurante restauranteExistente = buscarPorId(id);
        restauranteExistente.setAtivo(true);

        return restauranteRepository.save(restauranteExistente);
    }
}
