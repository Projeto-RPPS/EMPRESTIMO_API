package io.rpps.emprestimo.integracao;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BeneficioServiceGateway {

    private final RestTemplate restTemplate;

    @Value("${beneficios.api.base-url}")
    private String baseUrl;

    public BeneficioDTO getBeneficio(String cpf) {
        URI url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/beneficios/solicitacao/cpf/{cpf}/total")
                .buildAndExpand(cpf)
                .toUri();

        return restTemplate.getForObject(url, BeneficioDTO.class);
    }
}