# 📄 API de Empréstimos Consignados

Sistema simples de gerenciamento de empréstimos consignados, com validação automática de margem consignável, geração de parcelas e simulações. Desenvolvido com Spring Boot, PostgreSQL, JPA, Swagger e outras tecnologias modernas do ecossistema Java.

---

## 🧱 Estrutura do Projeto

📁 documentacao
 - Assinaturas_Integrações.pdf
 - EAP.pdf
 - MER.pdf

📁 emprestimo

---

## 🚀 Tecnologias Utilizadas

- Java 23
- Spring Boot 3.4.3
- Spring Data JPA
- PostgreSQL
- Lombok
- MapStruct
- Springdoc OpenAPI + Swagger UI
- Hibernate Utils
- Bean Validation

---

## 🔁 Funcionalidades da API

### 📌 Empréstimos

- **POST /emprestimos**  
  Criação de empréstimo com cálculo e validação automática da margem consignável.

- **GET /emprestimos/{cpfContribuinte}**  
  Lista todos os empréstimos associados a um determinado contribuinte.

- **POST /emprestimos/simulacao**  
  Simulação de empréstimo sem persistência no banco.

### 📌 Parcelas

- **GET /emprestimos/{idEmprestimo}/parcelas**  
  Lista todas as parcelas de um empréstimo.

- **POST /emprestimos/{idEmprestimo}/parcelas/pagar**  
  Realiza o pagamento da próxima parcela pendente.

### 📌 Margem Consignável

- **GET /emprestimos/margem-consignavel/{cpfContribuinte}**  
  Consulta o valor da margem consignável disponível do contribuinte.

---

## 📂 Documentação

A documentação completa está disponível na pasta `/documentacao`, incluindo:

- 🧾 Assinaturas de integração (endpoints, formatos de entrada/saída)
- 🧱 EAP (Estrutura Analítica do Projeto)
- 🧠 Modelo Entidade-Relacionamento (MER)

---

## 🔧 Executando o Projeto

```bash
# Clonar o projeto
git clone https://github.com/Projeto-RPPS/EMPRESTIMO_API.git

# Acessar a pasta
cd emprestimo

# Executar com Maven
./mvnw spring-boot:run
