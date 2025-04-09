package io.rpps.emprestimo.integracao;

import java.math.BigDecimal;

public record BeneficioDTO(BigDecimal valor,
                           String status) {
}
