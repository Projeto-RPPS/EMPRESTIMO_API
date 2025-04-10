package io.rpps.emprestimo.repository;

import io.rpps.emprestimo.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    @Query("""
              SELECT e FROM Emprestimo e
              WHERE e.cpfContribuinte = ?1
              AND e.status = 'APROVADO'
              """)
    List<Emprestimo> consultarPorCpf(String cpfContribuinte);
}
