default: .PHONY

SRC_DIR = src/main/terraform

clean:
	rm -rf $(SRC_DIR)/.terraform.lock.hcl
	rm -rf $(SRC_DIR)/.terraform
	rm -rf $(SRC_DIR)/avro/**/*.avsc

.avro:
	cp ../avro/src/main/avro/*.avsc src/main/terraform/avro/

init: .avro
	terraform -chdir=src/main/terraform init

plan: init
	terraform -chdir=src/main/terraform plan

apply: init
	terraform -chdir=src/main/terraform apply