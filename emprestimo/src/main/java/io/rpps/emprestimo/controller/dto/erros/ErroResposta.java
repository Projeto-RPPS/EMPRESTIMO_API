package io.rpps.emprestimo.controller.dto.erros;

import java.util.List;

public record ErroResposta(int status, String mensagem, List<ErroCampo> erros) {
}
