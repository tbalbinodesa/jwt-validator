# 🛠️ Guia de Desenvolvimento - JWT Validator API

## 🚀 Setup do Ambiente de Desenvolvimento

### 📋 Pré-requisitos

| Ferramenta | Versão Mínima | Descrição                       |
|------------|---------------|---------------------------------|
| **Java**   | 21+           | JDK para desenvolvimento        |
| **Maven**  | 3.8+          | Gerenciamento de dependências   |
| **Docker** | 20.10+        | Containerização                 |
| **Git**    | 2.30+         | Controle de versão              |
| **IDE**    | -             | IntelliJ IDEA, VS Code, Eclipse |

### ⚡ Setup Rápido

```bash
# 1. Clone e navegue para o projeto
git clone https://github.com/seu-usuario/jwt-validator.git
cd jwt-validator

# 2. Compile e teste
mvn clean compile test

# 3. Execute a aplicação
mvn spring-boot:run

# 4. Verifique se está funcionando
curl http://localhost:8080/actuator/health
```

**✅ Aplicação rodando em**: `http://localhost:8080`

### 🔧 Configuração da IDE

#### IntelliJ IDEA (Recomendado)

```bash
# 1. Importe o projeto
File → Open → Selecione o diretório do projeto

# 2. Configure o SDK
File → Project Structure → Project → SDK: Java 21

# 3. Plugins recomendados
Settings → Plugins → Install:
```

| Plugin          | Descrição                      |
|-----------------|--------------------------------|
| **Spring Boot** | Suporte para Spring Boot       |
| **SonarLint**   | Análise de qualidade de código |
| **GitToolBox**  | Melhorias para Git             |

#### VS Code

```bash
# Extensões recomendadas
code --install-extension vscjava.vscode-java-pack
code --install-extension vmware.vscode-spring-boot
code --install-extension humao.rest-client
```

## 📁 Arquitetura do Código

### 🏗️ Estrutura de Pacotes

```
src/main/java/com/instrospect/jwt_validator/
├── 🚀 JwtValidatorApplication.java     # Classe principal Spring Boot
├── ⚙️  config/
│   └── OpenApiConfig.java             # Configuração Swagger/OpenAPI
├── 🎮 controller/
│   └── JwtController.java             # Endpoints REST
├── 📦 dto/
│   ├── JwtRequest.java                # Request DTO
│   └── ValidationResponse.java        # Response DTO
├── 🧠 service/
│   └── JwtValidationService.java      # Lógica de negócio
└── 🔧 util/
    └── PrimeUtil.java                 # Utilitários (números primos)
```

### 🧪 Estrutura de Testes

```
src/test/java/com/instrospect/jwt_validator/
├── 🏁 JwtValidatorApplicationTests.java        # Testes de contexto
├── 📖 OpenApiDocumentationTest.java           # Testes de documentação
├── 📋 OpenApiStructureTest.java               # Testes de estrutura OpenAPI
├── 🎮 controller/
│   └── JwtControllerIntegrationTest.java      # Testes de integração
├── 🧠 service/
│   └── JwtValidationServiceTest.java          # Testes unitários do serviço
└── 🔧 util/
    └── PrimeUtilTest.java                     # Testes unitários dos utilitários
```

## 🧪 Executando Testes

### 🚀 Comandos Principais

| Comando                              | Descrição                   | Uso                      |
|--------------------------------------|-----------------------------|--------------------------|
| `mvn test`                           | Executa todos os testes     | Verificação completa     |
| `mvn test -Dtest="*Test"`            | Apenas testes unitários     | Desenvolvimento rápido   |
| `mvn test -Dtest="*IntegrationTest"` | Apenas testes de integração | Verificação de endpoints |
| `mvn jacoco:report`                  | Relatório de cobertura      | Análise de qualidade     |

### 🎯 Testes Específicos

```bash
# Teste específico por classe
mvn test -Dtest="JwtValidationServiceTest"

# Teste específico por método
mvn test -Dtest="JwtValidationServiceTest#shouldValidateValidJwt"

# Múltiplos testes
mvn test -Dtest="JwtValidationServiceTest,PrimeUtilTest"
```

### 📊 Cobertura de Testes

```bash
# Gerar relatório de cobertura
mvn clean test jacoco:report

# Visualizar relatório
open target/site/jacoco/index.html
```

### Testes com Docker

```bash
# Executar testes em container
docker-compose -f docker-compose.test.yml up --build
```

## Desenvolvimento Local

### Execução em Modo de Desenvolvimento

#### Com Maven

```bash
# Execução padrão
mvn spring-boot:run

# Com profile de desenvolvimento
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Com debug habilitado
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

#### Com Docker Compose

```bash
# Modo desenvolvimento (com hot reload)
docker-compose --profile dev up

