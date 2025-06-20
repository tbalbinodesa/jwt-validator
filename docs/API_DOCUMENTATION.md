# API Reference - JWT Validator

## 📋 Visão Geral

Esta documentação fornece especificações completas dos endpoints REST da JWT Validator API, incluindo exemplos de
requisições, respostas e códigos de erro.

## 🌐 Base URLs

| Ambiente     | URL                               | Descrição             |
|--------------|-----------------------------------|-----------------------|
| **Local**    | `http://localhost:8080`           | Desenvolvimento local |
| **Staging**  | `https://staging.your-domain.com` | Ambiente de testes    |
| **Produção** | `https://api.your-domain.com`     | Ambiente de produção  |

## 🔓 Autenticação

Esta API **não requer autenticação** para os endpoints de validação. Todos os endpoints são públicos e podem ser
acessados diretamente.

## 🚀 API Endpoints

### 1. 🔍 Validar JWT

Valida se um JWT atende a todas as regras de negócio definidas.

#### `POST /api/jwt/validate`

**📥 Request**

| Campo | Tipo   | Obrigatório | Descrição                     |
|-------|--------|-------------|-------------------------------|
| `jwt` | string | ✅           | JSON Web Token a ser validado |

```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.signature"
}
```

**📤 Response**

| Status | Descrição        | Response Body          |
|--------|------------------|------------------------|
| `200`  | Sucesso          | `{"isValid": boolean}` |
| `400`  | Request inválido | `{"error": "string"}`  |
| `500`  | Erro interno     | `{"error": "string"}`  |

**✅ Exemplo de Sucesso:**
```json
{
  "isValid": true
}
```

**❌ Exemplo de Erro:**

```json
{
  "isValid": false
}
```

**🔧 Exemplo com cURL:**
```bash
curl -X POST http://localhost:8080/api/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{"jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.signature"}'
```

---

### 2. 📋 Extrair Claims do JWT

Extrai todas as claims de um JWT fornecido.

#### `POST /api/jwt/extract-claims`

**📥 Request**

| Campo | Tipo   | Obrigatório | Descrição                                |
|-------|--------|-------------|------------------------------------------|
| `jwt` | string | ✅           | JSON Web Token do qual extrair as claims |

```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.signature"
}
```

**📤 Response**

| Status | Descrição        | Response Body            |
|--------|------------------|--------------------------|
| `200`  | Sucesso          | Claims extraídas ou erro |
| `400`  | Request inválido | `{"error": "string"}`    |
| `500`  | Erro interno     | `{"error": "string"}`    |

**✅ Exemplo de Sucesso:**
```json
{
  "Name": "JohnDoe",
  "Role": "Admin",
  "Seed": "7"
}
```

**❌ Exemplo de Erro:**
```json
{
  "error": "Token JWT inválido"
}
```

**🔧 Exemplo com cURL:**
```bash
curl -X POST http://localhost:8080/api/jwt/extract-claims \
  -H "Content-Type: application/json" \
  -d '{"jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.signature"}'
```

---

### 3. 🔍 Health Check

Verifica o status e saúde da aplicação.

#### `GET /actuator/health`

**📤 Response**

```json
{
  "status": "UP"
}
```

**🔧 Exemplo com cURL:**

```bash
curl -X GET http://localhost:8080/actuator/health
```

---

### 4. 📖 Documentação OpenAPI

Acessa a especificação OpenAPI da API.

#### `GET /v3/api-docs`

**📤 Response:** Especificação OpenAPI em formato JSON

**🔧 Exemplo com cURL:**

```bash
curl -X GET http://localhost:8080/v3/api-docs
```

## 📋 Regras de Validação

Para informações detalhadas sobre as regras de validação de JWT, consulte a seção **"📋 Regras de Validação"**
no [README.md](../README.md#-regras-de-validação).

### Resumo das Validações

- ✅ **Estrutura válida**: JWT bem formado com assinatura válida
- ✅ **Exatamente 3 claims**: Apenas `Name`, `Role` e `Seed`
- ✅ **Name sem números**: Não pode conter caracteres numéricos (máx. 256 chars)
- ✅ **Role válida**: Deve ser `Admin`, `Member` ou `External`
- ✅ **Seed primo**: Deve ser um número primo

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

## 📊 Códigos de Status HTTP

| Código | Status                   | Descrição                         | Quando Ocorre                    |
|--------|--------------------------|-----------------------------------|----------------------------------|
| `200`  | ✅ OK                     | Requisição processada com sucesso | Validação ou extração realizada  |
| `400`  | ❌ Bad Request            | Dados de entrada inválidos        | JWT ausente, vazio ou malformado |
| `500`  | ⚠️ Internal Server Error | Erro interno do servidor          | Falha na aplicação               |

## 🧪 Testando a API

### 🌐 Swagger UI (Recomendado)

Acesse a documentação interativa em: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

**Funcionalidades:**

- ✅ Visualizar todos os endpoints
- ✅ Testar requisições diretamente no navegador
- ✅ Ver exemplos de request/response
- ✅ Baixar especificação OpenAPI

### 🧪 Coleção Insomnia

Para testes avançados, consulte: **[Testes com Insomnia](INSOMNIA_COLLECTION.md)**

**Inclui:**

- ✅ Coleção pré-configurada
- ✅ Casos de teste prontos
- ✅ Variáveis de ambiente
- ✅ Cenários de erro

### 🔧 Linha de Comando (cURL)

Consulte os exemplos de cURL em cada endpoint acima.

### 🔍 Health Check Rápido

```bash
curl http://localhost:8080/actuator/health
# Resposta esperada: {"status":"UP"}
```

## 📈 Monitoramento

### 📋 Endpoints de Monitoramento

| Endpoint            | Descrição                | Uso           |
|---------------------|--------------------------|---------------|
| `/actuator/health`  | Status da aplicação      | Health checks |
| `/actuator/info`    | Informações da aplicação | Metadados     |
| `/actuator/metrics` | Métricas da aplicação    | Monitoramento |

### 📝 Logs da Aplicação

A aplicação registra logs estruturados para cada operação:

```
[INFO ] JWT validation successful for token: eyJ...
[WARN ] JWT validation failed: Name contains numeric characters
[ERROR] JWT validation failed: Invalid signature
```

## ⚠️ Limitações e Considerações

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
