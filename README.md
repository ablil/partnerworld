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


# Terraform

All the resources are defined as code through terrafrom in **IaC/** directory, and are managed by [ HCP terraform ]( https://app.terraform.io/app/ablil-org/workspaces ).

## Local dev

A *Makefile* is provided with some basic targets to deploy or destroy resources.

**deploy**: `make apply`

**destory**: `make destroy`


*Ensure you are authenticated to terraform, by running `terraform login`*


## Github workflows

A Github workflow has been configured to automatially deploy/update all the resources when a new commit is pushed to main branch.

*Ensure the secret `HCP_TERRAFORM_API_KEY` has been added to workflow secrets, in order for the workflow to authenticate to HCP terraform*

# References

[HCP terraform workspace](https://app.terraform.io/app/ablil-org/workspaces)
