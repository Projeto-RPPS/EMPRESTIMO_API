package io.rpps.emprestimo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "emprestimo")
public class Emprestimo {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="quantidade_parcelas", nullable = false)
    private Integer quantidadeParcelas;

    @Column(name="valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusEmprestimo status;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @OneToMany(mappedBy = "emprestimo", fetch = FetchType.LAZY)
    private List<Parcela> parcela;

    @Column(name = "cpf_contribuinte", nullable = false, length = 11)
    private String cpfContribuinte;
}