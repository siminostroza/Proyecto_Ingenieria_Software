# Aquí estuve yo sando

# 🏢 Building Repair Management System - Microservices Ecosystem

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)

## 📖 Descripción del Proyecto
El **Building Repair Management System** es una plataforma integral diseñada para gestionar, coordinar y facturar reparaciones en edificios. El ecosistema está construido bajo una **arquitectura de microservicios** (16 en total), enfocada en la alta disponibilidad, escalabilidad y la separación de responsabilidades funcionales.

El proyecto aborda todo el ciclo de vida de una reparación: desde la cotización y asignación de personal/flota, hasta la ejecución de la orden de trabajo, facturación y notificaciones asíncronas.

---

## 👥 Equipo de Desarrollo (Integrantes)
| Nombre | Rol / Especialidad | GitHub |
| :--- | :--- |
| **[Catalina Lobos]** | [@Shany-Ghost](https://github.com/Shany-Ghost) |
| **[Gabriel Sandoval]** | [@Sandy-Yera](https://github.com/Sandy-Yera) |
| **[Simón Inostroza]** | [@siminostroza](https://github.com/siminostroza) |

---

## 🏗️ Arquitectura y Tecnologías
El sistema sigue el patrón arquitectónico de microservicios utilizando el stack de **Spring Cloud**. Se implementó el patrón **CSR (Controller - Service - Repository)** en cada servicio, junto con pruebas unitarias y documentación OpenAPI/Swagger.

### Stack Tecnológico:
* **Backend:** Java 17+, Spring Boot 3.x
* **Infraestructura Cloud:** Spring Cloud Netflix Eureka (Registry), Spring Cloud Gateway
* **Persistencia:** MySQL (1 instancia única con 16 esquemas aislados)
* **Mensajería Asíncrona:** Apache Kafka + Zookeeper (implementado a partir del flujo de WorkOrders)
* **Comunicación Síncrona:** OpenFeign
* **Despliegue:** Docker & Docker Compose

---

## 🧩 Ecosistema de Microservicios
El ecosistema se divide en 6 fases lógicas que componen los 16 microservicios:

**Infraestructura Base:**
* `Service-Gateway`: Punto de entrada único.
* `Service-Registry`: Descubrimiento de servicios (Eureka).
* `Service-Security`: Gestión de roles y permisos.

**Fase 1: Núcleo de Identidad y Acceso**
* `Service-Auth`, `Service-Users`, `Service-Buildings`

**Fase 2 y 3: Ciclo de Cotización y Tarifas**
* `Service-Quotes`, `Service-Price-Engine`, `Service-Tariff` (Tarifario)

**Fase 4: Recursos y Logística**
* `Service-Staff`, `Service-Fleet`, `Service-Schedule`, `Service-Inventory`

**Fase 5: Operación y Ejecución (Flujo Crítico)**
* `Service-WorkOrders`, `Service-Logistics` (Orquestador central)

**Fase 6: Cierre, Pagos y Notificaciones**
* `Service-Payments`, `Service-Billing`, `Service-Notifications` (Vía Kafka)

---

## 🚀 Guía de Instalación y Despliegue

### Requisitos Previos
* Docker y Docker Compose instalados.
* Git.
* Puertos libres: `3306` (MySQL), `8761` (Eureka), `8080` (Gateway), `9092` (Kafka).

### Estructura de Contenedores
El proyecto se levanta mediante un archivo `docker-compose.yml` optimizado en **8 contenedores principales** con un orden de encendido estricto configurado mediante *Healthchecks* y dependencias (`depends_on`):

1. **MySQL** (Contiene las 16 bases de datos).
2. **Kafka + Zookeeper** (Broker de mensajería).
3. **Eureka Server** (Registro).
4. **API Gateway**.
5. **Bloque 1:** Identidad, acceso y seguridad.
6. **Bloque 2:** Operación.
7. **Bloque 3:** Logística.
8. **Bloque 4:** Pagos y soporte.

### Pasos para ejecutar:

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/tu-usuario/tu-repo.git](https://github.com/tu-usuario/tu-repo.git)
   cd tu-repo