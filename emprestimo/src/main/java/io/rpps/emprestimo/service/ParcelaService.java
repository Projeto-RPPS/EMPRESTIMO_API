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
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
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

        if (pendentes.isEmpty()) {
            return "Todas as parcelas já foram pagas.";
        }

        // Pega a próxima parcela pendente
        Parcela proxima = pendentes.getFirst();

        // Verifica se a próxima parcela está vencida
        if (proxima.getDataVencimento().isAfter(LocalDate.now())) {
            return "A próxima parcela vence em " +
                    proxima.getDataVencimento() +
                    ". Nenhuma parcela em atraso para pagamento.";
        }

        // Marca a parcela como paga
        proxima.setPaga(true);
        repository.save(proxima);

        return "Parcela com vencimento em " + proxima.getDataVencimento() + " paga com sucesso.";
    }

    private String anteciparParcelas(Long idEmprestimo, Integer numeroParcelas) {
        List<Parcela> pendentes = repository.buscarParcelasPendentesOrdenadas(idEmprestimo);

        // Verifica se o número de parcelas a ser antecipado é válido
        if (pendentes.size() < numeroParcelas) {
            return "Não há parcelas suficientes para antecipar.";
        }

        BigDecimal totalPago = BigDecimal.ZERO;
        for (int i = 0; i < numeroParcelas; i++) {
            Parcela parcela = pendentes.get(i);

            // Verifica se a parcela já foi paga, e se sim, pula para a próxima
            if (parcela.getPaga()) {
                continue;
            }

            // Marca a parcela como paga
            parcela.setPaga(true);
            totalPago = totalPago.add(parcela.getValor());

            // Atualiza a data de vencimento para a data atual (indicando que foi antecipada)
            parcela.setDataVencimento(LocalDate.now());

            repository.save(parcela);
        }

        return "Parcelas antecipadas com sucesso. Total pago: " + totalPago;
    }
}
