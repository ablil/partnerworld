# Partnerworld service


## Development


### Firestore emulator

A Docker container is configured to run Firestore emulator in Datastore mode, exposed on port 8081
```shell
docker-compose up -d firestore
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
