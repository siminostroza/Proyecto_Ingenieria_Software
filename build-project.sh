#!/bin/bash

# Nombre: build-project.sh
# Objetivo: Compilar todo el ecosistema sin configurar nada en la PC host.

echo "Limpiando formatos de archivo (Windows -> Linux)..."
sed -i 's/\r$//' build-project.sh
sed -i 's/\r$//' compile-all.sh

echo "Asegurando permisos de ejecución..."
chmod +x compile-all.sh

echo "Iniciando proceso de construcción universal..."

# Definimos una carpeta para la caché de Maven (para que no descargue todo cada vez)
MAVEN_CACHE_DIR="$HOME/.m2"
mkdir -p "$MAVEN_CACHE_DIR"

echo "Usando entorno Docker para compilación limpia..."

MSYS_NO_PATHCONV=1 docker run --rm -it \
    -v "$(pwd)":/app \
    -v "$MAVEN_CACHE_DIR":/root/.m2 \
    -w /app \
    maven:3.9.6-eclipse-temurin-21 \
    sh compile-all.sh

if [ $? -eq 0 ]; then
    echo "Paso 2: Compilación exitosa. Levantando infraestructura..."
    docker-compose -f docker/infra-docker/compose.yml up --build -d
    echo "✅ ¡Todo listo! Accede a Eureka para comprobarlo usando: http://localhost:8761"
else
    echo "❌ Error: La compilación falló. Revisa los logs arriba."
    exit 1
fi
