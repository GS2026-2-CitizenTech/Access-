package br.com.fiap.accessplus.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Set;

/**
 * Proteção temporária para a demonstração pública, enquanto não há autenticação.
 */
public class ModoPublicoDemoInterceptor implements HandlerInterceptor {

    private static final Set<String> METODOS_DE_ESCRITA = Set.of(
            HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name());

    private final boolean somenteLeitura;

    public ModoPublicoDemoInterceptor(boolean somenteLeitura) {
        this.somenteLeitura = somenteLeitura;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!somenteLeitura) {
            return true;
        }

        String caminho = request.getRequestURI().substring(request.getContextPath().length());
        boolean consultaAdministrativaDeUsuario = caminho.equals("/usuarios") || caminho.startsWith("/usuarios/");
        boolean operacaoDeEscrita = METODOS_DE_ESCRITA.contains(request.getMethod());

        if (consultaAdministrativaDeUsuario || operacaoDeEscrita) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Modo público de demonstração: operações de escrita e usuários administrativos estão indisponíveis.");
            return false;
        }

        return true;
    }
}
