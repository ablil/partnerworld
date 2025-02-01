# IaC (Infrastructure as Code)

This submodule contains all the resources definitions through terraform code, and it's state is managed on
[HCP terraform](https://app.terraform.io/app/ablil-org/workspaces).

# Pre-requisites

Terraform CLI is a required tools for running all the terraform tasks, do NOT forget to log in to HCP terraform by
running:

```shell
terraform login
```

Make sure you also have a gcp account.

# Get started

Some basic targets are configured to interact with Terraform through Makefile.

| task       | description                                          |
|------------|------------------------------------------------------|
| make init  | terraform init                                       |
| make plan  | terraform plan                                       |
| make plan  | terraform apply                                      |
| make .avro | copy avro schemas from avro submodule to this module |


# GitHub workflows

A GitHub workflow has been configured to automatically deploy/update all the resources when a new commit is pushed to
main branch.

Make sure the following secrets are configured:

| secret                | description                              |
|-----------------------|------------------------------------------|
| HCP_TERRAFORM_API_KEY | used to authenticated with HCP terraform |


# Avro schemas
Since avro schema files need to be upload to HCP terraform as part of the terraform source code, a Makefile target is configured to copy 
all avro schemas from avro submodule to this module.
```shell
make .avro
```