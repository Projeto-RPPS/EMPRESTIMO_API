# 📄 API de Empréstimos Consignados

Sistema simples de gerenciamento de empréstimos consignados, com validação automática de margem consignável, geração de parcelas e simulações. Desenvolvido com Spring Boot, PostgreSQL, JPA, Swagger e outras tecnologias modernas do ecossistema Java.

---

## 🧱 Estrutura do Projeto

📁 documentacao
 - Assinaturas_Integrações.pdf
 - EAP.pdf
 - MER.pdf

📁 emprestimo
- Código Fonte API

---

## 🚀 Tecnologias Utilizadas

- Java 21
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
  Realiza o pagamento da próxima parcela pendente ou antecipa parcelas.

### 📌 Margem Consignável

- **GET /emprestimos/margem-consignavel/{cpfContribuinte}**  
  Consulta o valor da margem consignável disponível do contribuinte.

---

## ⚙️ Variáveis de Ambiente (.env)

```bash
# PostgreSQL Configuration
POSTGRES_PASSWORD=your_postgres_password
POSTGRES_USER=your_postgres_user
POSTGRES_DB=your_database_name

# PgAdmin Configuration
PGADMIN_DEFAULT_EMAIL=your_pgadmin_email@example.com
PGADMIN_DEFAULT_PASSWORD=your_pgadmin_password

# Application Database Connection
DB_URL=jdbc:postgresql://your_postgres_host:5432/your_database_name
DB_USER=your_db_user
DB_PASSWORD=your_db_password

# Application Info
APP_NAME=your_app_name
SERVER_PORT=your_app_port
```

---

## 🐳 Executando o Projeto

### 1. Clonar o repositório
```bash
git clone https://github.com/Projeto-RPPS/EMPRESTIMO_API.git
```
### 2. Acessar o diretório do projeto

``` bash
cd emprestimo
```

### 3. Subir os serviços com Docker Compose

``` bash
docker-compose up -d --build
```
#### Serviços incluídos:

- postgresdb — Banco de dados PostgreSQL (porta 5432)

- pgadmin — Interface para gerenciar o banco (acessível em http://localhost:15434)

- emprestimo_api — API do projeto (acessível em http://localhost:8085)

## 📖 Swagger UI

Acesse a documentação interativa da API em:

```bash
http://localhost:8085/swagger-ui.html
```

## 🧪 Testes e Validações
- Testes podem ser realizados via Postman ou diretamente pelo Swagger.
- As validações de entrada usam @Valid com mensagens personalizadas.
- Retorno de erros é tratado por um @ControllerAdvice.
