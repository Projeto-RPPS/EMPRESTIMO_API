package io.rpps.emprestimo.controller.common;

import io.rpps.emprestimo.controller.dto.erros.ErroCampo;
import io.rpps.emprestimo.controller.dto.erros.ErroResposta;
import io.rpps.emprestimo.controller.dto.parcelas.PagamentoResponseDTO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Erro de validação: {} ", e.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();

        List<ErroCampo> listErros = fieldErrors.stream()
                .map(fieldError -> new ErroCampo(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", listErros);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleIllegalArgumentException(IllegalArgumentException e) {
        return ErroResposta.CampoInvalido(e.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleRestClientException(RestClientException e) {
        log.error("Erro ao fazer a solicitação REST", e);
        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro ao fazer a solicitação REST",
                List.of());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleDataAccessException(DataAccessException e) {
        log.error("Erro de acesso ao Banco de dados", e);

        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
        "Erro interno na conexão com Banco de Dados",
                List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleRuntimeException(RuntimeException e) {
        log.error("Erro inesperado no servidor", e);
        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor",
                           List.of());
    }
}