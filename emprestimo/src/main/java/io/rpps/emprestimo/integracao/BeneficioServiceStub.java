package io.rpps.emprestimo.integracao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BeneficioServiceStub implements BeneficioServiceGateway{

    @Override
    public BeneficioDTO getBeneficio(String cpfContribuinte) {
        return new BeneficioDTO(cpfContribuinte, BigDecimal.valueOf(2000), "ATIVO");
    }
}
