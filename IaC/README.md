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

Some basic Gradle tasks are configured to interact with Terraform.

| task                      | description      |
|---------------------------|------------------|
| ./gradlew terraformInit   | terraform init   |
| ./gradlew terraformPlan   | terraform plan   |
| ./gradlew terraformApply  | terraform apply  |
| ./gradlew terraformOutput | terraform output |

To get full list of available task, run:

```shell
./gradlew tasks
```

## Project properties

The Gradle project can be tweaked through the following properties:

| property    | description                             |
|-------------|-----------------------------------------|
| environment | `development` (default) or `production` |

eg, `./gradlew terraformPlan -Penvironment=production`

# GitHub workflows

A GitHub workflow has been configured to automatically deploy/update all the resources when a new commit is pushed to
main branch.

Make sure the following secrets are configured:

| secret                | description                              |
|-----------------------|------------------------------------------|
| HCP_TERRAFORM_API_KEY | used to authenticated with HCP terraform |
