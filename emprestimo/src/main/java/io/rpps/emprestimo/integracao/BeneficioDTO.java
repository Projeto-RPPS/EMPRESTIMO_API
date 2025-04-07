package io.rpps.emprestimo.integracao;

import java.math.BigDecimal;

public record BeneficioDTO(String cpfContribuinte,
                           BigDecimal valor,
                           String status) {
}
