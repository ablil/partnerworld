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
    default = "my-repository"
    description = "Docker images repository"
}
