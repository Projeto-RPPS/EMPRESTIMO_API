package io.rpps.emprestimo.controller.dto.emprestimoDTO;

import io.rpps.emprestimo.model.StatusEmprestimo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmprestimoResumoDTO(
        Long idEmprestimo,
        StatusEmprestimo status,
        BigDecimal valorTotal,
        BigDecimal valorParcela,
        Integer quantidadeParcelas,
        LocalDate dataInicio
) {}

