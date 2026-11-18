package br.com.fiap.accessplus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ModoPublicoDemoWebConfig implements WebMvcConfigurer {

    private final boolean somenteLeitura;

    public ModoPublicoDemoWebConfig(
            @Value("${accessplus.demo-publico.somente-leitura:false}") boolean somenteLeitura) {
        this.somenteLeitura = somenteLeitura;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ModoPublicoDemoInterceptor(somenteLeitura));
    }
}
