# Documentação da API - JWT Validator

## Visão Geral

A JWT Validator API expõe endpoints REST para validação e extração de informações de JSON Web Tokens (JWTs).

## Base URL

- **Local**: `http://localhost:8080`
- **Produção**: `https://your-production-domain.com`

## Autenticação

Esta API não requer autenticação para uso dos endpoints de validação.

## Endpoints

### 1. Validar JWT

Valida se um JWT atende a todas as regras de negócio definidas.

#### `POST /api/jwt/validate`

**Request Body:**

```json
{
  "jwt": "string"
}
```

**Parâmetros:**

- `jwt` (string, obrigatório): O JSON Web Token a ser validado

**Response (200 OK):**

```json
{
  "isValid": true
}
```

**Campos de Resposta:**

- `isValid` (boolean): `true` se o JWT é válido, `false` caso contrário

#### Exemplo com cURL

```bash
curl -X POST http://localhost:8080/api/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.B9QzPb_jzWlOvXLFiOzrKbF8yVHhxB7wTVHjF8yVHhx"
  }'
```

### 2. Extrair Claims do JWT

Extrai todas as claims de um JWT fornecido.

#### `POST /api/jwt/extract-claims`

**Request Body:**

```json
{
  "jwt": "string"
}
```

**Parâmetros:**

- `jwt` (string, obrigatório): O JSON Web Token do qual extrair as claims

**Response (200 OK) - Sucesso:**

```json
{
  "Name": "JohnDoe",
  "Role": "Admin",
  "Seed": "7"
}
```

**Response (200 OK) - Erro:**

```json
{
  "error": "Token JWT inválido"
}
```

#### Exemplo com cURL

```bash
curl -X POST http://localhost:8080/api/jwt/extract-claims \
  -H "Content-Type: application/json" \
  -d '{
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.B9QzPb_jzWlOvXLFiOzrKbF8yVHhxB7wTVHjF8yVHhx"
  }'
```

## Regras de Validação

Um JWT é considerado **válido** quando atende a **todas** as seguintes regras:

### 1. Estrutura e Assinatura Válidas

- Deve ser um JWT bem formado (header.payload.signature)
- A assinatura deve ser válida usando o secret configurado

### 2. Exatamente 3 Claims

- Deve conter **apenas** as claims: `Name`, `Role` e `Seed`
- Não pode ter claims adicionais ou ausentes

### 3. Validação da Claim `Name`

- ❌ **Não pode conter números**: Apenas letras e caracteres especiais
- ✅ **Tamanho máximo**: 256 caracteres
- ✅ **Não pode ser vazia**

### 4. Validação da Claim `Role`

- Deve ser **exatamente** um dos valores:
    - `Admin`
    - `Member`
    - `External`

### 5. Validação da Claim `Seed`

- Deve ser um **número primo**
- Aceita valores numéricos como string ou number

## Exemplos de Tokens

### Token Válido

```
eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.B9QzPb_jzWlOvXLFiOzrKbF8yVHhxB7wTVHjF8yVHhx
```

**Payload decodificado:**

```json
{
  "Role": "Admin",
  "Seed": "7",
  "Name": "JohnDoe"
}
```

### Tokens Inválidos

#### 1. Claim Name com números

❌ **Problema**: Name contém números

```json
{
  "Role": "Admin",
  "Seed": "7",
  "Name": "John123"
}
```

#### 2. Role inválida

❌ **Problema**: Role não é Admin, Member ou External

```json
{
  "Role": "SuperAdmin",
  "Seed": "7",
  "Name": "JohnDoe"
}
```

#### 3. Seed não primo

❌ **Problema**: 8 não é um número primo

```json
{
  "Role": "Admin",
  "Seed": "8",
  "Name": "JohnDoe"
}
```

#### 4. Claims extras

❌ **Problema**: Claim extra não permitida

```json
{
  "Role": "Admin",
  "Seed": "7",
  "Name": "JohnDoe",
  "exp": 1640995200
}
```

## Códigos de Status HTTP

| Código | Descrição             | Quando Ocorre                                        |
|--------|-----------------------|------------------------------------------------------|
| 200    | OK                    | Requisição processada com sucesso                    |
| 400    | Bad Request           | JWT ausente, vazio ou formato de requisição inválido |
| 500    | Internal Server Error | Erro interno do servidor                             |

## Documentação Interativa

### Swagger UI

- **URL**: `http://localhost:8080/swagger-ui.html`
- **Descrição**: Interface web interativa para testar a API
- **Funcionalidades**:
    - Visualizar todos os endpoints
    - Testar requisições diretamente no navegador
    - Ver exemplos de request/response
    - Baixar especificação OpenAPI

### Especificação OpenAPI

- **URL**: `http://localhost:8080/v3/api-docs`
- **Formato**: JSON seguindo OpenAPI 3.0
- **Uso**: Integração com ferramentas de desenvolvimento

## Testando a API

### 1. Usando Swagger UI

1. Acesse `http://localhost:8080/swagger-ui.html`
2. Clique no endpoint desejado
3. Clique em "Try it out"
4. Preencha os parâmetros
5. Clique em "Execute"

### 2. Usando Insomnia

Consulte a **[Documentação da Coleção Insomnia](INSOMNIA_COLLECTION.md)** para:

- Importar coleção pré-configurada
- Casos de teste prontos
- Variáveis de ambiente
- Exemplos avançados

### 3. Usando cURL

Veja os exemplos de cURL acima para cada endpoint.

### 4. Health Check

```bash
curl http://localhost:8080/actuator/health
```

## Monitoramento e Logs

### Logs da Aplicação

A aplicação registra logs detalhados para cada validação:

```
INFO  - JWT validation successful for token: eyJ...
WARN  - JWT validation failed: Name contains numeric characters
ERROR - JWT validation failed: Invalid signature
```

### Métricas (Actuator)

- **Health**: `/actuator/health`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

## Limitações e Considerações

### Performance

- Validação de números primos otimizada para números grandes
- Cache interno para melhor performance
- Timeout configurável para operações

### Segurança

- JWT secret deve ser configurado adequadamente em produção
- Logs não expõem tokens completos por segurança
- Rate limiting recomendado para produção

### Compatibilidade

- Suporta apenas algoritmo HS256
- Claims devem ser strings (exceto Seed que aceita number)
- Encoding UTF-8 obrigatório
