package io.rpps.emprestimo.validator;

import io.rpps.emprestimo.model.Parcela;
import io.rpps.emprestimo.model.StatusEmprestimo;
import org.springframework.stereotype.Component;

@Component
public class ParcelaValidator {

    public void validarPagamento(Parcela parcela){
        if (parcela.getPaga()){
            throw new IllegalStateException("Parcela já paga.");
        }

        if (parcela.getEmprestimo().getStatus() != StatusEmprestimo.APROVADO){
            throw new IllegalStateException("Emprestimo não ativo para pagamentos.");
        }
    }
}
