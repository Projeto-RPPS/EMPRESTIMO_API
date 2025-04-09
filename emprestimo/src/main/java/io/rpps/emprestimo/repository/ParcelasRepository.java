package io.rpps.emprestimo.repository;

import io.rpps.emprestimo.model.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParcelasRepository extends JpaRepository<Parcela, Long> {

    List<Parcela> findByEmprestimoId(Long idEmprestimo);

    @Query("""
            SELECT p FROM Parcela p
            WHERE p.emprestimo.id = ?1
            AND p.paga = false
            ORDER BY p.dataVencimento ASC
            """)
    List<Parcela> buscarParcelasPendentesOrdenadas(Long idEmprestimo);

}
