.PHONY: default

ENV ?= development

default: plan init

init:
	terraform -chdir=terraform init
	
plan: init
	terraform -chdir=terraform plan -var-file=../environments/$(ENV).auto.tfvars

apply: init
	terraform -chdir=terraform apply -var-file=../environments/$(ENV).auto.tfvars

destroy: init
	terraform -chdir=terraform destroy -var-file=../environments/$(ENV).auto.tfvars

