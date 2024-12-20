resource "google_firestore_database" "database" {
  project     = var.project_id
  name        = "partnerworld"
  location_id = var.region
  type        = "DATASTORE_MODE"
  deletion_policy = "DELETE"
}


