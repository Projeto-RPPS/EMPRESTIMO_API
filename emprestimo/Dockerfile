# Etapa 1: Build com Maven
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

# Copia o pom.xml e resolve dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do projeto e compila
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Runtime com JDK 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o JAR gerado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8085

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]