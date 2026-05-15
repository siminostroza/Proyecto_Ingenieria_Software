# Usamos una imagen ligera de Java 17
FROM eclipse-temurin:21-jre-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado por Maven desde la carpeta target
# Usamos un comodín (*) para que funcione sin importar la versión exacta
COPY target/*.jar app.jar

# Exponemos un puerto por defecto (el Config Server lo cambiará luego)
EXPOSE 8080

# Comando para arrancar el microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]