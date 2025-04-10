# Etapa 1: build do projeto
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Diretório de trabalho no container
WORKDIR /app

# Copia o projeto inteiro
COPY . .

# Compila o projeto e gera o JAR
RUN mvn clean package -DskipTests

# Etapa 2: rodar o JAR
FROM eclipse-temurin:23-jdk-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta (mesmo valor de SERVER_PORT)
EXPOSE 8085

# Define o comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]