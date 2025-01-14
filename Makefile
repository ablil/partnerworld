.PHONY: java

PROJECT_ID := gcp-training-playground-405915
REGION := europe-west3
SERVICE := partnerworld
ENV := development
JAVA_HOME := /opt/homebrew/Cellar/openjdk@17/17.0.13/libexec/openjdk.jdk/Contents/Home
GCLOUD_OPTS = --project=$(PROJECT_ID) --region=$(REGION)


clean:
	./gradlew clean

java:
	export JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.13/libexec/openjdk.jdk/Contents/Home

runCloud: clean
	JAVA_HOME=$(JAVA_HOME) PROJECT_ID=$(PROJECT_ID) DATABASE_ID=$(SERVICE)-$(ENV) SPRING_PROFILES_ACTIVE=cloud ./gradlew bootRun

emulator:
	docker compose up -d emulator

run: emulator clean
	SPRING_PROFILES_ACTIVE=emulator JAVA_HOME=$(JAVA_HOME) ./gradlew bootRun

# update cloud run service with local docker image
deploy:
	./gradlew clean jibDockerBuild
	docker tag $(SERVICE):latest ghcr.io/ablil/$(SERVICE):local
	docker push ghcr.io/ablil/$(SERVICE):local
	gcloud run services update $(SERVICE)-$(ENV) $(GCLOUD_OPTS) --image=$(REGION)-docker.pkg.dev/$(PROJECT_ID)/ghcr-$(ENV)/ablil/partnerworld:local

# open cloud run service with gcp proxy :8080
proxy:
	gcloud run services proxy $(SERVICE)-$(ENV) --project $(PROJECT_ID) --region $(REGION)
