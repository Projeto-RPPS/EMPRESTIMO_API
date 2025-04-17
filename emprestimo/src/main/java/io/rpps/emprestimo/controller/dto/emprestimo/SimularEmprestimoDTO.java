package io.rpps.emprestimo.controller.dto.emprestimo;

import java.math.BigDecimal;

public record SimularEmprestimoDTO(Boolean poderiaSerAprovado,
                                   BigDecimal margenDisponivel,
                                   BigDecimal margemNecessaria) {
}