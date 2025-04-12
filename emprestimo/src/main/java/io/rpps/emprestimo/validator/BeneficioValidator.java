package io.rpps.emprestimo.validator;

import io.rpps.emprestimo.integracao.BeneficioDTO;
import org.springframework.stereotype.Component;

@Component
public class BeneficioValidator {

    public void validarBeneficioAtivo(BeneficioDTO beneficio) {
        if (!"concedido".equalsIgnoreCase(beneficio.status())) {
            throw new IllegalStateException("Benefício não foi concedido.");
        }
    }

    public void validarCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido. O CPF deve conter exatamente 11 dígitos numéricos.");
        }
    }
}
