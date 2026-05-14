# Etapa 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copiamos el .jar generado
COPY --from=build /app/target/*.jar app.jar

# Instalamos curl para el healthcheck de Docker
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]