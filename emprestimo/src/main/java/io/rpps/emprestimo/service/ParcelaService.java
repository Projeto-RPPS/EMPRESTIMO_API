package io.rpps.emprestimo.service;

import io.rpps.emprestimo.model.Emprestimo;
import io.rpps.emprestimo.model.Parcela;
import io.rpps.emprestimo.repository.ParcelasRepository;
import io.rpps.emprestimo.validator.ParcelaValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelaService {

    private final ParcelasRepository repository;
    private final ParcelaValidator validator;

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
        return repository.findByEmprestimoId(idEmprestimo);

    }

    @Transactional
    public String pagarParcela(Long idEmprestimo) {
        List<Parcela> pendentes = repository.buscarParcelasPendentesOrdenadas(idEmprestimo);

        if (pendentes.isEmpty()) {
            return "Todas as parcelas deste empréstimo já foram pagas.";
        }

        Parcela proxima = pendentes.getFirst();

        if (proxima.getDataVencimento().isAfter(LocalDate.now())) {
            return "A próxima parcela vence em " + proxima.getDataVencimento() + ". Nenhuma parcela em atraso para pagamento.";
        }

        validator.validarPagamento(proxima);
        proxima.setPaga(true);
        repository.save(proxima);

        return "Parcela com vencimento em " + proxima.getDataVencimento() + " paga com sucesso.";
    }

}
