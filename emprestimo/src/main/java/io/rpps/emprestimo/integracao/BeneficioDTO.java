package io.rpps.emprestimo.integracao;

import java.math.BigDecimal;

public record BeneficioDTO(String cpf,
                           String status,
                           BigDecimal totalBeneficios) {
}
