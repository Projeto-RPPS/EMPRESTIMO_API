package io.rpps.emprestimo.controller.mappers;

import io.rpps.emprestimo.controller.dto.emprestimo.*;
import io.rpps.emprestimo.model.Emprestimo;
import io.rpps.emprestimo.model.Parcela;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring")
public interface EmprestimoMapper {

    Emprestimo toEntity(EmprestimoRequestDTO dto);

    @Mapping(target = "idEmprestimo", source = "emprestimo.id")
    @Mapping(target = "status", source = "emprestimo.status")
    @Mapping(target = "valorParcela", expression = "java(calcularParcela(emprestimo))")
    EmprestimoAprovadoDTO toAprovadoDTO(Emprestimo emprestimo, BigDecimal margemDisponivel);

    @Mapping(target = "idEmprestimo", source = "emprestimo.id")
    @Mapping(target = "status", source = "emprestimo.status")
    EmprestimoRejeitadoDTO toRejeitadoDTO(Emprestimo emprestimo, String justificativaRejeicao);

    @Mapping(target = "idEmprestimo", source = "id")
    @Mapping(target = "valorParcela", expression = "java(calcularParcela(emprestimo))")
    @Mapping(target = "statusFinanceiro", expression = "java(definirStatusFinanceiro(emprestimo))")
    EmprestimoResumoDTO toResumoDTO(Emprestimo emprestimo);

    default BigDecimal calcularParcela(Emprestimo emprestimo) {
        if (emprestimo.getQuantidadeParcelas() == null || emprestimo.getQuantidadeParcelas() == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal juros = emprestimo.getValorTotal().multiply(BigDecimal.valueOf(0.02));
        BigDecimal jurosTotal = juros.multiply(BigDecimal.valueOf(emprestimo.getQuantidadeParcelas()));
        BigDecimal valorTotalComJuros = emprestimo.getValorTotal().add(jurosTotal);
        BigDecimal valorParcela = valorTotalComJuros.divide(BigDecimal.valueOf(
                emprestimo.getQuantidadeParcelas()), 2, RoundingMode.HALF_UP);

        return valorParcela.setScale(2, RoundingMode.HALF_UP);
    }

    default StatusFinanceiro definirStatusFinanceiro(Emprestimo emprestimo) {
        if (emprestimo.getParcela() == null || emprestimo.getParcela().isEmpty()) {
            return StatusFinanceiro.PENDENTE;
        }
        boolean todasPagas = emprestimo.getParcela().stream().allMatch(Parcela::getPaga);
        return todasPagas ? StatusFinanceiro.QUITADO : StatusFinanceiro.PENDENTE;
    }
}