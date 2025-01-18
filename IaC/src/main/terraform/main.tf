terraform {
  cloud {
    organization = "ablil-org"

    workspaces {
      # expecting workspace through env variable TF_WORKSPACE
      name = "partnerworld"
    }
  }
  required_providers {
    google = {
      source = "hashicorp/google"
      version = "6.13.0"
    }
  }
}
