package io.rpps.emprestimo.controller.dto.emprestimo;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record EmprestimoRequestDTO(@NotBlank(message = "campo obrigatório")
                                   @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas números")
                                   @Size(min = 11, message = "O CPF deve ter exatamente 11 digitos.")
                                   String  cpfContribuinte,
                                   @NotNull(message = "campo obrigatório")
                                   @Min(value = 1, message = "O valor do empréstimo deve ser maior que zero")
                                   BigDecimal valorTotal,
                                   @NotNull(message = "campo obrigatório")
                                   @Min(value = 1, message = "A quantidade de parcelas deve ser maior que zero")
                                   Integer quantidadeParcelas) {
}
