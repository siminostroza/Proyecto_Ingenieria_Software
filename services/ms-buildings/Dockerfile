# Fase 1: Compilación limpia en entorno aislado
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias
COPY pom.xml .

# Descargar dependencias para guardarlas en caché de Docker
RUN mvn dependency:go-offline -B

# Copiar el código fuente del microservicio
COPY src ./src

# Compilar omitiendo los tests para acelerar el despliegue del laboratorio
RUN mvn clean package -DskipTests

# Fase 2: Imagen final de ejecución ligera
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el empaquetado .jar generado en la fase anterior
COPY --from=build /app/target/ms-buildings-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto interno inalterable de la red Docker
EXPOSE 8084

# Comando de arranque del microservicio con optimización de memoria
ENTRYPOINT ["java", "-jar", "app.jar"]