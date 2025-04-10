package io.rpps.emprestimo.controller;

import io.rpps.emprestimo.controller.dto.emprestimo.EmprestimoResumoDTO;
import io.rpps.emprestimo.controller.dto.erros.ErroResposta;
import io.rpps.emprestimo.controller.dto.parcelas.PagamentoResponseDTO;
import io.rpps.emprestimo.controller.dto.parcelas.ParcelaDTO;
import io.rpps.emprestimo.controller.mappers.ParcelaMapper;
import io.rpps.emprestimo.model.Parcela;
import io.rpps.emprestimo.service.ParcelaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emprestimos")
public class ParcelaController {

    private final ParcelaService service;
    private final ParcelaMapper mapper;

    @GetMapping("/{idEmprestimo}/parcelas")
    public ResponseEntity<List<ParcelaDTO>> listarParcelas(@PathVariable Long idEmprestimo){
        List<Parcela> parcelas = service.buscarPorEmprestimo(idEmprestimo);

        return ResponseEntity.ok(parcelas
                .stream()
                .map(mapper::toDTO)
                .toList());
    }

    @PostMapping("/{idEmprestimo}/parcelas/pagar")
    public ResponseEntity<PagamentoResponseDTO> pagarParcela(@PathVariable Long idEmprestimo,
                                                             @RequestParam(required = false) Integer numeroParcela) {
        String mensagem = service.pagarOuAnteciparParcelas(idEmprestimo, numeroParcela);
        return ResponseEntity.ok(new PagamentoResponseDTO(mensagem));
    }
}
