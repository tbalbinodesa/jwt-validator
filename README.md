# JWT Validator API

[![CI/CD Pipeline](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml)
[![Security Scan](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml/badge.svg?event=schedule)](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml)

> **API Spring Boot profissional para validação de JSON Web Tokens (JWTs) com regras de negócio customizadas**

## 🎯 Visão Geral

A JWT Validator API é uma aplicação Spring Boot robusta e escalável que valida JWTs com base em regras específicas de
negócio. Oferece endpoints REST seguros para validação e extração de claims, com documentação OpenAPI completa e
pipeline CI/CD automatizado.

### ✨ Características Principais

- 🔒 **Validação Robusta**: Regras de negócio customizadas para JWTs
- 🚀 **Alta Performance**: Algoritmos otimizados para validação de números primos
- 📊 **Documentação Completa**: OpenAPI 3.0 com Swagger UI interativo
- 🐳 **Containerizado**: Docker e Docker Compose prontos para produção
- ☁️ **Cloud Ready**: Infraestrutura AWS com Terraform
- 🔄 **CI/CD Automatizado**: Pipeline completo com GitHub Actions
- 🧪 **Testes Abrangentes**: Cobertura completa com JUnit 5

### 📋 Regras de Validação

Um JWT é considerado **válido** quando atende a **todas** as seguintes regras:

- ✅ **Estrutura válida**: JWT bem formado com assinatura válida
- ✅ **Exatamente 3 claims**: Apenas `Name`, `Role` e `Seed`
- ✅ **Name sem números**: Não pode conter caracteres numéricos (máx. 256 chars)
- ✅ **Role válida**: Deve ser `Admin`, `Member` ou `External`
- ✅ **Seed primo**: Deve ser um número primo

## 🚀 Início Rápido

### Pré-requisitos

- **Docker** e **Docker Compose** (recomendado)
- **Java 21** e **Maven 3.8+** (para desenvolvimento)

### Execução com Docker (Recomendado)

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/jwt-validator.git
cd jwt-validator

# Execute com Docker Compose
docker-compose up --build

# Teste a API
curl -X POST http://localhost:8080/api/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{"jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.B9QzPb_jzWlOvXLFiOzrKbF8yVHhxB7wTVHjF8yVHhx"}'
```

### Execução Local

```bash
# Compile e execute
mvn spring-boot:run

# Ou usando Maven Wrapper
./mvnw spring-boot:run
```

**🌐 Aplicação**: `http://localhost:8080`  
**📖 Documentação**: `http://localhost:8080/swagger-ui.html`  
**🔍 Health Check**: `http://localhost:8080/actuator/health`

## 📚 Documentação

### 📖 Guias de Usuário

| Documento                                                 | Descrição                                      | Público-Alvo                  |
|-----------------------------------------------------------|------------------------------------------------|-------------------------------|
| **[📖 API Reference](docs/API_DOCUMENTATION.md)**         | Endpoints, exemplos e especificações completas | Desenvolvedores, Integradores |
| **[🧪 Testes com Insomnia](docs/INSOMNIA_COLLECTION.md)** | Coleção pré-configurada para testes            | QA, Desenvolvedores           |

### 🛠️ Guias Técnicos

| Documento                                             | Descrição                                  | Público-Alvo       |
|-------------------------------------------------------|--------------------------------------------|--------------------|
| **[🚀 Setup e Desenvolvimento](docs/DEVELOPMENT.md)** | Ambiente de desenvolvimento e contribuição | Desenvolvedores    |
| **[🐳 Deploy e CI/CD](docs/DEPLOYMENT.md)**           | Deployment local, staging e produção       | DevOps, SRE        |
| **[☁️ Infraestrutura AWS](docs/INFRASTRUCTURE.md)**   | Terraform, arquitetura e custos            | DevOps, Arquitetos |

### 🔗 Links Úteis

- **[📊 Swagger UI](http://localhost:8080/swagger-ui.html)** - Documentação interativa da API
- **[🔍 Health Check](http://localhost:8080/actuator/health)** - Status e saúde da aplicação
- **[📋 OpenAPI Spec](http://localhost:8080/v3/api-docs)** - Especificação OpenAPI em JSON

## 🏗️ Arquitetura e Tecnologias

### 🛠️ Stack Tecnológica

| Categoria           | Tecnologias                     |
|---------------------|---------------------------------|
| **Backend**         | Spring Boot 3.x, Java 21        |
| **Containerização** | Docker, Docker Compose          |
| **CI/CD**           | GitHub Actions                  |
| **Cloud**           | AWS (ECS Fargate, ECR, ALB)     |
| **IaC**             | Terraform                       |
| **Testes**          | JUnit 5, Testcontainers, Jacoco |
| **Documentação**    | OpenAPI 3.0, Swagger UI         |

### 📁 Estrutura do Projeto

```
jwt-validator/
├── src/
│   ├── main/java/com/instrospect/jwt_validator/
│   │   ├── controller/          # 🎮 Controladores REST
│   │   ├── service/             # 🧠 Lógica de negócio
│   │   ├── dto/                 # 📦 Objetos de transferência
│   │   ├── config/              # ⚙️ Configurações
│   │   └── util/                # 🔧 Utilitários
│   └── test/                    # 🧪 Testes unitários e integração
├── docs/                        # 📚 Documentação técnica
├── terraform/                   # ☁️ Infraestrutura como código
├── .github/workflows/           # 🔄 Pipelines CI/CD
└── docker-compose.yml           # 🐳 Orquestração local
```

## 🚀 API Endpoints

### Principais Endpoints

| Método | Endpoint                  | Descrição                             |
|--------|---------------------------|---------------------------------------|
| `POST` | `/api/jwt/validate`       | Valida JWT conforme regras de negócio |
| `POST` | `/api/jwt/extract-claims` | Extrai claims de um JWT               |
| `GET`  | `/actuator/health`        | Health check da aplicação             |
| `GET`  | `/swagger-ui.html`        | Documentação interativa               |

### Exemplo de Uso

```bash
# Validar JWT
curl -X POST http://localhost:8080/api/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{"jwt": "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNyIsIk5hbWUiOiJKb2huRG9lIn0.signature"}'

# Resposta
{"isValid": true}
```

## 🚀 Deployment e CI/CD

### 🔄 Pipeline Automatizado

O projeto implementa um pipeline CI/CD completo com **GitHub Actions**:

1. **🧪 Test & Build** - Testes unitários/integração + build JAR
2. **🔒 Security Scan** - OWASP Dependency Check
3. **🐳 Docker Build** - Build e push para GitHub Container Registry
4. **🎭 Deploy Staging** - Deploy automático (branch `develop`)
5. **🏭 Deploy Production** - Deploy automático (branch `main`)

### 🌍 Opções de Deployment

| Ambiente          | Comando                         | Documentação                             |
|-------------------|---------------------------------|------------------------------------------|
| **🐳 Local**      | `docker-compose up --build`     | [Deploy Guide](docs/DEPLOYMENT.md)       |
| **☁️ AWS**        | `cd terraform && ./deploy.sh`   | [Infrastructure](docs/INFRASTRUCTURE.md) |
| **🎭 Staging**    | Automático via `develop` branch | [CI/CD Guide](docs/DEPLOYMENT.md)        |
| **🏭 Production** | Automático via `main` branch    | [CI/CD Guide](docs/DEPLOYMENT.md)        |

### 💰 Estimativa de Custos AWS

- **🧪 Desenvolvimento**: ~$35/mês (recursos otimizados)
- **🎭 Staging**: ~$75/mês (ambiente de testes)
- **🏭 Produção**: ~$150/mês (alta disponibilidade)

## 🤝 Contribuindo

Contribuições são bem-vindas! Siga estas etapas:

1. **🍴 Fork** o repositório
2. **🌿 Crie** uma branch: `git checkout -b feature/nova-funcionalidade`
3. **💻 Implemente** com testes adequados
4. **🧪 Execute** os testes: `mvn test`
5. **📤 Submeta** um Pull Request

📖 **Consulte o [Guia de Desenvolvimento](docs/DEVELOPMENT.md) para detalhes completos.**

## 📄 Licença

Este projeto está licenciado sob a **MIT License**. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.

## 🆘 Suporte e Contato

- **🐛 Issues**: [GitHub Issues](https://github.com/seu-usuario/jwt-validator/issues)
- **📚 Documentação**: [docs/](docs/)
- **📊 API Docs**: [Swagger UI](http://localhost:8080/swagger-ui.html)
- **🔍 Status**: [Health Check](http://localhost:8080/actuator/health)

---

<div align="center">

**⭐ Se este projeto foi útil, considere dar uma estrela!**

</div>
