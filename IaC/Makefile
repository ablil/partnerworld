.PHONY: default

ENV ?= development
TF_WORKSPACE = partnerworld-$(ENV)

default: plan init

init:
	TF_WORKSPACE=$(TF_WORKSPACE) terraform -chdir=terraform init
	
plan: init
	TF_WORKSPACE=$(TF_WORKSPACE) terraform -chdir=terraform plan -var-file=../environments/$(ENV).auto.tfvars

apply: init
	TF_WORKSPACE=$(TF_WORKSPACE) terraform -chdir=terraform apply -var-file=../environments/$(ENV).auto.tfvars

destroy: init
	TF_WORKSPACE=$(TF_WORKSPACE) terraform -chdir=terraform destroy -var-file=../environments/$(ENV).auto.tfvars


output:
	TF_WORKSPACE=$(TF_WORKSPACE) terraform -chdir=terraform output
