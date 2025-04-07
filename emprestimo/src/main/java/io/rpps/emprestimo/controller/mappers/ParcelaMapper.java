package io.rpps.emprestimo.controller.mappers;

import io.rpps.emprestimo.controller.dto.parcelasDTO.ParcelaDTO;
import io.rpps.emprestimo.model.Parcela;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParcelaMapper {

    ParcelaDTO toDTO(Parcela parcela);

}
