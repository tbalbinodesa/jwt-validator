# Guia de Início Rápido - JWT Validator API

## Visão Geral

A JWT Validator API é uma aplicação Spring Boot que valida JSON Web Tokens (JWTs) com base em regras de negócio
específicas.

## Pré-requisitos

- **Java 21** ou superior
- **Maven 3.8** ou superior
- **Docker** (opcional, para execução em container)

## Instalação e Execução

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/jwt-validator.git
cd jwt-validator
```

### 2. Execução Local

#### Usando Maven

```bash
mvn spring-boot:run
```

#### Usando Docker Compose (Recomendado)

```bash
# Produção
docker-compose up --build

# Desenvolvimento (com hot reload)
docker-compose --profile dev up
```

#### Usando Docker diretamente

```bash
docker build -t jwt-validator .
docker run -p 8080:8080 jwt-validator
```

### 3. Verificação

A aplicação estará disponível em: `http://localhost:8080`

**Teste rápido:**

```bash
curl -X POST http://localhost:8080/api/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{"jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.B9QzPb_jzWlOvXLFiOzrKbF8yVHhxB7wTVHjF8yVHhx"}'
```

## Regras de Validação

Um JWT é considerado válido quando atende **todas** as seguintes regras:

1. ✅ **Estrutura válida**: Deve ser um JWT bem formado com assinatura válida
2. ✅ **Exatamente 3 claims**: Deve conter apenas `Name`, `Role` e `Seed`
3. ✅ **Name sem números**: A claim `Name` não pode conter caracteres numéricos
4. ✅ **Name com tamanho máximo**: Máximo de 256 caracteres
5. ✅ **Role válida**: Deve ser `Admin`, `Member` ou `External`
6. ✅ **Seed primo**: A claim `Seed` deve ser um número primo

## Configuração

### Variáveis de Ambiente

```bash
# JWT Secret (obrigatório em produção)
JWT_SECRET=seu-jwt-secret-super-seguro

# Porta da aplicação (opcional, padrão: 8080)
SERVER_PORT=8080
```

### Arquivo application.properties

```properties
spring.application.name=jwt-validator
jwt.secret=${JWT_SECRET:3Vq#mP9$kL2@nR5*jF8&hX4^wC7!tY6zB1}
server.port=${SERVER_PORT:8080}
```

## Próximos Passos

- 📖 **[Documentação da API](API_DOCUMENTATION.md)** - Endpoints, exemplos e testes
- 🛠️ **[Guia de Desenvolvimento](DEVELOPMENT.md)** - Setup para desenvolvimento
- 🚀 **[Guia de Deploy](DEPLOYMENT.md)** - CI/CD e deployment
- ☁️ **[Infraestrutura AWS](INFRASTRUCTURE.md)** - Setup na AWS com Terraform

## Suporte

- **Documentação Interativa**: `http://localhost:8080/swagger-ui.html`
- **Especificação OpenAPI**: `http://localhost:8080/v3/api-docs`
- **Health Check**: `http://localhost:8080/actuator/health`
