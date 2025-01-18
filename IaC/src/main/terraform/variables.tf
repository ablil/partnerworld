variable "gcp_credentials" {
    type = string
    description = "gcp credentials setup through HCP terraform workspace variable"
    sensitive = true
}

variable "project_id" {
    type = string
    default = "gcp-training-playground-405915"
}

variable "region" {
    type = string
    default = "europe-west3"
}


