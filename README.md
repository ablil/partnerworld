# Partnerworld service

[![CI/CD](https://github.com/ablil/partnerworld/actions/workflows/cicd.yaml/badge.svg)](https://github.com/ablil/partnerworld/actions/workflows/cicd.yaml)

## Development


### Firestore emulator

A Docker container is configured to run Firestore emulator in Datastore mode, exposed on port 8081
```shell
docker compose up -d emulator
```

To reset the emulator and cleanup existing data on runtime without rebooting, run :
```shell
curl http://0.0.0.0:8081/reset -X POST
```

To export data from the emulator, run:
```shell
curl -X POST http://0.0.0.0:8081/emulator/v1/projects/myproject:export \
-H 'Content-Type: application/json' \
-d '{"database":"projects/myproject/databases/", "export_directory":"/tmp"}'
```
*note that the data is exported to the container volume itself, and not the host machine*

### Pubsub emulator

A Docker image is configured and exposed on port 8085:
```shell
docker compose up -d pubsub
```

To create a topic and a pull subscription, run:
```shell
python scripts/emulators/pubsub/setup.py <project_id> <topic_id> <subscription_id>
# or when running the emulator
PUBSUB_EMULATOR_HOST=localhost:8085 python scripts/emulators/pubsub/setup.py <project_id> <topic_id> <subscription_id>
```
or you can run a Makefile target that run all the container and create sample topic/subscription for you:
```shell
make emulators
```

**Important !**
If you want to run any python script with GCP client library against the emulator, make sure to set the following env variables:

```shell
PUBSUB_EMULATOR_HOST=localhost:8085 PUBSUB_PROJECT_ID=myproject python run.py
```

### Run locally

Before running the application locally, make sure to start Firestore emulator, or configure a remote firestore database.

Run:
```shell
./gradlew bootRun
```


### Generate sample data

You can create random configuration through REST API call, as follows:
```shell
curl -X POST http://0.0.0.0:8080/configurations/random
```

*For ease of use, this endpoint is ready-to-use on `http/configurations.http`*

### URLs

* http://localhost:8080/swagger-ui/index.html

# Terraform

Check [here](./IaC/README.md)

# References

[HCP terraform workspace](https://app.terraform.io/app/ablil-org/workspaces)
