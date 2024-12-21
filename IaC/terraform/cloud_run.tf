resource "google_cloud_run_v2_service" "partnerworld" {
  name     = "partnerworld"
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
          value = "cloud"
      }
    }
  }
  depends_on = [google_artifact_registry_repository.ghcr, google_firestore_database.database]
}
