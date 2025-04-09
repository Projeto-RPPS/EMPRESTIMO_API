package io.rpps.emprestimo.controller.dto.emprestimo;

import io.rpps.emprestimo.model.StatusEmprestimo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmprestimoResumoDTO(
        Long idEmprestimo,
        StatusEmprestimo status,
        BigDecimal valorTotal,
        BigDecimal valorParcela,
        Integer quantidadeParcelas,
        LocalDate dataInicio,
        StatusFinanceiro statusFinanceiro
) {}

