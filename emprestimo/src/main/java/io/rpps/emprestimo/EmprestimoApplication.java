package io.rpps.emprestimo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class EmprestimoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmprestimoApplication.class, args);
	}

}
