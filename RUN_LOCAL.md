# Ejecucion Local sin Docker

Este proyecto incluye un modo local con SQLite porque algunos entornos corporativos bloquean Docker o no permiten levantar contenedores. El perfil `local` permite ejecutar la API usando un archivo de base de datos portable llamado `supportdesk-lite.db`.

El modo PostgreSQL sigue disponible y el archivo `docker-compose.yml` no fue eliminado.

## Requisitos

- Java 21
- Maven 3.9+
- No requiere Docker para el perfil `local`

## Ejecutar sin Docker con SQLite

Desde la carpeta del proyecto:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Esto crea o reutiliza el archivo:

```text
supportdesk-lite.db
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Health check:

```text
http://localhost:8080/actuator/health
```

## Ejecutar con Docker y PostgreSQL

Cuando Docker este disponible:

```bash
docker compose up -d
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=docker
```

Tambien puedes usar el alias `postgres`:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

Tambien puedes ejecutar sin perfil explicito, porque la configuracion base usa PostgreSQL por defecto:

```bash
.\mvnw.cmd spring-boot:run
```

## Endpoints Principales

```http
POST /api/v1/agents
GET /api/v1/agents
POST /api/v1/tickets
GET /api/v1/tickets
GET /api/v1/tickets/{ticketId}
PATCH /api/v1/tickets/{ticketId}/assignment
PATCH /api/v1/tickets/{ticketId}/status
PATCH /api/v1/tickets/{ticketId}/resolve
POST /api/v1/tickets/{ticketId}/comments
GET /api/v1/tickets/{ticketId}/audit-events
GET /api/v1/dashboard/summary
```

## Notas de Base de Datos

- Perfil `docker`: usa PostgreSQL, Flyway y validacion de schema.
- Perfil `postgres`: alias equivalente para PostgreSQL local.
- Perfil `local`: usa SQLite, desactiva Flyway y usa `spring.jpa.hibernate.ddl-auto=update`.

La decision de desactivar Flyway en SQLite evita incompatibilidades con SQL especifico de PostgreSQL, como `uuid`, `now()` e intervalos.

El perfil `local` incluye un seed automatico minimo si la base SQLite esta vacia, para que Swagger tenga datos de demo sin depender de Docker.

## Troubleshooting

### Maven no esta instalado

Verifica:

```bash
.\mvnw.cmd -version
```

Si no existe, instala Maven o usa un IDE que lo incluya.

### El archivo SQLite queda con datos viejos

Deten la aplicacion y elimina:

```text
supportdesk-lite.db
```

La aplicacion lo volvera a crear al iniciar con el perfil `local`.

### Puerto 8080 ocupado

Ejecuta con otro puerto:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.arguments=--server.port=8081
```

### Docker bloqueado

Usa el perfil local:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```
