#!/bin/sh

# Cada vez que lo quieran usar en una pc nueva, darle permisos:
# chmod +x compile-all.sh **(se automatizó con build.sh)

# Colores para que la consola se vea pro
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# Lista de rutas (Se agregan separadas por espacio)
# Adaptado para ser compatible con el entorno Docker (sh/dash)
SERVICES="
infraestructure/eureka-server
infraestructure/config-server 
infraestructure/api-gateway
services/ms-auth
services/ms-logs
services/ms-users
services/ms-buildings
services/ms-staff
services/ms-fleet
services/ms-security
services/ms-quotes
services/ms-inventory
services/ms-payments
services/ms-billing
services/ms-logistics
services/ms-notifications
services/ms-workorders
services/ms-schedule
services/ms-purchase
services/ms-price-engine
"

# --- DETECCIÓN DINÁMICA DE ARGUMENTOS ---
# Si se pasa un argumento, filtramos la lista para aislar el microservicio
if [ -n "$1" ]; then
    MATCHED_SERVICE=""
    for CURRENT in $SERVICES; do
        # Compara si el argumento es igual al nombre de la carpeta o a la ruta completa
        case "$CURRENT" in
            *"$1"*) MATCHED_SERVICE="$CURRENT" ;;
        esac
    done

    if [ -n "$MATCHED_SERVICE" ]; then
        SERVICES="$MATCHED_SERVICE"
        printf "${YELLOW}🎯 Modo aislado activo: Se compilará únicamente [ $SERVICES ]${NC}\n"
    else
        printf "${RED}❌ Error: El servicio '$1' no coincide con ninguna ruta válida en el proyecto.${NC}\n"
        exit 1
    fi
else
    printf "${GREEN}Iniciando compilación masiva de microservicios (Limpieza y Empaquetado)...${NC}\n"
fi
# ----------------------------------------

for SERVICE in $SERVICES; do
    printf "${GREEN}----------------------------------------------------${NC}\n"
    printf "${GREEN}Compilando: $SERVICE...${NC}\n"
    printf "${GREEN}----------------------------------------------------${NC}\n"
    
    # Verificamos si el directorio existe antes de entrar
    if [ -d "$SERVICE" ]; then
        cd "$SERVICE" || exit
        
        # mvn clean: Borra la carpeta /target (descompila lo viejo)
        # package: Genera el nuevo .jar
        # -DskipTests: Salta los tests para que la compilación sea veloz
        # Usamos mvn directamente porque el contenedor ya lo tiene instalado
        if mvn clean package -DskipTests; then
            printf "${GREEN}✅ Exito en $SERVICE${NC}\n"
        else
            printf "${RED}❌ Error compilando $SERVICE. Abortando.${NC}\n"
            exit 1
        fi
        
        cd - > /dev/null
    else
        printf "${YELLOW}⚠️  Advertencia: El directorio $SERVICE no existe. Saltando...${NC}\n"
    fi
done

printf "${GREEN}----------------------------------------------------${NC}\n"
printf "${GREEN}¡Todos los servicios requeridos han sido compilados con éxito!${NC}\n"
printf "${GREEN}Ya puedes ejecutar: docker-compose up --build -d${NC}\n"
printf "${GREEN}----------------------------------------------------${NC}\n"