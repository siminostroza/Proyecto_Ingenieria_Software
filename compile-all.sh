#!/bin/sh

# Cada vez que lo quieran usar en una pc nueva, darle permisos:
# chmod +x compile-all.sh **(se automatizó con build.sh)

# Colores para que la consola se vea pro
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

printf "${GREEN}Iniciando compilación masiva de microservicios (Limpieza y Empaquetado)...${NC}\n"

# Lista de rutas (Se agregan separadas por espacio)
# Adaptado para ser compatible con el entorno Docker (sh/dash)
SERVICES="
infraestructure/eureka-server
infraestructure/config-server 
infraestructure/api-gateway
"
# Próximos servicios a habilitar:
# services/ms-users
# services/ms-logistics

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
printf "${GREEN}¡Todos los servicios han sido compilados con éxito!${NC}\n"
printf "${GREEN}Ya puedes ejecutar: docker-compose up --build -d${NC}\n"
printf "${GREEN}----------------------------------------------------${NC}\n"