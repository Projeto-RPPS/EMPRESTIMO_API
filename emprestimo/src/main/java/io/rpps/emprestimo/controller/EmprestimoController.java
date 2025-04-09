package io.rpps.emprestimo.controller;

import io.rpps.emprestimo.controller.dto.emprestimo.*;
import io.rpps.emprestimo.service.EmprestimoService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService service;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo aprovado",
                    content = @Content(schema = @Schema(implementation = EmprestimoAprovadoDTO.class))),
            @ApiResponse(responseCode = "422", description = "Empréstimo rejeitado",
                    content = @Content(schema = @Schema(implementation = EmprestimoRejeitadoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<Object> criar(@RequestBody @Valid EmprestimoRequestDTO dto) {
        Object response = service.criarEmprestimo(dto);

        if(response instanceof EmprestimoAprovadoDTO aprovado){
            return ResponseEntity.ok(aprovado);
        }
        if(response instanceof EmprestimoRejeitadoDTO rejeitado){
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(rejeitado);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @GetMapping("/{cpfContribuinte}")
    public ResponseEntity<List<EmprestimoResumoDTO>> consultarEmprestimosPorCpf(
            @PathVariable String cpfContribuinte){
        List<EmprestimoResumoDTO> response = service.consultarEmprestimoPorCpf(cpfContribuinte);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/simulacao")
    public ResponseEntity<SimularEmprestimoDTO> simularEmprestimo(@RequestBody @Valid EmprestimoRequestDTO dto){
        var simulacao = service.simularEmprestimo(dto);
        return ResponseEntity.ok(simulacao);
    }
}
