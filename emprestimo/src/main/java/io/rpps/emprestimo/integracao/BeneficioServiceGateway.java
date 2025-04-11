package io.rpps.emprestimo.integracao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BeneficioServiceGateway {

    private final RestTemplate restTemplate;

    // trocar para localhost quando subir apiBeneficio no docker
    private static final String BASE_URL = "http://host.docker.internal:8080";

    public BeneficioDTO getBeneficio(String cpf) {
        URI url = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path("/beneficios/solicitacao/cpf/{cpf}/total")
                .buildAndExpand(cpf)
                .toUri();

        return restTemplate.getForObject(url, BeneficioDTO.class);
    }
}
