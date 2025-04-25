package io.rpps.emprestimo.controller;

import io.rpps.emprestimo.controller.dto.emprestimo.*;
import io.rpps.emprestimo.controller.dto.erros.ErroCampo;
import io.rpps.emprestimo.controller.dto.erros.ErroResposta;
import io.rpps.emprestimo.controller.mappers.EmprestimoMapper;
import io.rpps.emprestimo.model.Emprestimo;
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

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService service;
    private final EmprestimoMapper mapper;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo aprovado",
                    content = @Content(schema = @Schema(implementation = EmprestimoAprovadoDTO.class))),
            @ApiResponse(responseCode = "200", description = "Empréstimo rejeitado",
                    content = @Content(schema = @Schema(implementation = EmprestimoRejeitadoDTO.class)))
    })
    public ResponseEntity<Object> criar(@RequestBody @Valid EmprestimoRequestDTO dto) {
        Object response = service.criarEmprestimo(dto);

        if(response instanceof EmprestimoAprovadoDTO aprovado){
            return ResponseEntity.ok(aprovado);
        }
        
        EmprestimoRejeitadoDTO rejeitado = (EmprestimoRejeitadoDTO) response;
        return ResponseEntity.ok(rejeitado);
        
    }

    @GetMapping("/{cpfContribuinte}")
    public ResponseEntity<List<EmprestimoResumoDTO>> consultarEmprestimosPorCpf(@PathVariable String cpfContribuinte) {
        List<Emprestimo> response = service.consultarEmprestimoPorCpf(cpfContribuinte);

        return ResponseEntity.ok(response
                .stream()
                .map(mapper::toResumoDTO)
                .toList());
    }

    @PostMapping("/simulacao")
    public ResponseEntity<SimularEmprestimoDTO> simularEmprestimo(@RequestBody @Valid EmprestimoRequestDTO dto){
        var simulacao = service.simularEmprestimo(dto);
        return ResponseEntity.ok(simulacao);
    }
}