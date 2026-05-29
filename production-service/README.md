# FacLovers Production Service

Microserviço responsável pelo domínio de produção do FacLovers Microservices Lab. Ele gerencia funcionárias, modelos de peça, operações por modelo, lotes e registros de produção.

## Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- JWT com JJWT
- Spring Data JPA
- PostgreSQL
- Bean Validation
- Lombok
- Springdoc OpenAPI
- Gradle
- JaCoCo
- SonarQube
- Docker

## Segurança

Todos os endpoints de produção exigem `Authorization: Bearer TOKEN`.

O serviço não recebe `companyId` no body, path ou query param. O `companyId` é extraído do JWT gerado pelo `auth-service`, usando a mesma variável `JWT_SECRET`.

Claims esperadas:

- `companyId`
- `companyName`
- `companyEmail`

## Endpoints

### Employees

- `GET /employees`
- `GET /employees/{id}`
- `POST /employees`
- `PUT /employees/{id}`
- `DELETE /employees/{id}`

### Piece Models

- `GET /piece-models`
- `GET /piece-models/{id}`
- `POST /piece-models`
- `PUT /piece-models/{id}`
- `DELETE /piece-models/{id}`

### Operations

- `GET /operations/model/{pieceModelId}`
- `GET /operations/{id}`
- `POST /operations`
- `PUT /operations/{id}`
- `DELETE /operations/{id}`

### Lots

- `GET /lots`
- `GET /lots/{id}`
- `GET /lots/model/{pieceModelId}`
- `POST /lots`
- `PUT /lots/{id}`
- `DELETE /lots/{id}`

### Production Logs

- `GET /production-logs`
- `GET /production-logs/{id}`
- `GET /production-logs/lot/{lotId}`
- `GET /production-logs/employee/{employeeId}`
- `GET /production-logs/date?date=2026-05-28`
- `POST /production-logs`
- `DELETE /production-logs/{id}`

## Como Rodar Localmente

```bash
./gradlew bootRun
```

No Windows:

```bash
./gradlew.bat bootRun
```

Variáveis principais:

```bash
SERVER_PORT=8082
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/faclovers_auth
SPRING_DATASOURCE_USERNAME=faclovers
SPRING_DATASOURCE_PASSWORD=faclovers123
JWT_SECRET=faclovers_microservices_lab_secret_key_with_at_least_32_chars
AUTH_SERVICE_URL=http://localhost:8081
```

## Swagger

Com a aplicação rodando:

```text
http://localhost:8082/swagger-ui/index.html
```

Use o botão `Authorize` e informe o JWT no formato Bearer.

## Como Testar no Postman

1. Faça login no `auth-service`.
2. Copie o JWT retornado.
3. Crie uma variável `token` no Postman.
4. Envie os requests para `http://localhost:8082`.
5. Adicione o header:

```text
Authorization: Bearer {{token}}
```

Exemplo:

```http
POST /employees
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "name": "Maria",
  "role": "Costureira"
}
```

## Testes

```bash
./gradlew test
```

No Windows:

```bash
./gradlew.bat test
```

## JaCoCo

```bash
./gradlew jacocoTestReport
```

Relatório HTML:

```text
build/reports/jacoco/test/html/index.html
```

Relatório XML:

```text
build/reports/jacoco/test/jacocoTestReport.xml
```

## SonarQube

```bash
./gradlew sonar
```

Variáveis opcionais:

```bash
SONAR_HOST_URL=http://localhost:9000
SONAR_TOKEN=seu_token
```

## Docker

Build:

```bash
docker build -t faclovers-production-service .
```

Run:

```bash
docker run --rm -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/faclovers_auth \
  -e SPRING_DATASOURCE_USERNAME=faclovers \
  -e SPRING_DATASOURCE_PASSWORD=faclovers123 \
  -e JWT_SECRET=faclovers_microservices_lab_secret_key_with_at_least_32_chars \
  faclovers-production-service
```
