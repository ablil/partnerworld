output "cloudrun_url" {
  value = google_cloud_run_v2_service.partnerworld.uri
}

output "load_balancer_ip" {
  value = google_compute_global_forwarding_rule.default.ip_address
}