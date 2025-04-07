package io.rpps.emprestimo.controller.dto.emprestimoDTO;

import io.rpps.emprestimo.model.StatusEmprestimo;

public record EmprestimoRejeitadoDTO(
        Long idEmprestimo,
        StatusEmprestimo status,           // sempre REJEITADO
        String justificativaRejeicao
) {}

