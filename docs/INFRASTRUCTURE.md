# Infraestrutura AWS - JWT Validator API

## Visão Geral

Este documento descreve a infraestrutura AWS completa para hospedar a JWT Validator API usando **Terraform** como
Infrastructure as Code (IaC).

## Arquitetura da Solução

### Diagrama de Arquitetura

```
Internet → ALB → ECS Fargate Tasks (Private Subnets)
                      ↓
                 ECR Repository
                      ↓
              Secrets Manager (JWT Secret)
                      ↓
              CloudWatch Logs
```

### Componentes Principais

#### 🌐 **Rede (VPC)**

- **VPC** dedicada com CIDR 10.0.0.0/16
- **2 Subnets Públicas** (para Application Load Balancer)
- **2 Subnets Privadas** (para ECS Tasks)
- **Internet Gateway** para acesso à internet
- **NAT Gateways** para saída de internet das subnets privadas
- **Route Tables** configuradas para roteamento adequado

#### ⚖️ **Load Balancer**

- **Application Load Balancer (ALB)** público
- **Target Groups** configurados para ECS tasks
- **Health Checks** no endpoint `/actuator/health`
- **Security Groups** restritivos

#### 🐳 **Containerização**

- **Amazon ECR** para armazenamento de imagens Docker
- **ECS Cluster** com Fargate para execução serverless
- **Task Definitions** otimizadas para a aplicação Spring Boot
- **ECS Service** com health checks e rolling deployments

#### 🔒 **Segurança**

- **AWS Secrets Manager** para JWT secret
- **IAM Roles** com permissões mínimas necessárias
- **Security Groups** com acesso restrito
- **Tasks executando em subnets privadas**

#### 📊 **Monitoramento**

- **CloudWatch Logs** centralizados
- **Container Insights** (opcional)
- **Auto Scaling** baseado em CPU e memória

#### 🔄 **Auto Scaling**

- **Escalabilidade automática** de 1 a 10 tasks
- **Métricas**: CPU (70%) e Memória (80%)
- **Fargate Spot** disponível para otimização de custos

## Estrutura dos Arquivos Terraform

```
terraform/
├── main.tf                    # Configuração principal da infraestrutura
├── variables.tf               # Definições de variáveis
├── outputs.tf                 # Outputs da infraestrutura
├── terraform.tfvars.example   # Template de configuração
├── deploy.sh                  # Script de deployment automatizado
└── README.md                  # Documentação detalhada
```

## Pré-requisitos

### Ferramentas Necessárias

1. **AWS CLI** configurado com credenciais apropriadas
2. **Terraform** >= 1.0 instalado
3. **Docker** para build da imagem (opcional)
4. Permissões AWS para criar os recursos necessários

### Permissões AWS Necessárias

Sua conta/usuário AWS precisa das seguintes permissões:

- EC2 (VPC, Subnets, Security Groups, etc.)
- ECS (Clusters, Services, Task Definitions)
- ECR (Repositories)
- IAM (Roles, Policies)
- Application Load Balancer
- Secrets Manager
- CloudWatch Logs

## Deployment da Infraestrutura

### Opção 1: Script Automatizado (Recomendado)

```bash
# Navegue para o diretório terraform
cd terraform

# Execute o script de deployment
./deploy.sh

# Ou para comandos específicos:
./deploy.sh infrastructure-only  # Apenas infraestrutura
./deploy.sh update-app          # Apenas aplicação
./deploy.sh cleanup             # Remover tudo
```

### Opção 2: Comandos Manuais

#### 1. Preparação

```bash
# Clone o repositório (se ainda não fez)
git clone <repository-url>
cd jwt-validator/terraform

# Copie o arquivo de exemplo e configure suas variáveis
cp terraform.tfvars.example terraform.tfvars
```

#### 2. Configuração das Variáveis

Edite o arquivo `terraform.tfvars` com suas configurações:

```hcl
# Configuração básica
aws_region   = "us-east-1"
environment  = "dev"
project_name = "jwt-validator"

# Configuração de segurança
jwt_secret = "seu-jwt-secret-super-seguro-aqui"

# Configuração de recursos
task_cpu      = 512
task_memory   = 1024
desired_count = 2
```

#### 3. Inicialização do Terraform

```bash
# Inicialize o Terraform
terraform init

# Valide a configuração
terraform validate

# Visualize o plano de execução
terraform plan
```

#### 4. Deploy da Infraestrutura

```bash
# Aplique a configuração
terraform apply

# Confirme com 'yes' quando solicitado
```

