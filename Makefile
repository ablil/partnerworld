.PHONY: java

PROJECT_ID := gcp-training-playground-405915
REGION := europe-west3
SERVICE := partnerworld
JAVA_HOME := /opt/homebrew/Cellar/openjdk@17/17.0.13/libexec/openjdk.jdk/Contents/Home


clean:
	./gradlew clean

java:
	export JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.13/libexec/openjdk.jdk/Contents/Home

run:
	JAVA_HOME=$(JAVA_HOME) PROJECT_ID=$(PROJECT_ID) DATABASE_ID=$(SERVICE) ./gradlew bootRun


# open cloudrun service with gcp proxy :8080
proxy:
	gcloud run services proxy $(SERVICE) --project $(PROJECT_ID) --region $(REGION)

terraform-init:
	terraform -chdir=terraform init

deploy-artifact-registry: terraform-init
	terraform -chdir=terraform apply -target="google_artifact_registry_repository.my-repo" --auto-approve

deploy: terraform-init
	terraform -chdir=terraform apply 
	@echo "run: 'make proxy', visit: http://localhost:8080"

apply:
	terraform -chdir=terraform apply 

destroy: terraform-init
	terraform -chdir=terraform destroy --auto-approve

