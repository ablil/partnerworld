.PHONY: java

PROJECT_ID := gcp-training-playground-405915
REGION := europe-west3
SERVICE := partnerworld
JAVA_HOME := /opt/homebrew/Cellar/openjdk@17/17.0.13/libexec/openjdk.jdk/Contents/Home


clean:
	./gradlew clean

java:
	export JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.13/libexec/openjdk.jdk/Contents/Home

runCloud:
	JAVA_HOME=$(JAVA_HOME) PROJECT_ID=$(PROJECT_ID) DATABASE_ID=$(SERVICE) SPRING_PROFILES_ACTIVE=cloud ./gradlew bootRun

emulator:
	docker compose up -d emulator

run: emulator
	SPRING_PROFILES_ACTIVE=emulator JAVA_HOME=$(JAVA_HOME) ./gradlew bootRun

# open cloud run service with gcp proxy :8080
proxy:
	gcloud run services proxy $(SERVICE) --project $(PROJECT_ID) --region $(REGION)
