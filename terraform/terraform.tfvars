# JWT Validator API - Terraform Variables Example
# Copy this file to terraform.tfvars and customize the values for your deployment

# Basic Configuration
aws_region  = "us-east-1"
environment = "dev"
project_name = "jwt-validator"

# Network Configuration
vpc_cidr = "10.0.0.0/16"

# Security Configuration
jwt_secret = "your-secure-jwt-secret-here-change-this-value"
allowed_cidr_blocks = ["0.0.0.0/0"]  # Restrict this in production

# ECS Task Configuration
task_cpu = 512   # 0.5 vCPU
task_memory = 1024  # 1 GB RAM
desired_count = 2     # Number of running tasks

# Auto Scaling Configuration
min_capacity = 1
max_capacity = 10

# Docker Image Configuration
image_tag = "latest"  # Change to specific version for production

# Cost Optimization (for development environments)
enable_nat_gateway = true   # Set to false for cost savings in dev
single_nat_gateway = false  # Set to true to use only one NAT Gateway

# Monitoring and Logging
log_retention_days        = 7
enable_container_insights = false  # Set to true for detailed monitoring

# Security and Compliance
enable_deletion_protection = false  # Set to true for production
backup_retention_days = 7

# Optional: Custom Domain (uncomment and configure for production)
# domain_name     = "api.yourdomain.com"
# certificate_arn = "arn:aws:acm:us-east-1:123456789012:certificate/12345678-1234-1234-1234-123456789012"

# Example configurations for different environments:

# Development Environment
# aws_region              = "us-east-1"
# environment             = "dev"
# task_cpu                = 256
# task_memory             = 512
# desired_count           = 1
# min_capacity            = 1
# max_capacity            = 3
# enable_nat_gateway      = false
# log_retention_days      = 3
# enable_deletion_protection = false

# Staging Environment
# aws_region              = "us-east-1"
# environment             = "staging"
# task_cpu                = 512
# task_memory             = 1024
# desired_count           = 2
# min_capacity            = 1
# max_capacity            = 5
# enable_nat_gateway      = true
# single_nat_gateway      = true
# log_retention_days      = 7
# enable_deletion_protection = false

# Production Environment
# aws_region              = "us-east-1"
# environment             = "prod"
# task_cpu                = 1024
# task_memory             = 2048
# desired_count           = 3
# min_capacity            = 2
# max_capacity            = 20
# enable_nat_gateway      = true
# single_nat_gateway      = false
# log_retention_days      = 30
# enable_container_insights = true
# enable_deletion_protection = true
# allowed_cidr_blocks     = ["10.0.0.0/8", "172.16.0.0/12"]  # Restrict access
# domain_name             = "api.yourdomain.com"
# certificate_arn         = "arn:aws:acm:us-east-1:123456789012:certificate/your-cert-id"
