# NEG (Network endpoint group)
resource "google_compute_region_network_endpoint_group" "default_neg" {
  name   = "default"
  region = var.region
  cloud_run {
    service = google_cloud_run_v2_service.partnerworld.name
  }
}

# backend service
resource "google_compute_backend_service" "backend_service" {
  name                  = "default"
  load_balancing_scheme = "EXTERNAL_MANAGED"
  protocol              = "HTTPS"
  backend {
    group = google_compute_region_network_endpoint_group.default_neg.id
  }

  depends_on = [google_compute_region_network_endpoint_group.default_neg]
}

# url map
resource "google_compute_url_map" "default_url_map" {
  name            = "default"
  default_service = google_compute_backend_service.backend_service.id
  depends_on      = [google_compute_backend_service.backend_service]
}

# Create a certificate map
resource "google_certificate_manager_certificate_map" "default" {
  name        = "my-certificate-map"
  description = "Certificate map for my load balancer"
}

# target proxy
resource "google_compute_target_https_proxy" "default_proxy" {
  name    = "default-https-proxy"
  url_map = google_compute_url_map.default_url_map.id
  certificate_map = format("//certificatemanager.googleapis.com/projects/%s/locations/global/certificateMaps/%s",
    var.project_id,
    google_certificate_manager_certificate_map.default.name
  )
  depends_on = [google_compute_url_map.default_url_map]
}

# forwarding rul
resource "google_compute_global_forwarding_rule" "default" {
  name                  = "default"
  target                = google_compute_target_https_proxy.default_proxy.id
  port_range            = "80"
  load_balancing_scheme = "EXTERNAL_MANAGED"
  depends_on            = [google_compute_target_https_proxy.default_proxy]
}


