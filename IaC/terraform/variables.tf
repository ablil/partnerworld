variable "gcp_credentials" {
    type = string
    description = "gcp credentials setup through HCP terraform workspace variable"
    sensitive = true
}

variable "environment" {
    type = string
    description = "environment where all resources are deployed"
}
variable "project_id" {
    type = string
    default = "gcp-training-playground-405915"
}

variable "region" {
    type = string
    default = "europe-west3"
}

variable "repository" {
    type = string
    default = "ghcr-upstream"
    description = "Docker images repository"
}