# Modo produção local
docker-compose up --build
```

### Hot Reload

Para desenvolvimento com hot reload, use:

```bash
# Terminal 1: Compilação contínua
mvn compile -Dspring-boot.repackage.skip=true

# Terminal 2: Aplicação com devtools
mvn spring-boot:run
```

### Variáveis de Ambiente para Desenvolvimento

```bash
# .env (para desenvolvimento local)
JWT_SECRET=dev-secret-key-not-for-production
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_INSTROSPECT=DEBUG
```

## Debugging

### Debug Local

```bash
# Executar com debug na porta 5005
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
```

### Debug com Docker

```yaml
# docker-compose.debug.yml
version: '3.8'
services:
  jwt-validator:
    build: .
    ports:
      - "8080:8080"
      - "5005:5005"  # Debug port
    environment:
      - JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

### Logs de Debug

```bash
# Habilitar logs de debug
export LOGGING_LEVEL_COM_INSTROSPECT=DEBUG

# Ou no application.properties
logging.level.com.instrospect=DEBUG
```

## Qualidade de Código

### Análise Estática

```bash
# SpotBugs
mvn spotbugs:check

# Checkstyle
mvn checkstyle:check

# PMD
mvn pmd:check
```

### Formatação de Código

```bash
# Formatar código (se configurado)
mvn fmt:format

# Verificar formatação
mvn fmt:check
```

## Adicionando Novas Funcionalidades

### 1. Novos Endpoints

1. **Criar DTO** em `dto/`
2. **Implementar lógica** em `service/`
3. **Criar controller** em `controller/`
4. **Adicionar testes** em `test/`
5. **Atualizar documentação**

### 2. Novas Regras de Validação

1. **Implementar em** `JwtValidationService`
2. **Adicionar testes unitários**
3. **Atualizar documentação da API**
4. **Adicionar casos de teste no Insomnia**

### 3. Exemplo: Nova Regra de Validação

```java
// Em JwtValidationService.java
private boolean validateNewRule(Claims claims) {
    // Implementar nova regra
    return true;
}

// Em JwtValidationServiceTest.java
@Test
void shouldValidateNewRule() {
    // Teste da nova regra
}
```

## Testes de Performance

### Testes de Carga Local

```bash
# Usando Apache Bench
ab -n 1000 -c 10 -p jwt-payload.json -T application/json http://localhost:8080/api/jwt/validate

# Usando curl em loop
for i in {1..100}; do
  curl -X POST http://localhost:8080/api/jwt/validate \
    -H "Content-Type: application/json" \
    -d '{"jwt": "token-here"}'
done
```

### Profiling

```bash
# Executar com profiler
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=profile.jfr"
```

## Contribuindo

### Fluxo de Trabalho

1. **Fork** o repositório
2. **Criar branch** para feature: `git checkout -b feature/nova-funcionalidade`
3. **Implementar** mudanças com testes
4. **Executar** todos os testes: `mvn test`
5. **Verificar** cobertura de código
6. **Commit** com mensagem descritiva
7. **Push** para seu fork
8. **Criar** Pull Request

### Padrões de Commit

```bash
# Formato
tipo(escopo): descrição

# Exemplos
feat(api): adicionar endpoint de extração de claims
fix(validation): corrigir validação de números primos
docs(readme): atualizar documentação da API
test(service): adicionar testes para validação de role
refactor(util): otimizar algoritmo de números primos
```

### Checklist para Pull Request

- [ ] Testes passando (`mvn test`)
- [ ] Cobertura de código mantida/melhorada
- [ ] Documentação atualizada
- [ ] Código formatado adequadamente
- [ ] Sem warnings de análise estática
- [ ] Changelog atualizado (se aplicável)

## Troubleshooting

### Problemas Comuns

#### 1. Testes Falhando

```bash
# Limpar e recompilar
mvn clean compile test

# Verificar dependências
mvn dependency:tree
```

#### 2. Aplicação Não Inicia

```bash
# Verificar porta em uso
lsof -i :8080

# Verificar logs
mvn spring-boot:run | grep ERROR
```

#### 3. Hot Reload Não Funciona

```bash
# Verificar se devtools está habilitado
mvn dependency:tree | grep devtools

# Reiniciar IDE
```

### Logs Úteis

```bash
# Logs da aplicação
tail -f logs/application.log

# Logs do Maven
mvn spring-boot:run -X

# Logs do Docker
docker-compose logs -f jwt-validator
```

## Recursos Adicionais

### Documentação

- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

### Ferramentas Recomendadas

- **Postman/Insomnia**: Testes de API
- **JProfiler/VisualVM**: Profiling
- **SonarQube**: Análise de qualidade
- **Docker Desktop**: Containerização local
