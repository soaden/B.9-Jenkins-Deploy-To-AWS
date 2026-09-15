# Jenkins Pipeline Demo

This repository is checked out by Jenkins and demonstrates a Jenkins CI/CD pipeline integrating Maven, Docker, and Docker Hub.

## Technologies

- Jenkins
- Groovy
- Maven
- Java
- Docker
- Docker Hub

## Pipeline

The pipeline executes the following steps:

1. Build the Java application with Maven.
2. Build a Docker image containing the application.
3. Publish the Docker image to a Docker Hub repository.
4. Simulate application deployment by logging the deployment step.

## Repository structure

- `Jenkinsfile` — defines the Jenkins pipeline stages and execution flow.
- `script.groovy` — external Groovy script invoked by the Jenkinsfile to keep pipeline logic separate from the pipeline definition.
- `src/`, `pom.xml` — Java/Maven demo application source code and build configuration.
- `Dockerfile` — defines how the application Docker image is built.
- `.gitignore` — Git ignore rules.

## Branches

The `feature/multi-branch-pipeline` branch demonstrates Jenkins Multibranch Pipeline functionality.

In this branch, the `Jenkinsfile` contains stage execution conditions based on the branch name.

The use case demonstrates how a single, shared `Jenkinsfile` can define different pipeline behavior for different branches. For example, the pipeline can stop after the test stage for feature branches, while continuing to the deployment stage when running on the `master` branch.
