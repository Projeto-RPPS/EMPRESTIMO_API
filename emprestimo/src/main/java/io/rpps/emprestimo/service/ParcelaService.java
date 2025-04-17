package io.rpps.emprestimo.service;

import io.rpps.emprestimo.controller.dto.parcelas.ParcelaDTO;
import io.rpps.emprestimo.model.Emprestimo;
import io.rpps.emprestimo.model.Parcela;
import io.rpps.emprestimo.repository.EmprestimoRepository;
import io.rpps.emprestimo.repository.ParcelasRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ParcelaService {

    private final ParcelasRepository repository;
    private final EmprestimoRepository emprestimoRepository;
    private static final BigDecimal TAXA_JUROS_DIARIA = BigDecimal.valueOf(0.01);

    public void gerarParcelas(Emprestimo emprestimo, BigDecimal valorParcela) {
        BigDecimal valorArredondado = valorParcela.setScale(2, RoundingMode.HALF_UP);
        for (int i = 1; i <= emprestimo.getQuantidadeParcelas(); i++) {
            Parcela parcela = new Parcela();
            parcela.setNumeroParcela(i);
            parcela.setDataVencimento(emprestimo.getDataInicio().plusMonths(i));
            parcela.setValor(valorArredondado);
            parcela.setPaga(false);
            parcela.setEmprestimo(emprestimo);
            repository.save(parcela);
        }
    }

    public List<Parcela> buscarPorEmprestimo(Long idEmprestimo){
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() ->
                        new IllegalArgumentException("Empréstimo não encontrado com este ID"));
        return repository.findByEmprestimoId(emprestimo.getId());
    }

    private record PagamentoInfo(List<Parcela> parcelas, Parcela proxima, BigDecimal total) {}

    private PagamentoInfo calcularDadosPagamentoAcumulado(Long idEmprestimo) {
        LocalDate hoje = LocalDate.now();
        List<Parcela> pendentes = repository.buscarParcelasPendentesOrdenadas(idEmprestimo);
        if (pendentes.isEmpty()){
            throw new IllegalArgumentException("Não há parcelas pendentes para este empréstimo.");
        }

        List<Parcela> vencidas = pendentes.stream()
                .filter(p -> p.getDataVencimento().isBefore(hoje))
                .toList();

        Parcela proxima = vencidas.isEmpty()
                ? pendentes.getFirst()
                : pendentes.get(vencidas.size());

        List<Parcela> aPagar = Stream.concat(vencidas.stream(), Stream.of(proxima))
                .collect(Collectors.toList());

        BigDecimal total = aPagar.stream()
                .map(p -> p.equals(proxima)
                        ? p.getValor().setScale(2, RoundingMode.HALF_UP)
                        : calcularValorComJuros(p, hoje))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return new PagamentoInfo(aPagar, proxima, total);
    }

    public ParcelaDTO proximaParcelaPendente(Long idEmprestimo) {
        PagamentoInfo info = calcularDadosPagamentoAcumulado(idEmprestimo);
        Parcela p = info.proxima;
        return new ParcelaDTO(
                p.getNumeroParcela(),
                p.getDataVencimento(),
                info.total,
                p.getPaga(),
                p.getDataPagamento()
        );
    }

    @Transactional
    public String pagarParcela(Long idEmprestimo) {
        emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado"));

        PagamentoInfo info = calcularDadosPagamentoAcumulado(idEmprestimo);
        LocalDate hoje = LocalDate.now();

        info.parcelas.forEach(p -> {
            p.setPaga(true);
            p.setDataPagamento(hoje);
        });
        repository.saveAll(info.parcelas);

        return String.format("Pagamento efetuado com sucesso. Valor total pago: R$ %.2f", info.total);
    }

    @Transactional
    public String anteciparParcelas(Long idEmprestimo, Integer quantidade) {
        emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado"));

        LocalDate hoje = LocalDate.now();
        List<Parcela> pendentes = repository.buscarParcelasPendentesOrdenadas(idEmprestimo);

        boolean temAtraso = pendentes.stream()
                .anyMatch(p -> p.getDataVencimento().isBefore(hoje));
        if (temAtraso) {
            throw new IllegalArgumentException(
                    "Não é possível antecipar: existem parcelas vencidas. Pague-as primeiro.");
        }

        if (quantidade <= 0 || pendentes.size() < quantidade) {
            throw new IllegalArgumentException("Quantidade inválida para antecipação.");
        }

        List<Parcela> selecionadas = pendentes.subList(0, quantidade);
        BigDecimal total = selecionadas.stream()
                .map(p -> p.getValor().setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        selecionadas.forEach(p -> {
            p.setPaga(true);
            p.setDataPagamento(hoje);
        });
        repository.saveAll(selecionadas);

        return String.format("Parcelas antecipadas com sucesso. Total pago: R$ %.2f", total);
    }

    private BigDecimal calcularValorComJuros(Parcela parcela, LocalDate hoje) {
        BigDecimal valor = parcela.getValor().setScale(2, RoundingMode.HALF_UP);
        long diasAtraso = ChronoUnit.DAYS.between(parcela.getDataVencimento(), hoje);
        if (diasAtraso > 0) {
            BigDecimal juros = valor
                    .multiply(TAXA_JUROS_DIARIA)
                    .multiply(BigDecimal.valueOf(diasAtraso))
                    .setScale(2, RoundingMode.HALF_UP);
            return valor.add(juros);
        }
        return valor;
    }
}