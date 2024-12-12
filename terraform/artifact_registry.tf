resource "google_artifact_registry_repository" "my-repo" {
  location      = var.region
  repository_id = var.repository
  description   = "Docker container repository"
  format        = "DOCKER"
}
