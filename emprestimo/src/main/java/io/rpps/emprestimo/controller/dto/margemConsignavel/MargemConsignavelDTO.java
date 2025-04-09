package io.rpps.emprestimo.controller.dto.margemConsignavel;

import java.math.BigDecimal;

public record MargemConsignavelDTO(BigDecimal valorBeneficio,
                                   BigDecimal margemTotal,
                                   BigDecimal valorEmUso,
                                   BigDecimal margemDisponivel) {
}
