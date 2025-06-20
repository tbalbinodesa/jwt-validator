## Prompts Utilizados para Criar o Projeto JWT Validator

Baseado na análise detalhada do projeto JWT Validator, embora não tenham sido encontrados arquivos específicos contendo
os prompts originais, é possível inferir a sequência de prompts que foram utilizados para criar este projeto através da
análise da estrutura, commits e funcionalidades implementadas.

### 🎯 **Prompt Principal (Inicial)**

```
Crie uma API Spring Boot para validação de JSON Web Tokens (JWTs) com as seguintes especificações:

1. **Regras de Validação**:
   - JWT deve ter estrutura válida com assinatura válida
   - Deve conter exatamente 3 claims: Name, Role e Seed
   - Name não pode conter números (máximo 256 caracteres)
   - Role deve ser: Admin, Member ou External
   - Seed deve ser um número primo

2. **Tecnologias**:
   - Java 21
   - Spring Boot 3.x
   - Maven
   - Lombok
   - JJWT library

3. **Estrutura**:
   - Controller REST com endpoint POST /api/jwt/validate
   - DTOs para request e response
   - Service para lógica de validação
   - Utilitário para verificação de números primos
```

### 🔧 **Prompts de Desenvolvimento Sequencial**

#### 1. **Setup Inicial**

```
Configure um projeto Spring Boot com:
- Java 21
- Dependências: spring-boot-starter-web, spring-boot-starter-validation
- JJWT para manipulação de JWT
- Lombok para redução de boilerplate
- Estrutura de pacotes: controller, service, dto, util
```

#### 2. **DTOs e Estruturas de Dados**

```
Crie DTOs para:
- JwtRequest: classe com campo jwt (String) e validação @NotBlank
- ValidationResponse: classe com campo isValid (boolean)
Utilize Lombok para getters, setters e construtores
```

#### 3. **Lógica de Validação**

```
Implemente JwtValidationService com:
- Método para validar estrutura do JWT
- Validação de exatamente 3 claims (Name, Role, Seed)
- Validação de Name sem números
- Validação de Role (Admin, Member, External)
- Validação de Seed como número primo
- Método para extrair claims do JWT
```

#### 4. **Utilitário de Números Primos**

```
Crie PrimeUtil com:
- Método isPrime() otimizado
- Tratamento de casos especiais (números negativos, 0, 1, 2)
- Algoritmo eficiente para verificação de primalidade
```

#### 5. **Controller REST**

```
Implemente JwtController com:
- Endpoint POST /api/jwt/validate
- Recebe JwtRequest e retorna ValidationResponse
- Tratamento de exceções
- Validação de entrada com @Valid
```

### 🧪 **Prompts para Testes**

#### 6. **Testes Unitários**

```
Crie testes abrangentes para:
- JwtValidationServiceTest: todos os cenários de validação
- PrimeUtilTest: casos de teste para números primos
- Testes de casos válidos e inválidos
- Cobertura de edge cases
```

#### 7. **Testes de Integração**

```
Implemente JwtControllerIntegrationTest com:
- Testes de endpoints usando @SpringBootTest
- Cenários de sucesso e falha
- Validação de responses HTTP
- Testes com JWTs válidos e inválidos
```

### 📖 **Prompts para Documentação**

#### 8. **OpenAPI/Swagger**

```
Configure documentação OpenAPI com:
- OpenApiConfig para configuração do Swagger
- Anotações nos controllers para documentação
- Exemplos de requests e responses
- Descrições detalhadas dos endpoints
```

#### 9. **Documentação Completa**

```
Crie documentação abrangente incluindo:
- README.md com visão geral, setup e exemplos
- API_DOCUMENTATION.md com especificações detalhadas
- DEVELOPMENT.md com guia para desenvolvedores
- DEPLOYMENT.md com instruções de deploy
```

### 🐳 **Prompts para Containerização**

#### 10. **Docker**

```
Crie Dockerfile multi-stage com:
- Stage de build usando Maven
- Stage de runtime com JRE otimizada
- Configurações de segurança
- Otimizações de tamanho da imagem
```

#### 11. **Docker Compose**

```
Configure docker-compose.yml para:
- Execução local simplificada
- Configuração de portas
- Variáveis de ambiente
- Profiles para desenvolvimento e produção
```

### 🚀 **Prompts para CI/CD**

#### 12. **GitHub Actions**

```
Implemente pipeline CI/CD com:
- Jobs para test, build, security scan
- Build e push de imagem Docker
- Deploy automatizado
- Relatórios de teste e cobertura
- Cache de dependências Maven
```

#### 13. **Segurança**

```
Adicione verificações de segurança:
- OWASP Dependency Check
- Scan de vulnerabilidades
- Análise de código estático
```

### ☁️ **Prompts para Infraestrutura**

#### 14. **Terraform AWS**

```
Crie infraestrutura AWS com Terraform:
- ECS Fargate para execução de containers
- Application Load Balancer
- ECR para registry de imagens
- VPC e configurações de rede
- IAM roles e políticas
```

#### 15. **Coleção de Testes**

```
Crie coleção Insomnia com:
- Requests pré-configurados
- Exemplos de JWTs válidos e inválidos
- Variáveis de ambiente
- Documentação de uso
```

### 📋 **Observações**

- Os prompts foram executados de forma incremental, construindo o projeto camada por camada
- Cada funcionalidade foi implementada com testes correspondentes
- A documentação foi criada em paralelo ao desenvolvimento
- O projeto seguiu boas práticas de desenvolvimento e arquitetura
- A infraestrutura foi pensada para ser escalável e production-ready

Esta sequência de prompts resultou em um projeto profissional, bem documentado e pronto para produção, demonstrando uma
abordagem estruturada e metodológica no desenvolvimento.
