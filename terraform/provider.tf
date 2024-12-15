terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
      version = "6.13.0"
    }
  }
}

provider "google" {
  project = "gcp-training-playground-405915"
  region  = "europe-west3"
}

