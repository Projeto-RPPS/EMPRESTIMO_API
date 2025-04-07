package io.rpps.emprestimo.service;

import io.rpps.emprestimo.controller.dto.emprestimoDTO.EmprestimoRequestDTO;
import io.rpps.emprestimo.controller.dto.emprestimoDTO.EmprestimoResumoDTO;
import io.rpps.emprestimo.controller.dto.emprestimoDTO.SimularEmprestimoDTO;
import io.rpps.emprestimo.controller.mappers.EmprestimoMapper;
import io.rpps.emprestimo.integracao.BeneficioDTO;
import io.rpps.emprestimo.integracao.BeneficioServiceGateway;
import io.rpps.emprestimo.model.Emprestimo;
import io.rpps.emprestimo.model.Parcela;
import io.rpps.emprestimo.model.StatusEmprestimo;
import io.rpps.emprestimo.repository.EmprestimoRepository;
import io.rpps.emprestimo.repository.ParcelasRepository;
import io.rpps.emprestimo.validator.EmprestimoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final BeneficioServiceGateway beneficioService;
    private final EmprestimoRepository repository;
    private final EmprestimoMapper mapper;
    private final EmprestimoValidator validator;
    private final ParcelaService parcelaService;
    private final MargemConsignavelService margemConsignavelService;

    public Object criarEmprestimo(EmprestimoRequestDTO dto) {
        var beneficio = beneficioService.getBeneficio(dto.cpfContribuinte());
        validator.validarBeneficioAtivo(beneficio);

        BigDecimal novaParcela = dto.valorTotal().divide(
                BigDecimal.valueOf(dto.quantidadeParcelas()), 2, RoundingMode.HALF_UP);

        var margem = margemConsignavelService.calcularMargem(dto.cpfContribuinte());

        Emprestimo emprestimo = mapper.toEntity(dto);
        emprestimo.setDataInicio(LocalDate.now());

        if (margem.valorEmUso().add(novaParcela).compareTo(margem.margemTotal()) <= 0) {
            emprestimo.setStatus(StatusEmprestimo.APROVADO);
            repository.save(emprestimo);
            parcelaService.gerarParcelas(emprestimo, novaParcela);
            return mapper.toAprovadoDTO(emprestimo, margem.margemDisponivel().subtract(novaParcela));
        } else {
            emprestimo.setStatus(StatusEmprestimo.REJEITADO);
            repository.save(emprestimo);
            return mapper.toRejeitadoDTO(emprestimo, "Margem consignável insuficiente");
        }
    }

    public List<EmprestimoResumoDTO> consultarEmprestimoPorCpf(String cpfContribuinte) {
        return repository.findByCpfContribuinte(cpfContribuinte)
                .stream()
                .map(mapper::toResumoDTO)
                .toList();
    }

    public SimularEmprestimoDTO simularEmprestimo(EmprestimoRequestDTO dto) {
        var beneficio = beneficioService.getBeneficio(dto.cpfContribuinte());
        validator.validarBeneficioAtivo(beneficio);

        BigDecimal novaParcela = dto.valorTotal().divide(
                BigDecimal.valueOf(dto.quantidadeParcelas()), 2, RoundingMode.HALF_UP);

        var margem = margemConsignavelService.calcularMargem(dto.cpfContribuinte());

        boolean aprovado = margem.valorEmUso().add(novaParcela).compareTo(margem.margemTotal()) <= 0;

        return new SimularEmprestimoDTO(aprovado, margem.margemDisponivel());
    }

}