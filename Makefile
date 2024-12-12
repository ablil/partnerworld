.PHONY: 

PROJECT_ID := gcp-training-playground-405915
REGION := europe-west3
REPOSITORY := my-repository
SERVICE := partnerworld


setup-java-home:
	export JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.13/libexec/openjdk.jdk/Contents/Home

# build and push docker image
docker: setup-java-home
	./gradlew jibDockerBuild
	docker tag partnerworld:latest $(REGION)-docker.pkg.dev/$(PROJECT_ID)/$(REPOSITORY)/partnerworld:latest
	docker push $(REGION)-docker.pkg.dev/$(PROJECT_ID)/$(REPOSITORY)/partnerworld:latest

# open cloudrun service with gcp proxy :8080
proxy:
	gcloud run services proxy $(SERVICE) --project $(PROJECT_ID) --region $(REGION)

