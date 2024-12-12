resource "google_cloud_run_v2_service" "partnerworld" {
  name     = "partnerworld"
  location = var.region
  deletion_protection = false
  ingress = "INGRESS_TRAFFIC_ALL"

  template {
    containers {
      image = "${var.region}-docker.pkg.dev/${var.project_id}/${var.repository}/partnerworld"
    }
  }
}
