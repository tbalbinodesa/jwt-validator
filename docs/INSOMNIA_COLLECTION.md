# Documentação da Coleção Insomnia - JWT Validator API

Esta documentação fornece informações detalhadas sobre como usar a coleção do Insomnia para testar a API JWT Validator.

## Visão Geral

A coleção `insomnia-collection.json` contém uma documentação completa da API em formato de requisições do Insomnia,
incluindo:

- Todos os endpoints da API
- Casos de teste para cenários válidos e inválidos
- Variáveis de ambiente pré-configuradas
- Múltiplos ambientes (desenvolvimento, produção)

## Importação da Coleção

### Passo a Passo

1. **Baixe o Insomnia**: Se ainda não tiver, baixe em [insomnia.rest](https://insomnia.rest/)

2. **Abra o Insomnia** e vá para a tela principal

3. **Importe a coleção**:
    - Clique em `Application` → `Preferences` → `Data` → `Import Data`
    - Ou use o atalho `Ctrl+Shift+I` (Windows/Linux) ou `Cmd+Shift+I` (Mac)

4. **Selecione o arquivo**:
    - Navegue até a pasta do projeto
    - Selecione o arquivo `insomnia-collection.json`
    - Clique em "Import"

5. **Verifique a importação**:
    - A coleção "JWT Validator" deve aparecer na sua lista de workspaces
    - Você verá uma pasta "JWT Validator API" com todas as requisições

## Estrutura da Coleção

### Endpoints Principais

#### 1. Validar JWT

- **Método**: POST
- **URL**: `{{base_url}}/api/jwt/validate`
- **Descrição**: Valida se um token JWT é válido
- **Body**:
  ```json
  {
    "jwt": "{{sample_jwt}}"
  }
  ```
- **Resposta Esperada**:
  ```json
  {
    "isValid": true
  }
  ```

#### 2. Extrair Claims do JWT

- **Método**: POST
- **URL**: `{{base_url}}/api/jwt/extract-claims`
- **Descrição**: Extrai todas as claims de um token JWT
- **Body**:
  ```json
  {
    "jwt": "{{sample_jwt}}"
  }
  ```
- **Resposta Esperada**: JSON com as claims extraídas

### Casos de Teste

#### 1. Validar JWT Inválido

- Testa o comportamento da API com um token malformado
- Útil para verificar tratamento de erros

#### 2. Validar JWT Vazio

- Testa a validação de entrada com string vazia
- Verifica se a API retorna erro apropriado

#### 3. Extrair Claims JWT Inválido

- Testa extração de claims com token inválido
- Verifica tratamento de erro na extração

### Endpoints de Documentação

#### 1. OpenAPI Specification

- **Método**: GET
- **URL**: `{{base_url}}/v3/api-docs`
- **Descrição**: Retorna a especificação OpenAPI em JSON

#### 2. Swagger UI

- **Método**: GET
- **URL**: `{{base_url}}/swagger-ui.html`
- **Descrição**: Acessa a interface interativa do Swagger

## Ambientes

### Base Environment (Padrão)

```json
{
  "base_url": "http://localhost:8080",
  "sample_jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.B9QzPb_jzWlOvXLFiOzrKbF8yVHhxB7wTVHjF8yVHhx",
  "valid_jwt_example": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjE2NDEwODE2MDB9.signature",
  "invalid_jwt": "invalid.jwt.token",
  "empty_jwt": ""
}
```

### Development Environment

- Herda do Base Environment
- `base_url`: `http://localhost:8080`
- Cor: Verde (#5cb85c)

### Production Environment

- Herda do Base Environment
- `base_url`: `https://your-production-domain.com`
- Cor: Vermelho (#d9534f)
- **Nota**: Atualize a URL para seu domínio de produção

## Como Usar

### 1. Configuração Inicial

1. **Selecione o ambiente**: Escolha entre Development, Production ou Base Environment
2. **Verifique as variáveis**: Certifique-se de que `base_url` está correto
3. **Inicie a aplicação**: Execute `./mvnw spring-boot:run` ou `docker-compose up`

### 2. Testando os Endpoints

#### Teste Básico de Validação

1. Abra a requisição "Validar JWT"
2. Clique em "Send"
3. Verifique se retorna `{"isValid": true}`

#### Teste de Extração de Claims

1. Abra a requisição "Extrair Claims do JWT"
2. Clique em "Send"
3. Verifique se retorna as claims do token

#### Testes de Erro

1. Execute "Validar JWT Inválido" para testar tratamento de erro
2. Execute "Validar JWT Vazio" para testar validação de entrada

### 3. Personalizando Tokens

Para testar com seus próprios tokens JWT:

1. **Edite as variáveis de ambiente**:
    - Clique no ícone de engrenagem ao lado do ambiente
    - Modifique `sample_jwt` com seu token
    - Adicione novos tokens se necessário

2. **Use tokens diretamente**:
    - Edite o body da requisição
    - Substitua `{{sample_jwt}}` pelo seu token

## Exemplos de Uso

### Exemplo 1: Validação com Token Personalizado

```json
{
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
}
```

### Exemplo 2: Testando Diferentes Cenários

1. **Token válido**: Use `sample_jwt` da variável de ambiente
2. **Token expirado**: Crie um token com `exp` no passado
3. **Token malformado**: Use `invalid_jwt` da variável de ambiente
4. **Token vazio**: Use `empty_jwt` da variável de ambiente

## Troubleshooting

### Problemas Comuns

#### 1. Erro de Conexão

- **Problema**: "Could not connect to server"
- **Solução**: Verifique se a aplicação está rodando em `localhost:8080`

#### 2. Variáveis Não Resolvidas

- **Problema**: `{{base_url}}` aparece literalmente na URL
- **Solução**: Certifique-se de que um ambiente está selecionado

#### 3. Token Inválido

- **Problema**: Sempre retorna `{"isValid": false}`
- **Solução**: Verifique se o token está no formato correto e não expirado

### Logs de Debug

Para debug adicional, verifique os logs da aplicação:

```bash
# Se rodando com Maven
./mvnw spring-boot:run

# Se rodando com Docker
docker-compose logs -f jwt-validator
```

## Contribuindo

Para adicionar novos endpoints ou casos de teste à coleção:

1. Faça as alterações no Insomnia
2. Exporte a coleção atualizada
3. Substitua o arquivo `insomnia-collection.json`
4. Atualize esta documentação se necessário

## Suporte

Para dúvidas ou problemas:

1. Verifique a documentação da API no Swagger UI
2. Consulte os logs da aplicação
3. Revise os casos de teste incluídos na coleção
