package io.rpps.emprestimo.controller.dto.emprestimoDTO;

import java.math.BigDecimal;

public record SimularEmprestimoDTO(Boolean poderiaSerAprovado,
                                   BigDecimal margenDisponivel) {
}
