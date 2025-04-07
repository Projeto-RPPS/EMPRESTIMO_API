package io.rpps.emprestimo.controller.mappers;

import io.rpps.emprestimo.controller.dto.emprestimoDTO.EmprestimoAprovadoDTO;
import io.rpps.emprestimo.controller.dto.emprestimoDTO.EmprestimoRejeitadoDTO;
import io.rpps.emprestimo.controller.dto.emprestimoDTO.EmprestimoRequestDTO;
import io.rpps.emprestimo.controller.dto.emprestimoDTO.EmprestimoResumoDTO;
import io.rpps.emprestimo.model.Emprestimo;
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
    EmprestimoResumoDTO toResumoDTO(Emprestimo emprestimo);

    default BigDecimal calcularParcela(Emprestimo emprestimo) {
        if (emprestimo.getQuantidadeParcelas() == null || emprestimo.getQuantidadeParcelas() == 0) {
            return BigDecimal.ZERO;
        }
        return emprestimo.getValorTotal()
                .divide(BigDecimal.valueOf(emprestimo.getQuantidadeParcelas()), 2, RoundingMode.HALF_UP);
    }
}