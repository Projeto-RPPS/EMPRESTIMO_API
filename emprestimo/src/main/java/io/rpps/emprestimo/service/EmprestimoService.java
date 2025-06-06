package io.rpps.emprestimo.service;

import io.rpps.emprestimo.controller.dto.emprestimo.EmprestimoRequestDTO;
import io.rpps.emprestimo.controller.dto.emprestimo.SimularEmprestimoDTO;
import io.rpps.emprestimo.controller.mappers.EmprestimoMapper;
import io.rpps.emprestimo.exceptions.ResourceNotFoundException;
import io.rpps.emprestimo.model.Emprestimo;
import io.rpps.emprestimo.model.StatusEmprestimo;
import io.rpps.emprestimo.repository.EmprestimoRepository;
import io.rpps.emprestimo.validator.BeneficioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository repository;
    private final EmprestimoMapper mapper;
    private final ParcelaService parcelaService;
    private final MargemConsignavelService margemConsignavelService;
    private final BeneficioValidator validator;
    private final BigDecimal VALOR_JUROS = BigDecimal.valueOf(0.02);


    private BigDecimal calcularNovaParcela(BigDecimal valorTotal, Integer quantidadeParcelas){
        return valorTotal.divide(
                BigDecimal.valueOf(quantidadeParcelas), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal aplicarJuros(BigDecimal valorPrincipal, int quantidadeParcelas) {
        BigDecimal jurosTotais = valorPrincipal
                .multiply(VALOR_JUROS)
                .multiply(BigDecimal.valueOf(quantidadeParcelas));

        return valorPrincipal.add(jurosTotais);
    }

    public Object criarEmprestimo(EmprestimoRequestDTO dto) {
        var margem = margemConsignavelService.calcularMargem(dto.cpfContribuinte());

        BigDecimal valorComJuros = aplicarJuros(dto.valorTotal(), dto.quantidadeParcelas());
        BigDecimal novaParcela = calcularNovaParcela(valorComJuros, dto.quantidadeParcelas());

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

    public List<Emprestimo> consultarEmprestimoPorCpf(String cpfContribuinte) {
        validator.validarCpf(cpfContribuinte);

        List<Emprestimo> emprestimos = repository.consultarPorCpf(cpfContribuinte);

        if (emprestimos.isEmpty()) {
            throw new ResourceNotFoundException("Empréstimos não encontrados para este CPF");
        }

        return emprestimos;
    }

    public SimularEmprestimoDTO simularEmprestimo(EmprestimoRequestDTO dto) {
        var margem = margemConsignavelService.calcularMargem(dto.cpfContribuinte());

        BigDecimal valorComJuros = aplicarJuros(dto.valorTotal(), dto.quantidadeParcelas());
        BigDecimal novaParcela = calcularNovaParcela(valorComJuros, dto.quantidadeParcelas());

        boolean aprovado = margem.valorEmUso().add(novaParcela).compareTo(margem.margemTotal()) <= 0;

        return new SimularEmprestimoDTO(aprovado, margem.margemDisponivel(), novaParcela);
    }
}