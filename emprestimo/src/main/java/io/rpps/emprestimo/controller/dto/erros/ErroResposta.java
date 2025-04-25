package io.rpps.emprestimo.controller.dto.erros;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErroResposta(int status, String mensagem, List<ErroCampo> erros) {

    public static ErroResposta CampoInvalido(String mensagem){
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }

    public static ErroResposta OperacaoNaoEncontrada(String mensagem){
        return new ErroResposta(HttpStatus.NOT_FOUND.value(), mensagem, List.of());
    }

    public static ErroResposta OperacaoNaoPermitidaBusiness(String mensagem){
        return new ErroResposta(HttpStatus.OK.value(), mensagem, List.of());
    }
}