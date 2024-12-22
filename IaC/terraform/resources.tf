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

resource "google_firestore_database" "database" {
  project     = var.project_id
  name        = "partnerworld-${var.environment}"
  location_id = var.region
  type        = "DATASTORE_MODE"
  deletion_policy = "DELETE"
}


resource "google_cloud_run_v2_service" "partnerworld" {
  name     = "partnerworld-${var.environment}"
  location = var.region
  deletion_protection = false
  ingress = "INGRESS_TRAFFIC_ALL"

  template {
    containers {
      image = "${var.region}-docker.pkg.dev/${var.project_id}/${var.repository}/ablil/partnerworld:latest"
      env {
          name = "PROJECT_ID"
          value = var.project_id
      }
      env {
          name = "DATABASE_ID"
          value = google_firestore_database.database.name
      }
      env {
          name = "SPRING_PROFILES_ACTIVE"
          value = "cloud,${var.environment}"
      }
    }
  }
  depends_on = [google_artifact_registry_repository.ghcr, google_firestore_database.database]
}
