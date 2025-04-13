package io.rpps.emprestimo.service;

import io.rpps.emprestimo.model.Emprestimo;
import io.rpps.emprestimo.model.Parcela;
import io.rpps.emprestimo.repository.EmprestimoRepository;
import io.rpps.emprestimo.repository.ParcelasRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelaService {

    private final ParcelasRepository repository;
    private final EmprestimoRepository emprestimoRepository;

    public void gerarParcelas(Emprestimo emprestimo, BigDecimal valorParcela){
        for(int i = 1; i <= emprestimo.getQuantidadeParcelas(); i++){
            Parcela parcela = new Parcela();
            parcela.setNumeroParcela(i);
            parcela.setDataVencimento(emprestimo.getDataInicio().plusMonths(i));
            parcela.setValor(valorParcela);
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

    @Transactional
    public String pagarOuAnteciparParcelas(Long idEmprestimo, Integer numeroParcelas) {
        // Se não foi fornecido o número de parcelas, pagamos a próxima
        emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() ->
                        new IllegalArgumentException("Empréstimo não encontrado com este ID"));

        if (numeroParcelas != null && numeroParcelas <= 0) {
            throw new IllegalArgumentException("Número de parcelas deve ser maior que zero.");
        }

        if (numeroParcelas == null) {
            return pagarParcela(idEmprestimo);
        }

        // Caso contrário, fazemos a antecipação das parcelas
        return anteciparParcelas(idEmprestimo, numeroParcelas);
    }

    private String pagarParcela(Long idEmprestimo) {
        // Buscar as parcelas pendentes ordenadas pela data de vencimento
        List<Parcela> pendentes = repository.buscarParcelasPendentesOrdenadas(idEmprestimo);

        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        int anoAtual = hoje.getYear();

        Parcela parcelaDoMes = pendentes.stream()
            .filter(p -> p.getDataVencimento().getMonthValue() == mesAtual &&
                         p.getDataVencimento().getYear() == anoAtual)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Nenhuma parcela disponível para pagamento neste mês."));

        parcelaDoMes.setPaga(true);
        repository.save(parcelaDoMes);

        return "Parcela com vencimento em " + parcelaDoMes.getDataVencimento() + " paga com sucesso.";
    }

    private String anteciparParcelas(Long idEmprestimo, Integer numeroParcelas) {
        List<Parcela> pendentes = repository.buscarParcelasPendentesOrdenadas(idEmprestimo);

        // Verifica se o número de parcelas a ser antecipado é válido
        if (pendentes.size() < numeroParcelas) {
            throw new IllegalArgumentException("Não há parcelas suficientes para antecipar.");
        }

        BigDecimal totalPago = BigDecimal.ZERO;
        for (int i = 0; i < numeroParcelas; i++) {
            Parcela parcela = pendentes.get(i);

            if (parcela.getPaga()) {
                continue;
            }

            parcela.setPaga(true);
            totalPago = totalPago.add(parcela.getValor());

            repository.save(parcela);
        }

        return "Parcelas antecipadas com sucesso. Total pago: " + totalPago;
    }
}