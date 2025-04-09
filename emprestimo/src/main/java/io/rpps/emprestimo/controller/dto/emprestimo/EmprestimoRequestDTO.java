package io.rpps.emprestimo.controller.dto.emprestimo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record EmprestimoRequestDTO(@NotBlank(message = "campo obrigatório")
                                   @CPF(message = "CPF imválido")
                                   String  cpfContribuinte,
                                   @NotNull(message = "campo obrigatório")
                                   BigDecimal valorTotal,
                                   @NotNull(message = "campo obrigatório")
                                   @Min(value = 1, message = "A quantidade de parcelas deve ser maior que zero")
                                   Integer quantidadeParcelas) {
}