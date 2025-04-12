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

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
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

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleDataAccessException(DataAccessException e) {

        log.error("Erro de acesso ao Banco de dados", e);

        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
        "Erro interno na conexão com Banco de Dados", List.of(new ErroCampo("Banco de Dados", e.getMostSpecificCause().getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleRuntimeException(RuntimeException e) {
        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor",
                           List.of());
    }
}
