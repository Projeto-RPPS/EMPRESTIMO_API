package io.rpps.emprestimo.controller.dto.margemConsignavel;

import java.math.BigDecimal;

public record MargemConsignavelDTO(String cpfContribuinte,
                                   BigDecimal valorBeneficio,
                                   BigDecimal margemTotal,
                                   BigDecimal valorEmUso,
                                   BigDecimal margemDisponivel) {
}
