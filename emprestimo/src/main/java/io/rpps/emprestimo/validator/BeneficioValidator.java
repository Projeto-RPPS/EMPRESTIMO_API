package io.rpps.emprestimo.validator;

import io.rpps.emprestimo.integracao.BeneficioDTO;
import org.springframework.stereotype.Component;

@Component
public class BeneficioValidator {

    public void validarBeneficioAtivo(BeneficioDTO beneficio) {
        if (!"ATIVO".equalsIgnoreCase(beneficio.status())) {
            throw new IllegalStateException("Benefício inativo");
        }
    }
}
