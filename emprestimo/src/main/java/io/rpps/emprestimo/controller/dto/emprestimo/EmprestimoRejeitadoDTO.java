package io.rpps.emprestimo.controller.dto.emprestimo;

import io.rpps.emprestimo.model.StatusEmprestimo;

public record EmprestimoRejeitadoDTO(
        Long idEmprestimo,
        StatusEmprestimo status,           // sempre REJEITADO
        String justificativaRejeicao
) {}

