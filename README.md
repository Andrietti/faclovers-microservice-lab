# FacLovers Microservices Lab

Projeto de laboratório para construção de microsserviços com Java 21, Spring Boot, PostgreSQL, Docker e boas práticas de qualidade.

O `auth-service` é o primeiro microserviço implementado. Ele cuida de cadastro de empresas, login com JWT, consulta de empresa, validação de existência e validação de token.

## Serviços

- `auth-service`: implementado
- `production-service`: previsto na arquitetura
- `indicator-service`: previsto na arquitetura
- `postgres-auth`: banco PostgreSQL do auth-service
- `postgres-production`: banco PostgreSQL do production-service

O Docker Compose completo referencia todos os serviços da arquitetura. Enquanto `production-service` e `indicator-service` ainda não existirem com Dockerfile, suba apenas o banco e o `auth-service`.

## Variáveis De Ambiente

O projeto usa variáveis de ambiente por meio de `.env`.

O arquivo `.env` não deve ser versionado. O arquivo `.env.example` deve ir para o GitHub como modelo seguro.

Criar `.env` no Linux/macOS:

```bash
cp .env.example .env
```

Criar `.env` no Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

Depois ajuste os valores locais conforme necessário.

## Portas

- Auth Service: `8081`
- Production Service: `8082`
- Indicator Service: `8083`
- PostgreSQL Auth: `5433`
- PostgreSQL Production: `5434`
- SonarQube: `9000`

## Docker Compose

Subir tudo:

```bash
docker compose up --build
```

Subir em segundo plano:

```bash
docker compose up --build -d
```

Subir apenas o auth-service e seu banco:

```bash
docker compose up --build postgres-auth auth-service
```

Parar:

```bash
docker compose down
```

Parar apagando volumes:

```bash
docker compose down -v
```

## Auth Service Local

Linux/macOS:

```bash
cd auth-service
./gradlew bootRun
```

Windows PowerShell:

```powershell
cd auth-service
.\gradlew bootRun
```

Swagger:

```text
http://localhost:8081/swagger-ui/index.html
```

## Testes

Windows PowerShell:

```powershell
cd auth-service
.\gradlew test
```

## JaCoCo

```powershell
.\gradlew jacocoTestReport
```

Relatório HTML:

```text
auth-service/build/reports/jacoco/test/html/index.html
```

## SonarQube

Configure as variáveis:

```powershell
$env:SONAR_HOST_URL="http://localhost:9000"
$env:SONAR_TOKEN="seu_token_aqui"
```

Rodar análise:

```powershell
cd auth-service
.\gradlew test jacocoTestReport sonar
```

## Endpoints Do Auth Service

- `POST /companies`
- `POST /auth/login`
- `GET /auth/validate`
- `GET /companies/{id}`
- `GET /companies/{id}/exists`
- `GET /companies/email/{email}/exists`
