package io.rpps.emprestimo.controller.dto.parcelasDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaDTO(Integer numeroParcela,
                         LocalDate dataVencimento,
                         BigDecimal valor,
                         Boolean paga) {
}
