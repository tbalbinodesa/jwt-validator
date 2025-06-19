# JWT Validator API

[![CI/CD Pipeline](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml)
[![Security Scan](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml/badge.svg?event=schedule)](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml)

API construída com Spring Boot para validar JWTs (JSON Web Tokens) com base em um conjunto de regras de negócio
customizadas.

## Descrição

Esta aplicação expõe uma API web que recebe um JWT (string) por parâmetro e verifica sua validade conforme as seguintes
regras:

- Deve ser um JWT válido (estrutura e assinatura).
- Deve conter **exatamente** 3 claims: `Name`, `Role` e `Seed`.
- A claim `Name` não pode conter caracteres numéricos.
- O tamanho máximo da claim `Name` é de 256 caracteres.
- A claim `Role` deve ser um dos seguintes valores: `Admin`, `Member` ou `External`.
- A claim `Seed` deve ser um número primo.

**Input:** Um JWT (string).  
**Output:** Um booleano indicando se é válido ou não.

---

## Decisões de Design e Premissas

1. **Arquitetura em Camadas:** A aplicação foi dividida em camadas (Controller, Service, Util) para seguir os princípios
   de Single Responsibility (SOLID) e Clean Architecture. Isso promove baixo acoplamento, alta coesão e facilita os
   testes unitários e a manutenção.
2. **Segredo do JWT (`jwt.secret`):** A especificação de um JWT válido implica na verificação de sua assinatura
   criptográfica. O desafio não forneceu um segredo ou chave para essa verificação. **Foi configurado um segredo
   forte (`3Vq#mP9$kL2@nR5*jF8&hX4^wC7!tY6zB1`)** no arquivo `application.properties`. Em um ambiente de produção, este
   valor **deve** ser gerenciado de forma segura, por exemplo, através de variáveis de ambiente ou um serviço de
   gerenciamento de segredos (como AWS Secrets Manager).
3. **API Design:** O endpoint espera um método `POST` com um corpo JSON (`{"jwt": "..."}`). Isso é mais robusto e seguro
   do que passar o JWT como um parâmetro de URL, que pode ser registrado em logs e tem limitações de tamanho.
4. **Logging:** Foi adicionado logging (via SLF4J, padrão do Spring Boot) no serviço de validação. Os logs são
   informativos para o caminho feliz e registram avisos (`WARN`) ou erros (`ERROR`) específicos para cada falha de
   validação, melhorando a **Observability**.
5. **Eficiência da Verificação de Primos:** O algoritmo `isPrime` foi otimizado para não verificar todos os números até
   `n`, mas apenas até a sua raiz quadrada, utilizando uma abordagem ainda mais eficiente que verifica divisibilidade
   por 2 e 3 primeiro, depois testa apenas números da forma 6k±1, o que é significativamente mais performático para
   números grandes.
6. **Validação de Entrada:** Utiliza Bean Validation (`@NotBlank`) para garantir que o JWT não seja nulo ou vazio antes
   do processamento.
7. **Funcionalidade Adicional:** Além da validação, a API também oferece um endpoint para extrair claims do JWT,
   facilitando a depuração e análise de tokens.

---

## CI/CD Pipeline

Este projeto implementa um pipeline completo de CI/CD usando GitHub Actions, seguindo as melhores práticas de DevOps.

### Arquitetura do Pipeline

O pipeline é composto por 5 jobs principais:

1. **Test and Build** - Executa testes unitários e de integração, gera relatórios e constrói o artefato JAR
2. **Security Scan** - Executa verificação de vulnerabilidades nas dependências usando OWASP Dependency Check
3. **Build and Push Docker Image** - Constrói e publica a imagem Docker no GitHub Container Registry
4. **Deploy to Staging** - Deploy automático para ambiente de staging (branch `develop`)
5. **Deploy to Production** - Deploy automático para ambiente de produção (branch `main`)

### Fluxo de Trabalho

#### Branch Strategy

- **`main`**: Branch de produção - deploys automáticos para produção
- **`develop`**: Branch de desenvolvimento - deploys automáticos para staging
- **Feature branches**: Executam apenas testes e validações

#### Triggers

- **Push** para `main` ou `develop`: Executa pipeline completo com deploy
- **Pull Request** para `main`: Executa testes e validações (sem deploy)

### Funcionalidades do Pipeline

#### ✅ Continuous Integration (CI)

- **Testes Automatizados**: Execução de todos os testes unitários e de integração
- **Cache de Dependências**: Cache inteligente do Maven para acelerar builds
- **Relatórios de Teste**: Geração automática de relatórios JUnit
- **Build de Artefatos**: Construção e upload do JAR como artefato
- **Verificação de Segurança**: Scan de vulnerabilidades nas dependências

#### 🚀 Continuous Deployment (CD)

- **Containerização**: Build automático de imagens Docker
- **Registry**: Publicação no GitHub Container Registry (ghcr.io)
- **Versionamento**: Tags automáticas baseadas em branch e commit SHA
- **Ambientes Protegidos**: Uso de GitHub Environments para controle de deploy
- **Smoke Tests**: Testes básicos pós-deploy para validação

#### 🔒 Segurança e Qualidade

- **Dependency Updates**: Dependabot configurado para atualizações automáticas
- **Security Scanning**: OWASP Dependency Check integrado
- **Least Privilege**: Permissões mínimas necessárias para cada job
- **Secrets Management**: Uso seguro de secrets do GitHub

### Configuração dos Ambientes

Para configurar os ambientes de staging e produção:

1. **GitHub Environments**: Configure os environments `staging` e `production` no repositório
2. **Protection Rules**: Adicione regras de proteção (aprovações, reviewers)
3. **Secrets**: Configure secrets específicos por ambiente se necessário
4. **Deploy Scripts**: Customize os comandos de deploy nos jobs correspondentes

### Monitoramento e Observabilidade

- **Artifacts**: JAR e relatórios de segurança são salvos como artifacts
- **Logs Estruturados**: Logs detalhados de cada etapa do pipeline
- **Status Badges**: Adicione badges do GitHub Actions ao README
- **Notifications**: Configure notificações para falhas ou sucessos

### Dependabot

O projeto inclui configuração do Dependabot para:

- **Maven Dependencies**: Atualizações semanais das dependências Java
- **GitHub Actions**: Atualizações das actions utilizadas no pipeline
- **Docker**: Atualizações das imagens base do Dockerfile

---

## Como Executar o Projeto

### Pré-requisitos

- Java 21 ou superior
- Maven 3.8 ou superior

### Executando Localmente

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/jwt-validator.git
   cd jwt-validator
   ```

2. Compile e execute o projeto com o Maven:
   ```bash
   mvn spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`.

### Executando com Docker

#### Usando Docker Compose (Recomendado)

1. **Produção**:
   ```bash
   docker-compose up --build
   ```

2. **Desenvolvimento** (com hot reload):
   ```bash
   docker-compose --profile dev up
   ```

#### Usando Docker diretamente

1. Construa a imagem Docker:
   ```bash
   docker build -t jwt-validator .
   ```

2. Execute o container:
   ```bash
   docker run -p 8080:8080 jwt-validator
   ```

#### Usando a imagem do GitHub Container Registry

```bash
docker run -p 8080:8080 ghcr.io/seu-usuario/jwt-validator:latest
```

---

## Documentação da API

### Documentação OpenAPI/Swagger

A API possui documentação interativa gerada automaticamente usando OpenAPI 3.0 (Swagger). Após iniciar a aplicação, você
pode acessar:

#### Swagger UI (Interface Interativa)

- **URL**: `http://localhost:8080/swagger-ui.html`
- **Descrição**: Interface web interativa onde você pode visualizar todos os endpoints, seus parâmetros, exemplos de
  requisições e respostas, e até mesmo testar a API diretamente no navegador.

#### Especificação OpenAPI (JSON)

- **URL**: `http://localhost:8080/v3/api-docs`
- **Descrição**: Especificação completa da API em formato JSON, seguindo o padrão OpenAPI 3.0. Útil para integração com
  ferramentas de desenvolvimento e geração automática de clientes.

#### Como usar o Swagger UI

1. Acesse `http://localhost:8080/swagger-ui.html` no seu navegador
2. Explore os endpoints disponíveis na interface
3. Clique em um endpoint para ver detalhes (parâmetros, exemplos, etc.)
4. Use o botão "Try it out" para testar os endpoints diretamente
5. Preencha os parâmetros necessários e clique em "Execute"

### Endpoints da API

### `POST /api/jwt/validate`

Valida um JWT fornecido.

#### Request Body

```json
{
  "jwt": "string"
}
```

- `jwt` (string, obrigatório): O JSON Web Token a ser validado.

#### Response (Success, 200 OK)

```json
{
  "isValid": true
}
```

- `isValid`: `true` se o JWT atende a todas as regras; `false` caso contrário.

#### Exemplo de Uso com cURL

```bash
curl -X POST http://localhost:8080/api/jwt/validate \
-H "Content-Type: application/json" \
-d '{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.B9QzPb_jzWlOvXLFiOzrKbF8yVHhxB7wTVHjF8yVHhx"
}'
```

### `POST /api/jwt/extract-claims`

Extrai as claims de um JWT fornecido.

#### Request Body

```json
{
  "jwt": "string"
}
```

- `jwt` (string, obrigatório): O JSON Web Token do qual extrair as claims.

#### Response (Success, 200 OK)

```json
{
  "Name": "JohnDoe",
  "Role": "Admin",
  "Seed": "7"
}
```

Ou em caso de erro:

```json
{
  "error": "Token JWT inválido"
}
```

#### Exemplo de Uso com cURL

```bash
curl -X POST http://localhost:8080/api/jwt/extract-claims \
-H "Content-Type: application/json" \
-d '{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.B9QzPb_jzWlOvXLFiOzrKbF8yVHhxB7wTVHjF8yVHhx"
}'
```

---

## Como Executar os Testes

Para garantir a qualidade e a corretude do código, execute a suíte de testes (unidade e integração):

```bash
mvn test
```

Os testes cobrem:

- Validação de JWTs válidos e inválidos
- Verificação de todas as regras de negócio
- Testes de integração dos endpoints
- Validação da funcionalidade de extração de claims
- Testes unitários do utilitário de números primos

---

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/instrospect/jwt_validator/
│   │   ├── controller/          # Controladores REST
│   │   ├── dto/                 # Objetos de transferência de dados
│   │   ├── service/             # Lógica de negócio
│   │   ├── util/                # Utilitários
│   │   └── JwtValidatorApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/instrospect/jwt_validator/
        ├── controller/          # Testes de integração
        ├── service/             # Testes unitários dos serviços
        └── util/                # Testes unitários dos utilitários
```

---

## Configuração

A aplicação pode ser configurada através do arquivo `application.properties`:

```properties
spring.application.name=jwt-validator
jwt.secret=3Vq#mP9$kL2@nR5*jF8&hX4^wC7!tY6zB1
```

**Importante:** Em ambiente de produção, configure o `jwt.secret` através de variáveis de ambiente para maior segurança.
