package io.rpps.emprestimo.validator;

import io.rpps.emprestimo.integracao.BeneficioDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EmprestimoValidator {

    public void validarBeneficioAtivo(BeneficioDTO beneficio) {
        if (!"ATIVO".equalsIgnoreCase(beneficio.status())) {
            throw new IllegalStateException("Benefício inativo");
        }
    }

    public boolean temMargem(BigDecimal valorParcela, BigDecimal margemDisponivel) {
        return valorParcela.compareTo(margemDisponivel) <= 0;
    }
}
