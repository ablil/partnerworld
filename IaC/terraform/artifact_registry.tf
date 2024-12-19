resource "google_artifact_registry_repository" "ghcr" {
  location      = var.region
  repository_id = var.repository
  description   = "Docker images registy"
  format        = "DOCKER"
  mode = "REMOTE_REPOSITORY"

  remote_repository_config {
      description = "Github container registry"
      common_repository {
          uri = "https://ghcr.io"
      }
  }
}
