# Stage 1: build
FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app

# copia pom + fontes
COPY pom.xml .
COPY src ./src

# empacota a aplicação (pulando testes para acelerar)
RUN mvn clean package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# copia o jar gerado (pega qualquer jar no target)
COPY --from=builder /app/target/*.jar app.jar

# expõe a porta que vamos parametrizar
EXPOSE ${SERVER_PORT}

# entrypoint
ENTRYPOINT ["java","-jar","/app/app.jar"]
