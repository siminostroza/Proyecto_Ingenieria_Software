#!/bin/bash

# 1. Limpieza de formatos (CRLF -> LF)
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
    sh compile-all.sh "$1"

# Guardamos el resultado de la compilación
COMPILE_STATUS=$?

# 4. Procesamiento post-compilación (Limpieza de BD y Despliegue)
if [ $COMPILE_STATUS -eq 0 ]; then
    echo "✅ Compilación exitosa."
    
    # --- RESETEO INTELIGENTE DE BASE DE DATOS ---
    if [ -n "$1" ]; then
        # Extraemos el nombre limpio (por si pasan rutas completas como 'services/ms-users')
        CLEAN_NAME=$(basename "$1")
        
        # Filtramos componentes de infraestructura que no usan base de datos de negocio
        if [[ "$CLEAN_NAME" != *"server"* && "$CLEAN_NAME" != *"gateway"* ]]; then
            
            # Eliminamos el prefijo 'ms-' (ej: ms-users -> users)
            DB_SUFFIX=${CLEAN_NAME#ms-}
            
            # 🚨 CASO ESPECIAL: Ajustamos la excepción detectada en tu init.sql (purchase -> purchases)
            if [ "$DB_SUFFIX" = "purchase" ]; then
                DB_SUFFIX="purchases"
            fi
            
            DB_NAME="db_service_${DB_SUFFIX}"
            
            echo "🧹 Detectado modo aislado para: $CLEAN_NAME"
            echo "💥 Destruyendo y recreando base de datos: $DB_NAME ..."
            
            # 🔍 CORRECCIÓN: Apuntamos al nombre real del contenedor 'logistica-mysql'
            # Se usa la clave 'root' que mantuviste en tu archivo compose.yml
            docker exec -i logistica-mysql mysql -uroot -proot -e \
                "DROP DATABASE IF EXISTS \`${DB_NAME}\`; CREATE DATABASE \`${DB_NAME}\`;"
            
            if [ $? -eq 0 ]; then
                echo "✨ Base de datos '${DB_NAME}' reseteada con éxito. Hibernate la inicializará limpia."
            else
                echo "⚠️  Advertencia: No se pudo resetear la BD (Comprueba los accesos expuestos arriba)."
            fi
            
            # ⏱️ Pausa estratégica de 5 segundos para alcanzar a leer el resultado en la consola
            echo "⏱️  ESPERANDOOOOO 5 segundos antes de levantar el contenedor..."
            sleep 5
        fi
    fi
    # ---------------------------------------------

# 4. Actualización inteligente y AISLADA de Docker Compose
    if [ -n "$1" ] && [[ "$CLEAN_NAME" != *"server"* && "$CLEAN_NAME" != *"gateway"* ]]; then
        # Traducimos el nombre del parámetro al nombre del servicio en el compose.yml
        # Si tus servicios se llaman brm-ms-users, brm-ms-billing, etc:
        SERVICE_CONTAINER_NAME="brm-${CLEAN_NAME}"
        
        echo "🔄 Modo aislado activo en Docker: Actualizando SOLO el contenedor [ $SERVICE_CONTAINER_NAME ]..."
        docker-compose -f docker/infra-docker/compose.yml up --build -d "$SERVICE_CONTAINER_NAME"
    else
        # Si se corrió sin argumentos o es infraestructura, actualiza todo normalmente
        echo "🔄 Actualizando todo el ecosistema de contenedores..."
        docker-compose -f docker/infra-docker/compose.yml up --build -d
    fi

    echo "✨ ¡Listo! Verifica en http://localhost:8761"
else
    echo "❌ Error en la compilación."
    exit 1
fi