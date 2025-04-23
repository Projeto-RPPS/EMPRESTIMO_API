package io.rpps.emprestimo.service;

import io.rpps.emprestimo.controller.dto.margemConsignavel.MargemConsignavelDTO;
import io.rpps.emprestimo.integracao.BeneficioServiceGateway;
import io.rpps.emprestimo.model.Parcela;
import io.rpps.emprestimo.model.StatusEmprestimo;
import io.rpps.emprestimo.repository.ParcelasRepository;
import io.rpps.emprestimo.validator.BeneficioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MargemConsignavelService {

    private final BeneficioServiceGateway beneficioService;
    private final ParcelasRepository parcelasRepository;
    private final BeneficioValidator validator;

    public MargemConsignavelDTO calcularMargem(String cpfContribuinte) {
        validator.validarCpf(cpfContribuinte);

        var beneficio = beneficioService.getBeneficio(cpfContribuinte);
        validator.validarBeneficioAtivo(beneficio);
        BigDecimal margemTotal = beneficio.totalBeneficios()
                .multiply(BigDecimal.valueOf(0.3));

        List<Parcela> parcelasPendentes = parcelasRepository
                .findByEmprestimo_CpfContribuinteAndEmprestimo_StatusAndPagaFalse(
                        cpfContribuinte, StatusEmprestimo.APROVADO);

        BigDecimal valorEmUso = parcelasPendentes.stream()
                .collect(Collectors.toMap(
                        parcela -> parcela.getEmprestimo().getId(),
                        Parcela::getValor,
                        (valorExistente, novoValor) -> valorExistente
                ))
                .values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal margemDisponivel = margemTotal.subtract(valorEmUso);

        return new MargemConsignavelDTO(
                beneficio.totalBeneficios(),
                margemTotal,
                valorEmUso,
                margemDisponivel
        );
    }
}