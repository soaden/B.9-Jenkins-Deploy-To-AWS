# Jenkins Deployment to AWS EC2

This repository is based on a clone from https://github.com/soaden/B.8-Jenkins (check that repo's README for a full overview).
It complements the previously demonstrated version of Jenkins CI/CD pipeline with actual deployment to AWS EC2.

## Technologies

- Jenkins
- Groovy
- Docker
- Docker Compose
- Docker Hub
- AWS

## Pipeline

The pipeline executes the following steps:

1. Build the Java application with Maven.
2. Build a Docker image containing the application.
3. Publish the Docker image to a Docker Hub repository.
4. Deploy a container to AWS EC2 instance from Docker Hub image.

## Repository structure

- `Jenkinsfile` — defines the Jenkins pipeline stages and execution flow.
- `script.groovy` — external Groovy script invoked by the Jenkinsfile to keep pipeline logic separate from the pipeline definition.
- `src/`, `pom.xml` — Java/Maven demo application source code and build configuration.
- `Dockerfile` — defines how the application Docker image is built.
- `.gitignore` — Git ignore rules.

## Branches

**Master**

The `master` branch demonstrates basic deployment to AWS EC2. Compared to the base repo (https://github.com/soaden/B.8-Jenkins), here the function `deployApp` in the `script.groovy` implements the logic for Jenkins to SSH-connect to an AWS EC2 instance, pull the Docker image from Docker Hub and run container.

**Docker Compose**

The `feature/docker-compose` branch demonstrates the Docker Compose Plugin capability.
It allows to deploy several containers in one command through the added to repository `docker-compose.yaml` file.
This file is copied by Jenkins to the target server and executed with `docker compose` command (changes reflected in `script.groovy` file.

**Jenkins Multibranch Pipeline**

The `feature/multi-branch-pipeline` branch demonstrates Jenkins Multibranch Pipeline functionality.

In that branch, the `Jenkinsfile` contains stage execution conditions based on the branch name.

The use case demonstrates how a single, shared `Jenkinsfile` can define different pipeline behavior for different branches. For example, the pipeline can stop after the test stage for feature branches, while continuing to the deployment stage when running on the `master` branch.

**Jenkins Shared Library**

The `feature/jenkins-shared-library` branch demonstrates Jenkins Shared Library functionality.  

In that branch, the `Jenkinsfile` uses functions `buildJar` and `buildImage` provided by the Shared Library instead of defining them in `script.groovy`.  
This approach enables pipeline functions to be reused across multiple pipelines and projects.  

The Jenkins Shared Library implementation is maintained in a separate repository: https://github.com/soaden/B.8-Jenkins-Shared-Library.  

**Increment Build Version**

The `feature/increment-build-version` branch demonstrates automatic build version incrementing.

In that branch, `script.groovy` increments the version in `pom.xml`, which defines the application build version. The `IMAGE_NAME` variable is used to assign the corresponding Docker image tag.

The `Jenkinsfile` contains two additional stages to handle the versioning workflow. In the `Dockerfile`, `ENTRYPOINT` is replaced with `CMD` so the application can be started using the version-dependent JAR filename.

The `pushVersionUpdate` function in `script.groovy` commits and pushes the updated `pom.xml` back to GitHub. A Jenkins plugin prevents these Jenkins-authored commits from triggering another build through the GitHub webhook, avoiding a build loop.