#### 5. Build e Push da Imagem Docker

Após o deploy da infraestrutura, você precisa fazer o build e push da imagem:

```bash
# Obtenha a URL do ECR repository
ECR_URL=$(terraform output -raw ecr_repository_url)

# Configure o Docker para usar o ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $ECR_URL

# Volte para o diretório raiz do projeto
cd ..

# Build da imagem
docker build -t jwt-validator .

# Tag da imagem
docker tag jwt-validator:latest $ECR_URL:latest

# Push da imagem
docker push $ECR_URL:latest
```

#### 6. Atualização do ECS Service

```bash
# Volte para o diretório terraform
cd terraform

# Force uma nova implantação do ECS service
aws ecs update-service \
  --cluster $(terraform output -raw ecs_cluster_id) \
  --service $(terraform output -raw ecs_service_name) \
  --force-new-deployment
```

## Verificação do Deploy

Após o deploy, você pode verificar se tudo está funcionando:

```bash
# Obtenha a URL da aplicação
terraform output application_url

# Teste o health check
curl $(terraform output -raw health_check_url)

# Acesse a documentação da API
echo "Swagger UI: $(terraform output -raw swagger_ui_url)"
```

## Configurações por Ambiente

### 🧪 Desenvolvimento (Custo Otimizado)

```hcl
environment        = "dev"
task_cpu          = 256
task_memory       = 512
desired_count     = 1
enable_nat_gateway = false  # Economia de ~$32/mês
log_retention_days = 3
```

### 🎭 Staging

```hcl
environment             = "staging"
task_cpu                = 512
task_memory             = 1024
desired_count           = 2
enable_nat_gateway      = true
single_nat_gateway      = true   # Economia de ~$32/mês
log_retention_days      = 7
```

### 🏭 Produção

```hcl
environment                = "prod"
task_cpu                   = 1024
task_memory                = 2048
desired_count              = 3
min_capacity               = 2
max_capacity               = 20
enable_nat_gateway         = true
single_nat_gateway         = false
log_retention_days         = 30
enable_container_insights  = true
enable_deletion_protection = true
```

## Estimativa de Custos

### Ambiente de Desenvolvimento

- **Fargate Tasks**: ~$15-30/mês (1 task, recursos menores)
- **Load Balancer**: ~$16/mês
- **ECR + Logs**: ~$2-5/mês
- **Secrets Manager**: ~$0.40/mês
- **Total**: ~$35-50/mês

### Ambiente de Produção

- **Fargate Tasks**: ~$60-120/mês (3+ tasks)
- **NAT Gateways**: ~$64/mês (2 AZs)
- **Load Balancer**: ~$22/mês
- **ECR + Logs**: ~$5-10/mês
- **Secrets Manager**: ~$0.40/mês
- **Total**: ~$150-220/mês

### Otimização de Custos para Desenvolvimento

Para reduzir custos em ambiente de desenvolvimento:

```hcl
# No terraform.tfvars
enable_nat_gateway = false  # Remove NAT Gateway
task_cpu          = 256     # Reduz CPU
task_memory       = 512     # Reduz memória
desired_count     = 1       # Apenas 1 task
```

## Segurança Implementada

### ✅ Boas Práticas Aplicadas

1. **Princípio do Menor Privilégio**: IAM roles com permissões mínimas
2. **Defesa em Profundidade**: Múltiplas camadas de segurança
3. **Secrets Management**: JWT secret no AWS Secrets Manager
4. **Network Isolation**: ECS tasks em subnets privadas
5. **Encryption**: Dados em trânsito e em repouso criptografados

### 🛡️ Controles de Segurança

- **Security Groups**: Acesso restrito entre componentes
- **NACLs**: Controle de rede adicional
- **IAM Policies**: Permissões granulares
- **VPC Flow Logs**: Auditoria de tráfego (opcional)

### Configurações de Segurança Adicionais

Para produção, considere:

```hcl
# Restringir acesso por IP
allowed_cidr_blocks = ["10.0.0.0/8", "172.16.0.0/12"]

# Configurar domínio personalizado com HTTPS
domain_name     = "api.seudominio.com"
certificate_arn = "arn:aws:acm:us-east-1:123456789012:certificate/seu-cert-id"

# WAF para proteção adicional
enable_waf = true

# Backup automatizado
enable_backup = true
backup_retention_days = 30
```

## Monitoramento e Observabilidade

### 📈 Métricas Disponíveis

