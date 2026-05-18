![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)

# 🏢 Building Repair Management System
### Proyecto Semestral — Arquitectura de Microservicios

---

## 📖 Descripción del Proyecto

El **Building Repair Management System** es un ecosistema integral basado en una **arquitectura de microservicios** diseñado para gestionar todo el ciclo de vida de las reparaciones en edificios: desde la cotización inicial hasta la facturación y el historial de mantenimiento.

El sistema coordina de forma eficiente el personal técnico, la flota de vehículos y los materiales necesarios para realizar reparaciones, cubriendo procesos críticos como:

- **Gestión de Identidad:** Registro de usuarios, autenticación y edificios.
- **Ciclo de Cotización:** Solicitudes de presupuesto y motores de precios automáticos.
- **Operación Logística:** Asignación de técnicos, vehículos, agendas e inventario.
- **Soporte Financiero:** Procesamiento de pagos y generación de facturas.
- **Notificaciones Asíncronas:** Comunicación por eventos mediante Apache Kafka.

El ecosistema está compuesto por **16 microservicios independientes**, cada uno con su propia base de datos aislada, siguiendo el patrón arquitectónico **CSR (Controller – Service – Repository)** y principios de diseño REST.

---

## 👥 Integrantes del Equipo

| Nombre | GitHub |
|:---|:---|
| Catalina Lobos | [@Shany-Ghost](https://github.com/Shany-Ghost) |
| Gabriel Sandoval | [@Sandy-Yera](https://github.com/Sandy-Yera) |
| Simón Inostroza | [@siminostroza](https://github.com/siminostroza) |

---

## 🛠️ Stack Tecnológico

| Componente | Tecnología |
|:---|:---|
| Lenguaje | Java 21+ |
| Framework | Spring Boot 3.x |
| Persistencia | JPA + Hibernate + MySQL |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Comunicación síncrona | OpenFeign |
| Mensajería asíncrona | Apache Kafka + Zookeeper |
| Validaciones | Bean Validation (JSR 380) |
| Contenedores | Docker & Docker Compose |
| Control de versiones | Git + GitHub |

---

## 🧩 Ecosistema de Microservicios

El sistema se organiza en **6 fases lógicas** que componen los 16 microservicios:

### 🔧 Infraestructura Base

| Servicio | Puerto | Descripción |
|:---|:---|:---|
| `service-gateway` | 8080 | Punto de entrada único (API Gateway) |
| `service-registry` | 8761 | Descubrimiento de servicios (Eureka Server) |
| `service-security` | 8084 | Gestión centralizada de roles y permisos |

### 🔑 Fase 1 — Núcleo de Identidad y Acceso

| Servicio | Descripción |
|:---|:---|
| `service-auth` | Autenticación y emisión de tokens |
| `service-users` | Registro y gestión de usuarios del sistema |
| `service-buildings` | Gestión del catálogo de edificios |

### 💰 Fase 2 y 3 — Ciclo de Cotización y Tarifas

| Servicio | Descripción |
|:---|:---|
| `service-quotes` | Creación y seguimiento de solicitudes de cotización |
| `service-price-engine` | Motor automático de cálculo de precios |
| `service-tariff` | Gestión del tarifario de servicios |

### 🚚 Fase 4 — Recursos y Logística

| Servicio | Descripción |
|:---|:---|
| `service-staff` | Gestión del personal técnico |
| `service-fleet` | Control de la flota de vehículos |
| `service-schedule` | Agendamiento y disponibilidad |
| `service-inventory` | Control de materiales e insumos |

### ⚙️ Fase 5 — Operación y Ejecución

| Servicio | Descripción |
|:---|:---|
| `service-workorders` | Gestión de órdenes de trabajo (flujo crítico) |
| `service-logistics` | Orquestador central de operaciones |

### 🧾 Fase 6 — Cierre, Pagos y Notificaciones

| Servicio | Descripción |
|:---|:---|
| `service-payments` | Procesamiento de pagos |
| `service-billing` | Generación de facturas legales |
| `service-notifications` | Notificaciones asíncronas vía Kafka |

---

## ✅ Funcionalidades Implementadas

### Arquitectura y Estructura
- Patrón **CSR** aplicado en todos los microservicios (Controller → Service → Repository)
- Separación estricta de responsabilidades por capa y por paquete
- Uso de **DTOs** para comunicación entre capas y entrada/salida de datos
- Cada microservicio posee su propio esquema de base de datos aislado

### Persistencia con JPA + Hibernate
- Entidades modeladas con anotaciones `@Entity`, `@Id`, `@GeneratedValue`, `@OneToMany`, `@ManyToOne`
- Repositorios implementados con `JpaRepository` para operaciones CRUD
- Configuración del datasource y dialecto en `application.properties`
- Scripts SQL de migración inicial de base de datos

### CRUD Completo
- Operaciones **GET, POST, PUT, DELETE** implementadas en todos los microservicios
- Endpoints retornan JSON estructurado con `ResponseEntity`
- Códigos HTTP adecuados según la operación y resultado

### Validaciones con Bean Validation (JSR 380)
- Anotaciones de validación en DTOs (`@NotNull`, `@NotBlank`, `@Size`, `@Min`, etc.)
- Respuestas consistentes ante entradas inválidas
- Separación limpia entre entidades JPA y objetos de transferencia

### Manejo de Excepciones
- Uso de `try/catch` en la capa de servicio
- `@ControllerAdvice` para manejo centralizado de errores
- Respuestas estructuradas con códigos HTTP correctos (400, 404, 409, 500, etc.)

### Reglas de Negocio
- Lógica de cotización automática mediante el motor de precios
- Restricciones de disponibilidad de técnicos y vehículos
- Control de integridad referencial entre entidades del dominio
- Flujo completo de una orden de trabajo: cotización → asignación → ejecución → facturación

### Comunicación entre Microservicios
- **Síncrona:** OpenFeign para consultas en tiempo real entre servicios
- **Asíncrona:** Apache Kafka para notificaciones y eventos del ciclo de vida
- Manejo de timeouts y errores en llamadas remotas

### Logs Estructurados
- Trazabilidad entre capas: creación, actualización, errores y validaciones fallidas

### Herramientas Colaborativas
- Repositorio GitHub con historial de commits técnicos y progresivos
- Tablero de tareas con asignaciones por integrante

---

## 🗂️ Estructura del Repositorio

```
Proyecto_Ingenieria_Software/
│
├── services/                    # Microservicios de negocio
│   ├── service-auth/
│   ├── service-users/
│   ├── service-buildings/
│   ├── service-quotes/
│   ├── service-price-engine/
│   ├── service-tariff/
│   ├── service-staff/
│   ├── service-fleet/
│   ├── service-schedule/
│   ├── service-inventory/
│   ├── service-workorders/
│   ├── service-logistics/
│   ├── service-payments/
│   ├── service-billing/
│   └── service-notifications/
│
├── infraestructure/             # Servicios de infraestructura (Gateway, Registry, Security)
│
├── docker/infra-docker/         # Archivos Docker Compose
│
├── config-repo/                 # Configuración centralizada
│
├── build-project.sh             # Script de compilación y despliegue completo
├── compile-all.sh               # Script de compilación de todos los servicios
├── compile-only.sh              # Script de compilación selectiva
└── README.md
```

Cada microservicio sigue la estructura de paquetes estándar:

```
service-xxx/
└── src/main/java/com/brms/xxx/
    ├── controller/      # Manejo de solicitudes REST
    ├── service/         # Lógica de negocio
    ├── repository/      # Acceso a datos (JpaRepository)
    ├── model/           # Entidades JPA
    ├── dto/             # Objetos de transferencia de datos
    ├── client/          # Conexión entre microservicios mediante OpenFeign
    └── exception/       # Manejo de errores y @ControllerAdvice
```

---

## 🚀 Pasos para Ejecutar

### Requisitos Previos

Asegúrate de tener instalado:
- **Docker** y **Docker Compose**
- **Git**
- Puertos disponibles: `3306` (MySQL), `8761` (Eureka), `8080` (Gateway), `9092` (Kafka)

### Instalación

**1. Clonar el repositorio**

```bash
git clone https://github.com/siminostroza/Proyecto_Ingenieria_Software.git
cd Proyecto_Ingenieria_Software
```

**2. Ejecutar el script de construcción y despliegue**

```bash
./build-project.sh
```

Este script se encarga automáticamente de:
- Compilar todos los microservicios con Maven
- Construir las imágenes Docker de cada servicio
- Levantar los contenedores en el orden correcto mediante Docker Compose

### Orden de Inicio de Contenedores

El sistema respeta un orden de encendido estricto configurado con *healthchecks* y `depends_on`:

1. MySQL (16 esquemas de base de datos)
2. Zookeeper + Kafka
3. Eureka Server (Service Registry)
4. API Gateway
5. Servicios de identidad y seguridad
6. Servicios de cotización y tarifas
7. Servicios de recursos y logística
8. Servicios de pagos y notificaciones

### Verificación del Despliegue

Una vez levantado el sistema:

- **Eureka Dashboard:** http://localhost:8761
- **API Gateway:** http://localhost:8080

Todos los microservicios se registran automáticamente en Eureka y son accesibles a través del Gateway.

### Pruebas de Endpoints

Realiza llamadas directamente a través del Gateway con Postman u otra herramienta REST.

> **Estado actual:** Microservicios operativos: `service-users`, `service-auth`, `service-logs`.

---

#### 👤 service-users — `GET /api/users`

| Método | Endpoint | Descripción | Respuesta exitosa |
|:---|:---|:---|:---|
| GET | `/api/users` | Lista todos los usuarios registrados | `200 OK` / `204 No Content` si no hay usuarios |
| GET | `/api/users/existe?id={id}` | Verifica si existe un usuario por ID | `200 OK` con `true` o `false` |
| GET | `/api/users/existe?rut={rut}` | Verifica si existe un usuario por RUT | `200 OK` con `true` o `false` |
| GET | `/api/users/total-usuarios` | Retorna un mensaje con el total de usuarios | `200 OK` con texto |
| POST | `/api/users` | Crea un nuevo usuario (body: `UserRegisterDTO`) | `201 Created` |
| PUT | `/api/users/{id}` | Actualiza los datos de un usuario por ID | `200 OK` |
| DELETE | `/api/users/{id}` | Elimina un usuario por ID | `204 No Content` |

**Notas:**
- `GET /api/users/existe` acepta solo un parámetro a la vez (`id` **o** `rut`). Si se envían ambos simultáneamente retorna `400 Bad Request`.
- El body de `POST` y `PUT` debe ser un JSON válido según `UserRegisterDTO` con sus validaciones (`@Valid`).

---

#### 🔐 service-auth — `GET /api/auth`

| Método | Endpoint | Descripción | Respuesta exitosa |
|:---|:---|:---|:---|
| GET | `/api/auth` | Lista todas las credenciales registradas | `200 OK` / `204 No Content` si no hay registros |
| GET | `/api/auth/existe?id={id}` | Verifica si existe una credencial por ID | `200 OK` con `true` o `false` |
| GET | `/api/auth/existe?username={username}` | Verifica si existe una credencial por username | `200 OK` con `true` o `false` |
| POST | `/api/auth` | Crea una nueva credencial (llamada interna desde `service-users` vía OpenFeign) | `201 Created` |
| PUT | `/api/auth/{id}` | Actualiza una credencial por ID | `200 OK` |
| DELETE | `/api/auth/{id}` | Elimina una credencial por ID | `204 No Content` |

**Notas:**
- `POST /api/auth` es un endpoint receptor: es invocado internamente por `service-users` mediante **OpenFeign** al momento de registrar un usuario. No está pensado para ser llamado directamente en producción.
- `GET /api/auth/existe` requiere exactamente un parámetro (`id` o `username`). Sin parámetros retorna `400 Bad Request`.

---

#### 📋 service-logs — `/api/logs`

| Método | Endpoint | Descripción | Respuesta exitosa |
|:---|:---|:---|:---|
| GET | `/api/logs` | Lista todos los registros de log del sistema | `200 OK` |

**Notas:**
- Los logs son generados automáticamente por `service-users` y `service-auth` mediante **Kafka** (`KafkaLogProducer`). No se crean manualmente vía API.
- Este endpoint es de solo lectura (consulta).
