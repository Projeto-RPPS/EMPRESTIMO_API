package io.rpps.emprestimo.controller;

import io.rpps.emprestimo.controller.dto.margemConsignavel.MargemConsignavelDTO;
import io.rpps.emprestimo.service.MargemConsignavelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/emprestimos")
public class MargemConsignavelController {

    private final MargemConsignavelService margemConsignavelService;

    @GetMapping("/margem-consignavel/{cpfContribuinte}")
    public ResponseEntity<MargemConsignavelDTO> consultarMargem(@PathVariable String cpfContribuinte){
        return ResponseEntity.ok(margemConsignavelService.calcularMargem(cpfContribuinte));
    }
}
