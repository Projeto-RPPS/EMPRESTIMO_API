package io.rpps.emprestimo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "parcela")
@Data
public class Parcela {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_parcela", nullable = false)
    private Integer numeroParcela;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "paga", nullable = false)
    private Boolean paga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;
}
