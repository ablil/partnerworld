variable "gcp_credentials" {
    type = string
    description = "gcp credentials setup through HCP terraform workspace variable"
    sensitive = true
}
variable "project_id" {
    type = string
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

