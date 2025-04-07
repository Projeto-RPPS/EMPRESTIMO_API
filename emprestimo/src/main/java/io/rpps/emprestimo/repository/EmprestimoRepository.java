package io.rpps.emprestimo.repository;

import io.rpps.emprestimo.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByCpfContribuinte(String cpfContribuinte);
}
