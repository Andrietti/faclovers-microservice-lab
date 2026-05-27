# FacLovers Auth Service

Microserviço responsável por cadastro de empresas, login com JWT, consulta de empresas e validação de token.

## Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT com JJWT
- Lombok
- Validation
- Springdoc OpenAPI
- JUnit 5
- Mockito
- MockMvc
- JaCoCo
- SonarQube

## Endpoints

- `POST /companies`
- `POST /auth/login`
- `GET /auth/validate`
- `GET /companies/{id}`
- `GET /companies/{id}/exists`
- `GET /companies/email/{email}/exists`

## Variáveis De Ambiente

- `SERVER_PORT`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `SPRING_JPA_SHOW_SQL`
- `SPRING_JPA_FORMAT_SQL`
- `JWT_SECRET`
- `JWT_EXPIRATION_MINUTES`
- `SONAR_HOST_URL`
- `SONAR_TOKEN`

## Rodar Localmente

```powershell
.\gradlew bootRun
```

## Testes

```powershell
.\gradlew test
```

## Cobertura

```powershell
.\gradlew jacocoTestReport
```

Relatório HTML:

```text
build/reports/jacoco/test/html/index.html
```

## SonarQube

```powershell
$env:SONAR_HOST_URL="http://localhost:9000"
$env:SONAR_TOKEN="seu_token_aqui"
.\gradlew test jacocoTestReport sonar
```

## Swagger

```text
http://localhost:8081/swagger-ui/index.html
```

## Docker

```powershell
docker build -t faclovers-auth-service .
docker run --env-file ..\.env -p 8081:8081 faclovers-auth-service
```
