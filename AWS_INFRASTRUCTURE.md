# 🚀 JWT Validator API - Infraestrutura AWS com Terraform

## 📋 Resumo da Implementação

Foi implementada uma infraestrutura completa na AWS usando **OpenTerraform** para hospedar a API JWT Validator. A
solução segue as melhores práticas de DevOps, segurança e escalabilidade.

## 🏗️ Arquitetura Implementada

### Visão Geral

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

## 📁 Estrutura dos Arquivos

```
terraform/
├── main.tf                    # Configuração principal da infraestrutura
├── variables.tf               # Definições de variáveis
├── outputs.tf                 # Outputs da infraestrutura
├── terraform.tfvars.example   # Template de configuração
├── deploy.sh                  # Script de deployment automatizado
└── README.md                  # Documentação detalhada
```

## 🚀 Como Usar

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

```bash
# 1. Configure as variáveis
cp terraform.tfvars.example terraform.tfvars
# Edite terraform.tfvars com suas configurações

# 2. Inicialize o Terraform
terraform init
terraform plan
terraform apply

# 3. Build e push da imagem
ECR_URL=$(terraform output -raw ecr_repository_url)
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $ECR_URL
docker build -t jwt-validator ..
docker tag jwt-validator:latest $ECR_URL:latest
docker push $ECR_URL:latest

# 4. Atualize o ECS service
aws ecs update-service \
  --cluster $(terraform output -raw ecs_cluster_id) \
  --service $(terraform output -raw ecs_service_name) \
  --force-new-deployment
```

## ⚙️ Configurações por Ambiente

### 🧪 Desenvolvimento (Custo Otimizado)

```hcl
environment        = "dev"
task_cpu          = 256
task_memory       = 512
desired_count     = 1
enable_nat_gateway = false  # Economia de ~$32/mês
```

### 🎭 Staging

```hcl
environment        = "staging"
task_cpu          = 512
task_memory       = 1024
desired_count     = 2
single_nat_gateway = true   # Economia de ~$32/mês
```

### 🏭 Produção

```hcl
environment                = "prod"
task_cpu                   = 1024
task_memory                = 2048
desired_count              = 3
min_capacity               = 2
max_capacity               = 20
enable_container_insights  = true
enable_deletion_protection = true
```

## 💰 Estimativa de Custos

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

## 🔒 Segurança Implementada

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

## 📊 Monitoramento e Observabilidade

### 📈 Métricas Disponíveis

- **CPU e Memória**: Auto scaling baseado em utilização
- **Request Count**: Número de requisições no ALB
- **Response Time**: Latência das requisições
- **Health Checks**: Status da aplicação

### 📋 Logs Centralizados

```bash
# Visualizar logs em tempo real
aws logs tail /ecs/jwt-validator --follow

# Logs específicos por task
aws logs filter-log-events \
  --log-group-name /ecs/jwt-validator \
  --filter-pattern "ERROR"
```

## 🔧 Operações Comuns

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

```bash
# Logs da aplicação
aws logs tail /ecs/jwt-validator --follow

# Tasks em execução
aws ecs list-tasks --cluster jwt-validator-cluster

# Status do target group
aws elbv2 describe-target-health \
  --target-group-arn $(terraform output -raw target_group_arn)
```

## 🌟 Benefícios da Solução

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

## 📚 Próximos Passos

### 🔧 Melhorias Opcionais

1. **HTTPS/SSL**: Configurar certificado SSL no ALB
2. **Custom Domain**: Configurar domínio personalizado
3. **WAF**: Adicionar Web Application Firewall
4. **Backup**: Implementar backup automatizado
5. **Multi-Region**: Expandir para múltiplas regiões

### 🎯 Configurações Avançadas

```hcl
# HTTPS com domínio personalizado
domain_name     = "api.seudominio.com"
certificate_arn = "arn:aws:acm:us-east-1:123456789012:certificate/..."

# WAF para proteção adicional
enable_waf = true

# Backup automatizado
enable_backup = true
backup_retention_days = 30
```

## 🤝 Suporte e Contribuição

### 📞 Suporte

- Documentação completa em `terraform/README.md`
- Logs detalhados para troubleshooting
- Script de deployment com verificações automáticas

### 🔄 Contribuição

1. Fork do repositório
2. Criar branch para feature
3. Testar mudanças
4. Submeter pull request

---

## ✅ Conclusão

A infraestrutura AWS foi implementada com sucesso usando Terraform, fornecendo:

- ✅ **Infraestrutura completa** para a API JWT Validator
- ✅ **Segurança robusta** com melhores práticas
- ✅ **Escalabilidade automática** baseada em demanda
- ✅ **Deployment automatizado** com script dedicado
- ✅ **Monitoramento integrado** com CloudWatch
- ✅ **Documentação completa** para operação

A solução está pronta para uso em produção e pode ser facilmente adaptada para diferentes ambientes e necessidades.

**🚀 Para começar**: `cd terraform && ./deploy.sh`
