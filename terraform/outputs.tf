# Outputs for JWT Validator API Infrastructure

# Network outputs
output "vpc_id" {
  description = "ID of the VPC"
  value       = aws_vpc.main.id
}

output "vpc_cidr_block" {
  description = "CIDR block of the VPC"
  value       = aws_vpc.main.cidr_block
}

output "public_subnet_ids" {
  description = "IDs of the public subnets"
  value       = aws_subnet.public[*].id
}

output "private_subnet_ids" {
  description = "IDs of the private subnets"
  value       = aws_subnet.private[*].id
}

# Load Balancer outputs
output "load_balancer_dns_name" {
  description = "DNS name of the load balancer"
  value       = aws_lb.main.dns_name
}

output "load_balancer_zone_id" {
  description = "Zone ID of the load balancer"
  value       = aws_lb.main.zone_id
}

output "load_balancer_arn" {
  description = "ARN of the load balancer"
  value       = aws_lb.main.arn
}

# Application URL
output "application_url" {
  description = "URL to access the JWT Validator API"
  value       = "http://${aws_lb.main.dns_name}"
}

output "health_check_url" {
  description = "Health check endpoint URL"
  value       = "http://${aws_lb.main.dns_name}/actuator/health"
}

output "swagger_ui_url" {
  description = "Swagger UI URL for API documentation"
  value       = "http://${aws_lb.main.dns_name}/swagger-ui/index.html"
}

# ECS outputs
output "ecs_cluster_id" {
  description = "ID of the ECS cluster"
  value       = aws_ecs_cluster.main.id
}

output "ecs_cluster_arn" {
  description = "ARN of the ECS cluster"
  value       = aws_ecs_cluster.main.arn
}

output "ecs_service_name" {
  description = "Name of the ECS service"
  value       = aws_ecs_service.jwt_validator.name
}

output "ecs_task_definition_arn" {
  description = "ARN of the ECS task definition"
  value       = aws_ecs_task_definition.jwt_validator.arn
}

# ECR outputs
output "ecr_repository_url" {
  description = "URL of the ECR repository"
  value       = aws_ecr_repository.jwt_validator.repository_url
}

output "ecr_repository_arn" {
  description = "ARN of the ECR repository"
  value       = aws_ecr_repository.jwt_validator.arn
}

# Security outputs
output "alb_security_group_id" {
  description = "ID of the ALB security group"
  value       = aws_security_group.alb.id
}

output "ecs_security_group_id" {
  description = "ID of the ECS tasks security group"
  value       = aws_security_group.ecs_tasks.id
}

# Secrets Manager outputs
output "jwt_secret_arn" {
  description = "ARN of the JWT secret in Secrets Manager"
  value       = aws_secretsmanager_secret.jwt_secret.arn
  sensitive   = true
}

# CloudWatch outputs
output "cloudwatch_log_group_name" {
  description = "Name of the CloudWatch log group"
  value       = aws_cloudwatch_log_group.ecs.name
}

output "cloudwatch_log_group_arn" {
  description = "ARN of the CloudWatch log group"
  value       = aws_cloudwatch_log_group.ecs.arn
}

# IAM outputs
output "ecs_task_execution_role_arn" {
  description = "ARN of the ECS task execution role"
  value       = aws_iam_role.ecs_task_execution_role.arn
}

output "ecs_task_role_arn" {
  description = "ARN of the ECS task role"
  value       = aws_iam_role.ecs_task_role.arn
}

# Auto Scaling outputs
output "autoscaling_target_resource_id" {
  description = "Resource ID of the auto scaling target"
  value       = aws_appautoscaling_target.ecs_target.resource_id
}

# Deployment information
output "deployment_info" {
  description = "Deployment information and next steps"
  value = {
    environment     = var.environment
    project_name    = var.project_name
    aws_region      = var.aws_region
    application_url = "http://${aws_lb.main.dns_name}"
    next_steps = [
      "1. Build and push Docker image to ECR: ${aws_ecr_repository.jwt_validator.repository_url}",
      "2. Update ECS service to deploy the new image",
      "3. Monitor application logs in CloudWatch: ${aws_cloudwatch_log_group.ecs.name}",
      "4. Test the API endpoints using the application URL above"
    ]
  }
}

# Cost estimation (approximate)
output "estimated_monthly_cost" {
  description = "Estimated monthly cost breakdown (USD, approximate)"
  value = {
    note = "Costs are approximate and may vary based on usage"
    fargate_tasks = "~$${var.desired_count * 30 * 24 * (var.task_cpu/1024 * 0.04048 + var.task_memory/1024 * 0.004445)} (${var.desired_count} tasks)"
    nat_gateway = var.enable_nat_gateway ? "~$32-64 (depends on single_nat_gateway setting)" : "$0 (disabled)"
    load_balancer = "~$16-22"
    ecr_storage = "~$1-5 (depends on image size and count)"
    cloudwatch_logs = "~$1-5 (depends on log volume)"
    secrets_manager = "~$0.40"
    total_estimate = "~$50-100+ per month"
  }
}
