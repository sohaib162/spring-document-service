# Document Service (Spring Boot + JWT)

A small **document management** microservice built with Spring Boot. It manages **Users**, **Departments** (many‑to‑many with users), **Categories**, and **Documents**, and protects `/api/**` routes with a stateless JWT filter.

![Swagger UI](swagger.png)

## Features
- CRUD for **Departments**, **Categories**, **Documents**, and **Users**
- JWT (HS256) verification via a lightweight filter (`JwtAuthFilter`) + per‑request `UserContext`
- Department membership included in JWT claims (e.g. `departments: [1,2]`)
- JPA entities with relationships:
  - `User ↔ Department` (many‑to‑many via join table)
  - `Document → Department` (many‑to‑one)
  - `Document → Category` (many‑to‑one)
  - `DocumentStatus` enum

## Tech Stack
- Java 17+, Spring Boot 3 (Web, Security, Data JPA)
- PostgreSQL (dev/prod), H2 (tests optional)
- JJWT (io.jsonwebtoken), Lombok

## Project Structure (short)
```
src/main/java/com/example/document/
  controller/     # DepartmentController, CategoryController, DocumentController, UserController
  model/          # User, Department, Category, Document, DocumentStatus
  repository/     # Spring Data JPA repositories
  security/       # JwtAuthFilter, UserContext
  service/        # DepartmentService, CategoryService, DocumentService, UserService
  SecurityConfig.java
  DocumentserviceApplication.java
src/main/resources/
  application.yaml

```

## Quick Start

### 1) Prerequisites
- Java 17+
- Maven or Gradle
- PostgreSQL running (or update the JDBC URL)


### 2) Run
```bash
# Maven
mvn spring-boot:run

# or Gradle
./gradlew bootRun
```
App defaults to `http://localhost:8082` (see `server.port`).

### 3) API & Docs
- **Swagger UI:** `http://localhost:8082/swagger-ui/index.html`
- **OpenAPI JSON:** `http://localhost:8082/v3/api-docs`

Authenticate by sending the JWT from your auth provider:
```
Authorization: Bearer <your-jwt>
```

## JWT Claims (example)
```json
{
  "sub": "alice@example.com",
  "role": "USER",
  "departments": [1, 2],
  "iat": 1710000000,
  "exp": 1710086400
}
```
> The service **verifies** tokens; it does **not** mint them. Supply a valid token issued by your Auth service.

## API Quick Tour (typical routes)
> Replace IDs and bodies to match your DTOs and controllers.
```
GET    /api/departments
POST   /api/departments
PUT    /api/departments/{id}
DELETE /api/departments/{id}

GET    /api/categories
POST   /api/categories
PUT    /api/categories/{id}
DELETE /api/categories/{id}

GET    /api/documents
POST   /api/documents
GET    /api/documents/{id}
PUT    /api/documents/{id}
DELETE /api/documents/{id}

GET    /api/users
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### Smoke Tests (curl examples)
```bash
# List departments
curl -H "Authorization: Bearer $TOKEN" http://localhost:8082/api/departments

# Create a category
curl -X POST http://localhost:8082/api/categories   -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json"   -d '{"name":"Policies"}'

# Create a document (example shape)
curl -X POST http://localhost:8082/api/documents   -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json"   -d '{"title":"HR Policy v1","description":"...","departmentId":1,"categoryId":1,"fileUrl":"https://...","status":"DRAFT"}'
```

## Development Notes
- `SecurityConfig` protects `/api/**` and adds `JwtAuthFilter` before `UsernamePasswordAuthenticationFilter`.
- `UserContext` holds `email`, `role`, and `departments` for the current request; it is **cleared** after each request.
- Keep entity ID types consistent across entities, DTOs, `UserContext`, and JWT claims (`Long` recommended).
- For production, replace `ddl-auto` with migrations (Flyway/Liquibase), and set stricter logging.

