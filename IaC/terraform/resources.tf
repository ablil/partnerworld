resource "google_artifact_registry_repository" "ghcr" {
  location      = var.region
  repository_id = "ghcr-${var.environment}"
  description   = "Docker images registy for ${var.environment}"
  format        = "DOCKER"
  mode          = "REMOTE_REPOSITORY"

  remote_repository_config {
    description = "Github container registry"
    common_repository {
      uri = "https://ghcr.io"
    }
  }
}

resource "google_firestore_database" "database" {
  project         = var.project_id
  name            = "partnerworld-${var.environment}"
  location_id     = var.region
  type            = "DATASTORE_MODE"
  deletion_policy = "DELETE"
}


resource "google_cloud_run_v2_service" "partnerworld" {
  name                = "partnerworld-${var.environment}"
  location            = var.region
  deletion_protection = false
  ingress             = "INGRESS_TRAFFIC_ALL"

  template {
    containers {
      image = "${var.region}-docker.pkg.dev/${var.project_id}/${google_artifact_registry_repository.ghcr.repository_id}/ablil/partnerworld:latest"
      env {
        name  = "PROJECT_ID"
        value = var.project_id
      }
      env {
        name  = "DATABASE_ID"
        value = google_firestore_database.database.name
      }
      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "cloud,${var.environment}"
      }
      env {
        name  = "subscription"
        value = google_pubsub_subscription.partner_pull_subscription.name
      }
    }
  }
  depends_on = [
    google_artifact_registry_repository.ghcr, google_firestore_database.database,
    google_pubsub_subscription.partner_pull_subscription
  ]
}

// pubsub related resources
resource "google_pubsub_topic" "partner_configurations_topic" {
  name                       = "partner-configurations"
  message_retention_duration = "3600s"
  schema_settings {
    schema   = google_pubsub_schema.configuration_schema.id
    encoding = "JSON"
  }

  depends_on = [google_pubsub_schema.configuration_schema]
}

resource "google_pubsub_schema" "configuration_schema" {
  name       = "partnerconfiguration"
  type       = "AVRO"
  definition = <<EOF
    {
     "type" : "record",
     "name" : "Avro",
     "fields" : [
       {
         "name" : "shortName",
         "type" : "string"
       },
       {
         "name" : "displayName",
         "type" : "string"
       }
     ]
    }
  EOF
}

resource "google_pubsub_subscription" "partner_pull_subscription" {
  name  = "partner-configuration"
  topic = google_pubsub_topic.partner_configurations_topic.id

  message_retention_duration = "1200s" # 20 minutes
  retain_acked_messages = true
  ack_deadline_seconds  = 10

  expiration_policy {
    ttl = "604800s" # 7 days
  }

  retry_policy {
    minimum_backoff = "10s"
  }

  enable_message_ordering = false
  depends_on = [google_pubsub_topic.partner_configurations_topic]
}