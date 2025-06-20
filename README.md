# JWT Validator API

[![CI/CD Pipeline](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml)
[![Security Scan](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml/badge.svg?event=schedule)](https://github.com/tbalbinodesa/jwt-validator/actions/workflows/ci-cd.yml)

> **API Spring Boot para validação de JSON Web Tokens (JWTs) com regras de negócio customizadas**

## 🎯 Visão Geral

A JWT Validator API é uma aplicação robusta que valida JWTs com base em regras específicas de negócio, oferecendo
endpoints REST para validação e extração de claims.

### Regras de Validação

Um JWT é considerado **válido** quando atende a **todas** as seguintes regras:

- ✅ **Estrutura válida**: JWT bem formado com assinatura válida
- ✅ **Exatamente 3 claims**: Apenas `Name`, `Role` e `Seed`
- ✅ **Name sem números**: Não pode conter caracteres numéricos (máx. 256 chars)
- ✅ **Role válida**: Deve ser `Admin`, `Member` ou `External`
- ✅ **Seed primo**: Deve ser um número primo

## 🚀 Início Rápido

```bash
# Clone e execute
git clone https://github.com/seu-usuario/jwt-validator.git
cd jwt-validator
docker-compose up --build

# Teste a API
curl -X POST http://localhost:8080/api/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{"jwt": "seu-jwt-aqui"}'
```

**Aplicação disponível em**: `http://localhost:8080`  
**Documentação interativa**: `http://localhost:8080/swagger-ui.html`

## 📚 Documentação

### Guias Principais

| Documento                                       | Descrição                                 |
|-------------------------------------------------|-------------------------------------------|
| **[🚀 Início Rápido](docs/GETTING_STARTED.md)** | Instalação, execução e primeiros passos   |
| **[📖 API](docs/API_DOCUMENTATION.md)**         | Endpoints, exemplos e regras de validação |
| **[🛠️ Desenvolvimento](docs/DEVELOPMENT.md)**  | Setup de dev, testes e contribuição       |
| **[🚀 Deploy](docs/DEPLOYMENT.md)**             | CI/CD, Docker e deployment em cloud       |
| **[☁️ Infraestrutura](docs/INFRASTRUCTURE.md)** | AWS, Terraform e arquitetura              |

### Recursos Adicionais

- **[🧪 Coleção Insomnia](docs/INSOMNIA_COLLECTION.md)** - Testes de API pré-configurados
- **[📊 Swagger UI](http://localhost:8080/swagger-ui.html)** - Documentação interativa
- **[🔍 Health Check](http://localhost:8080/actuator/health)** - Status da aplicação

## 🏗️ Arquitetura

### Stack Tecnológica

- **Backend**: Spring Boot 3.x, Java 21
- **Containerização**: Docker, Docker Compose
- **CI/CD**: GitHub Actions
- **Cloud**: AWS (ECS Fargate, ECR, ALB)
- **IaC**: Terraform
- **Testes**: JUnit 5, Testcontainers
- **Documentação**: OpenAPI 3.0, Swagger UI

### Estrutura do Projeto

```
├── src/main/java/com/instrospect/jwt_validator/
│   ├── controller/     # Controladores REST
│   ├── service/        # Lógica de negócio
│   ├── dto/           # Objetos de transferência
│   └── util/          # Utilitários
├── docs/              # Documentação organizada
├── terraform/         # Infraestrutura como código
└── .github/workflows/ # CI/CD pipelines
```

## 🔄 CI/CD Pipeline

Pipeline automatizado com **GitHub Actions**:

1. **🧪 Test & Build** - Testes unitários/integração + build JAR
2. **🔒 Security Scan** - OWASP Dependency Check
3. **🐳 Docker Build** - Build e push para registry
4. **🎭 Deploy Staging** - Deploy automático (branch `develop`)
5. **🏭 Deploy Production** - Deploy automático (branch `main`)

### Branch Strategy

- `main` → Produção (deploy automático)
- `develop` → Staging (deploy automático)
- `feature/*` → Apenas testes

## 🌟 Funcionalidades

### Endpoints Principais

- `POST /api/jwt/validate` - Valida JWT
- `POST /api/jwt/extract-claims` - Extrai claims do JWT

### Características Técnicas

- ⚡ **Performance**: Algoritmo otimizado para números primos
- 🔒 **Segurança**: Secrets management, validação robusta
- 📊 **Observabilidade**: Logs estruturados, métricas
- 🔄 **Escalabilidade**: Auto-scaling, load balancing
- 🧪 **Testabilidade**: Cobertura completa de testes

## 🚀 Deploy em Produção

### Opções de Deploy

| Plataforma          | Comando                       | Documentação                                |
|---------------------|-------------------------------|---------------------------------------------|
| **AWS (Terraform)** | `cd terraform && ./deploy.sh` | [📖 Infraestrutura](docs/INFRASTRUCTURE.md) |
| **Docker Local**    | `docker-compose up --build`   | [📖 Deploy](docs/DEPLOYMENT.md)             |
| **Heroku**          | `git push heroku main`        | [📖 Deploy](docs/DEPLOYMENT.md)             |

### Ambientes Disponíveis

- 🧪 **Desenvolvimento**: Recursos otimizados (~$35/mês)
- 🎭 **Staging**: Ambiente de testes (~$75/mês)
- 🏭 **Produção**: Alta disponibilidade (~$150/mês)

## 🤝 Contribuindo

1. **Fork** o repositório
2. **Crie** uma branch: `git checkout -b feature/nova-funcionalidade`
3. **Implemente** com testes
4. **Execute** testes: `mvn test`
5. **Submeta** Pull Request

Consulte o **[Guia de Desenvolvimento](docs/DEVELOPMENT.md)** para detalhes completos.

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🆘 Suporte

- **Issues**: [GitHub Issues](https://github.com/seu-usuario/jwt-validator/issues)
- **Documentação**: [docs/](docs/)
- **API Docs**: `http://localhost:8080/swagger-ui.html`
