CREATE TABLE emprestimo (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cpf_contribuinte VARCHAR(11) NOT NULL,
    quantidade_parcelas INTEGER NOT NULL,
    valor_total DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_inicio DATE NOT NULL,
    CONSTRAINT chk_status_aprovacao CHECK (
        status IN ('APROVADO', 'REJEITADO')
    )
);

CREATE TABLE parcela (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_emprestimo BIGINT NOT NULL,
    numero_parcela INTEGER NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(10,2) NOT NULL,
    paga BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_emprestimo FOREIGN KEY (id_emprestimo) REFERENCES emprestimo(id) ON DELETE CASCADE
);