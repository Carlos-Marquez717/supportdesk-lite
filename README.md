# SupportDesk Lite

API backend para gestion de tickets de soporte con SLA, asignaciones, comentarios, auditoria y automatizaciones.

Construido como proyecto de portfolio con Spring Boot 3, Java 21, PostgreSQL, Spring Data JPA, Flyway, Jakarta Validation y Swagger/OpenAPI.

## Funcionalidades

- Gestion de agentes de soporte.
- Creacion, actualizacion, asignacion, resolucion y reapertura de tickets.
- Filtros y paginacion para tickets.
- Calculo automatico de SLA por prioridad.
- Automatizacion programada para marcar tickets vencidos y escalar prioridad.
- Comentarios publicos e internos.
- Auditoria de eventos relevantes.
- Dashboard con metricas operacionales.
- Manejo global de errores con formato consistente.
- Datos seed para demo.

## Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Jakarta Validation
- Springdoc OpenAPI
- Docker Compose

## Requisitos

- Java 21
- Maven 3.9+
- Docker y Docker Compose para PostgreSQL

Docker es opcional si usas el perfil local con SQLite.

## Ejecucion sin Docker

Para entornos corporativos donde Docker esta bloqueado, el proyecto incluye el perfil `local` con SQLite. Este modo crea un archivo portable `supportdesk-lite.db` en la carpeta del proyecto.

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

En este perfil:

- La base de datos es SQLite.
- No hay usuario ni contrasena de base de datos.
- Flyway se desactiva para evitar SQL especifico de PostgreSQL.
- Hibernate usa `ddl-auto=update` para crear o actualizar tablas locales.
- Si la base local esta vacia, se cargan datos de demo minimos.

## Ejecucion con Docker/PostgreSQL

Levantar PostgreSQL:

```bash
docker compose up -d
```

Ejecutar la aplicacion:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=docker
```

Tambien puedes ejecutar sin perfil explicito porque la configuracion base usa PostgreSQL por defecto.

El perfil `postgres` tambien esta disponible como alias explicito:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Health check:

```text
http://localhost:8080/actuator/health
```

## Variables de Entorno

La aplicacion funciona con valores por defecto para desarrollo local:

```text
DB_URL=jdbc:postgresql://localhost:5432/supportdesk_lite
DB_USERNAME=supportdesk
DB_PASSWORD=supportdesk
SERVER_PORT=8080
```

## Endpoints Principales

### Agentes

```http
POST /api/v1/agents
GET /api/v1/agents
GET /api/v1/agents/{agentId}
PATCH /api/v1/agents/{agentId}/activate
PATCH /api/v1/agents/{agentId}/deactivate
```

### Tickets

```http
POST /api/v1/tickets
GET /api/v1/tickets
GET /api/v1/tickets/{ticketId}
PUT /api/v1/tickets/{ticketId}
PATCH /api/v1/tickets/{ticketId}/status
PATCH /api/v1/tickets/{ticketId}/assignment
PATCH /api/v1/tickets/{ticketId}/resolve
PATCH /api/v1/tickets/{ticketId}/reopen
DELETE /api/v1/tickets/{ticketId}
```

### Comentarios y Auditoria

```http
POST /api/v1/tickets/{ticketId}/comments
GET /api/v1/tickets/{ticketId}/comments
GET /api/v1/tickets/{ticketId}/audit-events
```

### Dashboard

```http
GET /api/v1/dashboard/summary
GET /api/v1/dashboard/tickets-by-priority
GET /api/v1/dashboard/tickets-by-agent
GET /api/v1/dashboard/recent-tickets?limit=10
```

## Ejemplo de Ticket

```json
{
  "title": "No puedo acceder a mi cuenta",
  "description": "El login devuelve error 500 desde el portal principal.",
  "priority": "HIGH",
  "category": "ACCOUNT",
  "requesterName": "Maria Perez",
  "requesterEmail": "maria@example.com"
}
```

## SLA

| Prioridad | Tiempo SLA |
| --- | --- |
| LOW | 72 horas |
| MEDIUM | 48 horas |
| HIGH | 24 horas |
| URGENT | 4 horas |

El job `TicketAutomationService` revisa tickets abiertos y vencidos cada 5 minutos por defecto. Si un ticket supera `dueAt`, lo marca como `slaBreached=true`, escala prioridad cuando aplica y registra eventos de auditoria.

## Errores

Formato estandar:

```json
{
  "timestamp": "2026-04-26T10:00:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "path": "/api/v1/tickets",
  "details": [
    {
      "field": "title",
      "message": "size must be between 5 and 160"
    }
  ]
}
```

## Pruebas

```bash
.\mvnw.cmd test
```

## Demo Sugerida para Portfolio

1. Abrir Swagger UI.
2. Crear un agente.
3. Crear un ticket urgente.
4. Mostrar el `dueAt` calculado.
5. Asignar el ticket a un agente activo.
6. Agregar un comentario interno.
7. Cambiar estado a `IN_PROGRESS`.
8. Resolver el ticket con resumen.
9. Consultar auditoria.
10. Mostrar dashboard.

## Capturas Recomendadas

- Swagger UI con endpoints.
- Creacion de ticket.
- Error de validacion.
- Asignacion de agente.
- Comentarios del ticket.
- Auditoria.
- Dashboard summary.
"# supportdesk-lite" 
