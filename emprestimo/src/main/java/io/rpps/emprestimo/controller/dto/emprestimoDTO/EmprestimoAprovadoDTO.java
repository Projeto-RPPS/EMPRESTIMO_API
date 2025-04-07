package io.rpps.emprestimo.controller.dto.emprestimoDTO;

import io.rpps.emprestimo.model.StatusEmprestimo;

import java.math.BigDecimal;

public record EmprestimoAprovadoDTO(Long idEmprestimo,
                                    StatusEmprestimo status,
                                    BigDecimal valorParcela,
                                    BigDecimal margemDisponivel
                                    ) {
}
