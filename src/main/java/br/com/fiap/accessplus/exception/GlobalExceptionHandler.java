package br.com.fiap.accessplus.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RestauranteNaoEncontradoException.class)
    public ResponseEntity<String> tratarRestauranteNaoEncontrado(
            RestauranteNaoEncontradoException ex) {

        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarArgumentoInvalido(
            IllegalArgumentException ex) {

        return ResponseEntity.status(400).body(ex.getMessage());
    }

}