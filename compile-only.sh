#!/bin/bash

# 1. Limpieza de formatos (CRLF -> LF)
# Limpiamos los tres para estar seguros, incluyendo este mismo archivo
sed -i 's/\r$//' build-project.sh
sed -i 's/\r$//' compile-only.sh
sed -i 's/\r$//' compile-all.sh

echo "Asegurando permisos de ejecución..."
chmod +x compile-all.sh

# 2. Configuración de caché
MAVEN_CACHE_DIR="$HOME/.m2"
mkdir -p "$MAVEN_CACHE_DIR"

echo "🚀 Iniciando compilación aislada en Docker..."

# 3. Ejecución de la compilación
MSYS_NO_PATHCONV=1 docker run --rm -it \
    -v "$(pwd)":/app \
    -v "$MAVEN_CACHE_DIR":/root/.m2 \
    -w /app \
    maven:3.9.6-eclipse-temurin-21 \
    sh compile-all.sh

# 4. Actualización inteligente de Docker Compose
if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa. Actualizando contenedores..."
    docker-compose -f docker/infra-docker/compose.yml up --build -d
    echo "✨ ¡Listo! Verifica en http://localhost:8761"
else
    echo "❌ Error en la compilación."
    exit 1
fi