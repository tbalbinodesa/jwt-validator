#!/bin/bash

# JWT Validator API - Deployment Script
# This script automates the deployment process for the JWT Validator API on AWS

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if required tools are installed
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    # Check if terraform is installed
    if ! command -v terraform &> /dev/null; then
        print_error "Terraform is not installed. Please install Terraform >= 1.0"
        exit 1
    fi
    
    # Check if AWS CLI is installed
    if ! command -v aws &> /dev/null; then
        print_error "AWS CLI is not installed. Please install AWS CLI"
        exit 1
    fi
    
    # Check if Docker is installed
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker"
        exit 1
    fi
    
    # Check AWS credentials
    if ! aws sts get-caller-identity &> /dev/null; then
        print_error "AWS credentials not configured. Please run 'aws configure'"
        exit 1
    fi
    
    print_success "All prerequisites are met"
}

# Function to initialize Terraform
init_terraform() {
    print_status "Initializing Terraform..."
    
    if [ ! -f "terraform.tfvars" ]; then
        print_warning "terraform.tfvars not found. Creating from example..."
        cp terraform.tfvars.example terraform.tfvars
        print_warning "Please edit terraform.tfvars with your configuration before continuing"
        read -p "Press Enter to continue after editing terraform.tfvars..."
    fi
    
    terraform init
    terraform validate
    
    print_success "Terraform initialized successfully"
}

# Function to deploy infrastructure
deploy_infrastructure() {
    print_status "Planning Terraform deployment..."
    terraform plan
    
    echo
    read -p "Do you want to proceed with the deployment? (y/N): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_status "Deploying infrastructure..."
        terraform apply -auto-approve
        print_success "Infrastructure deployed successfully"
    else
        print_warning "Deployment cancelled"
        exit 0
    fi
}

# Function to build and push Docker image
build_and_push_image() {
    print_status "Building and pushing Docker image..."
    
    # Get ECR repository URL
    ECR_URL=$(terraform output -raw ecr_repository_url)
    AWS_REGION=$(terraform output -raw aws_region || echo "us-east-1")
    
    print_status "ECR Repository: $ECR_URL"
    
    # Login to ECR
    print_status "Logging in to ECR..."
    aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_URL
    
    # Go to project root
    cd ..
    
    # Build Docker image
    print_status "Building Docker image..."
    docker build -t jwt-validator .
    
    # Tag image
    print_status "Tagging image..."
    docker tag jwt-validator:latest $ECR_URL:latest
    
    # Push image
    print_status "Pushing image to ECR..."
    docker push $ECR_URL:latest
    
    # Return to terraform directory
    cd terraform
    
    print_success "Docker image built and pushed successfully"
}

# Function to update ECS service
update_ecs_service() {
    print_status "Updating ECS service..."
    
    CLUSTER_NAME=$(terraform output -raw ecs_cluster_id)
    SERVICE_NAME=$(terraform output -raw ecs_service_name)
    
    aws ecs update-service \
        --cluster $CLUSTER_NAME \
        --service $SERVICE_NAME \
        --force-new-deployment \
        --query 'service.serviceName' \
        --output text
    
    print_success "ECS service updated successfully"
}

# Function to wait for deployment to complete
wait_for_deployment() {
    print_status "Waiting for deployment to complete..."
    
    CLUSTER_NAME=$(terraform output -raw ecs_cluster_id)
    SERVICE_NAME=$(terraform output -raw ecs_service_name)
    
    aws ecs wait services-stable \
        --cluster $CLUSTER_NAME \
        --services $SERVICE_NAME
    
    print_success "Deployment completed successfully"
}

# Function to verify deployment
verify_deployment() {
    print_status "Verifying deployment..."
    
    APP_URL=$(terraform output -raw application_url)
    HEALTH_URL=$(terraform output -raw health_check_url)
    
    print_status "Application URL: $APP_URL"
    print_status "Health Check URL: $HEALTH_URL"
    
    # Wait a bit for the service to be ready
    sleep 30
    
    # Test health endpoint
    if curl -f -s "$HEALTH_URL" > /dev/null; then
        print_success "Health check passed!"
        print_success "Application is running at: $APP_URL"
        print_success "API Documentation: $APP_URL/swagger-ui/index.html"
    else
        print_warning "Health check failed. The application might still be starting up."
        print_status "You can check the logs with: aws logs tail /ecs/jwt-validator --follow"
    fi
}

# Function to show deployment info
show_deployment_info() {
    echo
    print_success "=== DEPLOYMENT COMPLETE ==="
    echo
    terraform output deployment_info
    echo
    terraform output estimated_monthly_cost
    echo
}

# Function to clean up resources
cleanup() {
    print_warning "This will destroy ALL resources created by Terraform!"
    read -p "Are you sure you want to continue? (y/N): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_status "Destroying infrastructure..."
        terraform destroy -auto-approve
        print_success "Infrastructure destroyed successfully"
    else
        print_status "Cleanup cancelled"
    fi
}

# Main function
main() {
    echo "JWT Validator API - AWS Deployment Script"
    echo "========================================"
    echo
    
    # Parse command line arguments
    case "${1:-deploy}" in
        "deploy")
            check_prerequisites
            init_terraform
            deploy_infrastructure
            build_and_push_image
            update_ecs_service
            wait_for_deployment
            verify_deployment
            show_deployment_info
            ;;
        "infrastructure-only")
            check_prerequisites
            init_terraform
            deploy_infrastructure
            show_deployment_info
            ;;
        "update-app")
            check_prerequisites
            build_and_push_image
            update_ecs_service
            wait_for_deployment
            verify_deployment
            ;;
        "cleanup")
            cleanup
            ;;
        "help"|"-h"|"--help")
            echo "Usage: $0 [command]"
            echo
            echo "Commands:"
            echo "  deploy              Full deployment (infrastructure + application) [default]"
            echo "  infrastructure-only Deploy only the infrastructure"
            echo "  update-app          Update only the application (rebuild and redeploy)"
            echo "  cleanup             Destroy all resources"
            echo "  help                Show this help message"
            echo
            echo "Examples:"
            echo "  $0                  # Full deployment"
            echo "  $0 deploy           # Full deployment"
            echo "  $0 update-app       # Update application only"
            echo "  $0 cleanup          # Destroy everything"
            ;;
        *)
            print_error "Unknown command: $1"
            echo "Use '$0 help' for usage information"
            exit 1
            ;;
    esac
}

# Run main function with all arguments
main "$@"
