package br.com.fiap.accessplus.exception;

public class RestauranteNaoEncontradoException extends RuntimeException {

    public RestauranteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}