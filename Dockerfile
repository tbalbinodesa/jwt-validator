# Estágio 1: Build da aplicação com Maven
FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Estágio 2: Criação da imagem final
FROM openjdk:26-jdk-slim
WORKDIR /app
# Copia o JAR do estágio de build
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta que a aplicação Spring Boot usa
EXPOSE 8080
# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
