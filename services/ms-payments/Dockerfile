# Usamos una imagen ligera y segura de Java 21
FROM eclipse-temurin:21-jre-alpine

# Copiamos el archivo JAR generado por Maven desde la carpeta target
COPY target/*.jar app.jar

# Comando optimizado para arrancar el microservicio
ENTRYPOINT ["java", "-jar", "/app.jar"]