- **CPU e Memória**: Auto scaling baseado em utilização
- **Request Count**: Número de requisições no ALB
- **Response Time**: Latência das requisições
- **Health Checks**: Status da aplicação

### 📋 Logs Centralizados

```bash
# Visualizar logs em tempo real
aws logs tail $(terraform output -raw cloudwatch_log_group_name) --follow

# Logs específicos por task
aws logs filter-log-events \
  --log-group-name /ecs/jwt-validator \
  --filter-pattern "ERROR"

# ECS Service Status
aws ecs describe-services \
  --cluster $(terraform output -raw ecs_cluster_id) \
  --services $(terraform output -raw ecs_service_name)
```

## Operações Comuns

### 🔄 Atualizar Aplicação

```bash
./deploy.sh update-app
```

### 📊 Verificar Status

```bash
# Status do ECS service
aws ecs describe-services \
  --cluster jwt-validator-cluster \
  --services jwt-validator

# Health check
curl $(terraform output -raw health_check_url)
```

### 🔍 Troubleshooting

#### Problemas Comuns

1. **ECS Tasks não iniciam**
   ```bash
   # Verifique os logs
   aws logs tail /ecs/jwt-validator --follow
   
   # Verifique o status das tasks
   aws ecs list-tasks --cluster jwt-validator-cluster
   ```

2. **Health Check falhando**
   ```bash
   # Teste diretamente o endpoint
   curl http://ALB-DNS-NAME/actuator/health
   
   # Verifique se a aplicação está rodando na porta 8080
   ```

3. **Imagem não encontrada**
   ```bash
   # Verifique se a imagem foi enviada para o ECR
   aws ecr list-images --repository-name jwt-validator
   ```

### Logs Úteis

```bash
# Logs da aplicação
aws logs tail /ecs/jwt-validator --follow

# Eventos do ECS service
aws ecs describe-services --cluster jwt-validator-cluster --services jwt-validator

# Status do ALB target group
aws elbv2 describe-target-health --target-group-arn TARGET-GROUP-ARN
```

## Benefícios da Solução

### 🚀 **Escalabilidade**

- Auto scaling automático baseado em métricas
- Fargate serverless elimina gerenciamento de servidores
- Load balancer distribui tráfego automaticamente

### 🔒 **Segurança**

- Secrets gerenciados de forma segura
- Network isolation com subnets privadas
- IAM roles com permissões mínimas

### 💰 **Custo-Efetivo**

- Pay-per-use com Fargate
- Opções de otimização para desenvolvimento
- Fargate Spot para cargas não críticas

### 🛠️ **Operacional**

- Deployment automatizado
- Health checks e self-healing
- Logs centralizados
- Infrastructure as Code

### 🔄 **CI/CD Ready**

- Integração fácil com pipelines
- Rolling deployments sem downtime
- Versionamento de imagens no ECR

## Próximos Passos

### 🔧 Melhorias Opcionais

1. **HTTPS/SSL**: Configurar certificado SSL no ALB
2. **Custom Domain**: Configurar domínio personalizado
3. **WAF**: Adicionar Web Application Firewall
4. **Backup**: Implementar backup automatizado
5. **Multi-Region**: Expandir para múltiplas regiões

## Limpeza da Infraestrutura

Para remover toda a infraestrutura:

```bash
# ATENÇÃO: Isso removerá TODOS os recursos criados
terraform destroy

# Confirme com 'yes' quando solicitado
```

## Recursos Adicionais

### Documentação

- [Documentação do Terraform AWS Provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs)
- [AWS ECS Documentation](https://docs.aws.amazon.com/ecs/)
- [AWS Fargate Documentation](https://docs.aws.amazon.com/fargate/)
- [Spring Boot on AWS](https://spring.io/guides/gs/spring-boot-docker/)

### Suporte

Para questões relacionadas à infraestrutura, consulte:

- **Documentação detalhada**: `terraform/README.md`
- **Logs detalhados** para troubleshooting
- **Script de deployment** com verificações automáticas

## Conclusão

A infraestrutura AWS foi implementada com sucesso usando Terraform, fornecendo:

- ✅ **Infraestrutura completa** para a API JWT Validator
- ✅ **Segurança robusta** com melhores práticas
- ✅ **Escalabilidade automática** baseada em demanda
- ✅ **Deployment automatizado** com script dedicado
- ✅ **Monitoramento integrado** com CloudWatch
- ✅ **Documentação completa** para operação

A solução está pronta para uso em produção e pode ser facilmente adaptada para diferentes ambientes e necessidades.

**🚀 Para começar**: `cd terraform && ./deploy.sh`
