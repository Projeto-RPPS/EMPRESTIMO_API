package io.rpps.emprestimo.controller.mappers;

import io.rpps.emprestimo.controller.dto.parcelas.ParcelaDTO;
import io.rpps.emprestimo.model.Parcela;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParcelaMapper {

    ParcelaDTO toDTO(Parcela parcela);

}
