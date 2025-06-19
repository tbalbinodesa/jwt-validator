# JWT Validator API - AWS Infrastructure com Terraform

Este diretório contém a configuração do Terraform para provisionar a infraestrutura AWS necessária para executar a API
JWT Validator.

## 📋 Visão Geral da Arquitetura

A infraestrutura provisionada inclui:

- **VPC** com subnets públicas e privadas em múltiplas AZs
- **Application Load Balancer (ALB)** para distribuição de tráfego
- **ECS Fargate** para execução de containers
- **ECR** para armazenamento de imagens Docker
- **Secrets Manager** para gerenciamento seguro do JWT secret
- **CloudWatch** para logs e monitoramento
- **Auto Scaling** para escalabilidade automática
- **Security Groups** para controle de acesso

## 🏗️ Componentes da Infraestrutura

### Rede

- VPC com CIDR 10.0.0.0/16
- 2 subnets públicas (para ALB)
- 2 subnets privadas (para ECS tasks)
- Internet Gateway e NAT Gateways
- Route tables configuradas

### Computação

- ECS Cluster com Fargate
- Task Definition configurada para a aplicação
- Service com health checks
- Auto Scaling baseado em CPU e memória

### Segurança

- Security Groups restritivos
- IAM roles com permissões mínimas
- JWT secret armazenado no Secrets Manager
- Logs centralizados no CloudWatch

## 🚀 Pré-requisitos

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

## 📦 Estrutura dos Arquivos

```
terraform/
├── main.tf                    # Configuração principal da infraestrutura
├── variables.tf               # Definição de variáveis
├── outputs.tf                 # Outputs da infraestrutura
├── terraform.tfvars.example   # Exemplo de configuração
└── README.md                  # Esta documentação
```

## 🛠️ Configuração e Deploy

### 1. Preparação

```bash
# Clone o repositório (se ainda não fez)
git clone <repository-url>
cd jwt-validator/terraform

# Copie o arquivo de exemplo e configure suas variáveis
cp terraform.tfvars.example terraform.tfvars
```

### 2. Configuração das Variáveis

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

### 3. Inicialização do Terraform

```bash
# Inicialize o Terraform
terraform init

# Valide a configuração
terraform validate

# Visualize o plano de execução
terraform plan
```

### 4. Deploy da Infraestrutura

```bash
# Aplique a configuração
terraform apply

# Confirme com 'yes' quando solicitado
```

### 5. Build e Push da Imagem Docker

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

### 6. Atualização do ECS Service

```bash
# Volte para o diretório terraform
cd terraform

# Force uma nova implantação do ECS service
aws ecs update-service \
  --cluster $(terraform output -raw ecs_cluster_id) \
  --service $(terraform output -raw ecs_service_name) \
  --force-new-deployment
```

## 🔍 Verificação do Deploy

Após o deploy, você pode verificar se tudo está funcionando:

```bash
# Obtenha a URL da aplicação
terraform output application_url

# Teste o health check
curl $(terraform output -raw health_check_url)

# Acesse a documentação da API
echo "Swagger UI: $(terraform output -raw swagger_ui_url)"
```

## 📊 Monitoramento

### CloudWatch Logs

```bash
# Visualize os logs da aplicação
aws logs tail $(terraform output -raw cloudwatch_log_group_name) --follow
```

### ECS Service Status

```bash
# Verifique o status do service
aws ecs describe-services \
  --cluster $(terraform output -raw ecs_cluster_id) \
  --services $(terraform output -raw ecs_service_name)
```

## 💰 Estimativa de Custos

A infraestrutura provisionada tem um custo estimado de **$50-100 USD/mês**, incluindo:

- **Fargate Tasks**: ~$30-60/mês (dependendo do número de tasks)
- **NAT Gateway**: ~$32-64/mês (pode ser desabilitado em dev)
- **Application Load Balancer**: ~$16-22/mês
- **ECR Storage**: ~$1-5/mês
- **CloudWatch Logs**: ~$1-5/mês
- **Secrets Manager**: ~$0.40/mês

### Otimização de Custos para Desenvolvimento

Para reduzir custos em ambiente de desenvolvimento:

```hcl
# No terraform.tfvars
enable_nat_gateway = false  # Remove NAT Gateway
task_cpu          = 256     # Reduz CPU
task_memory       = 512     # Reduz memória
desired_count     = 1       # Apenas 1 task
```

## 🔧 Configurações por Ambiente

### Desenvolvimento

```hcl
environment             = "dev"
task_cpu                = 256
task_memory             = 512
desired_count           = 1
enable_nat_gateway      = false
log_retention_days      = 3
```

### Staging

```hcl
environment             = "staging"
task_cpu                = 512
task_memory             = 1024
desired_count           = 2
enable_nat_gateway      = true
single_nat_gateway      = true
log_retention_days      = 7
```

### Produção

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

## 🔒 Segurança

### Boas Práticas Implementadas

1. **Secrets Manager**: JWT secret armazenado de forma segura
2. **IAM Roles**: Permissões mínimas necessárias
3. **Security Groups**: Acesso restrito entre componentes
4. **Private Subnets**: ECS tasks executam em subnets privadas
5. **Encryption**: Logs e secrets criptografados

### Configurações de Segurança Adicionais

Para produção, considere:

```hcl
# Restringir acesso por IP
allowed_cidr_blocks = ["10.0.0.0/8", "172.16.0.0/12"]

# Configurar domínio personalizado com HTTPS
domain_name     = "api.seudominio.com"
certificate_arn = "arn:aws:acm:us-east-1:123456789012:certificate/seu-cert-id"
```

## 🚨 Troubleshooting

### Problemas Comuns

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

## 🧹 Limpeza

Para remover toda a infraestrutura:

```bash
# ATENÇÃO: Isso removerá TODOS os recursos criados
terraform destroy

# Confirme com 'yes' quando solicitado
```

## 📚 Recursos Adicionais

- [Documentação do Terraform AWS Provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs)
- [AWS ECS Documentation](https://docs.aws.amazon.com/ecs/)
- [AWS Fargate Documentation](https://docs.aws.amazon.com/fargate/)
- [Spring Boot on AWS](https://spring.io/guides/gs/spring-boot-docker/)

## 🤝 Contribuição

Para contribuir com melhorias na infraestrutura:

1. Faça um fork do repositório
2. Crie uma branch para sua feature
3. Teste suas mudanças
4. Submeta um pull request

## 📞 Suporte

Para questões relacionadas à infraestrutura, abra uma issue no repositório do projeto.
