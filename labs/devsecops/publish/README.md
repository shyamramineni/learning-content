# Build and Publish from Jenkins Pipeline

Let's build our application as a docker image and publish it to Docker Hub.

For this activity you have to create a `Dockerfile`. This file contains instructins for building the application as a docker image. You can relate this with `Jenkinsfile` but with a different syntax. Create a `Dockerfile` at `project/products-api` and copy the below content. `Dockerfile` should be placed at the same level as your `pom.xml` and `Jenkinsfile`.


## Dockerfile contents

```dockerfile
FROM maven:3.6.3-jdk-11 AS builder

WORKDIR /usr/src/api
COPY . .

RUN chmod +x restore.sh && ./restore.sh
RUN chmod +x build.sh  && ./build.sh

# app
FROM openjdk:11.0.12-jre-slim-buster

WORKDIR /app
COPY --from=builder /usr/src/api/target/products-api-0.1.0.jar .

EXPOSE 80
ENTRYPOINT ["java", "-jar", "/app/products-api-0.1.0.jar"]

ENV JRE_VERSION="11.0.12" \
  APP_VERSION="0.4.0"

ARG BUILD_VERSION=local
ARG GIT_COMMIT=local
LABEL build_version=${BUILD_VERSION}
LABEL commit_sha=${GIT_COMMIT}
```

## Accessing Images on Registries

*Registries* are servers which store Docker images. Docker Hub is the most popular, but the registry API is standard and you can run your own registry in the cloud or locally as your private image store.

Organizations run their own registries so they can have a long-term store of release versions from the build pipeline, or to make images available in the same network region as the production environment.

## Pushing images

Any images you build are only stored on your machine.

To share images you need to push them to a *registry* - like Docker Hub. For Docker Hub the image name needs to include your username, which Docker Hub uses to identify ownership.

Make sure you've [registered on Docker Hub](https://hub.docker.com/signup/). Then save your Docker ID in a variable, so we can use it in later commands:

```
# on Linux or macOS:
dockerId='<your-docker-hub-id>'

# OR with PowerShell:
$dockerId='<your-docker-hub-id>'
```

> This is your Hub username *not* your email address. For mine I use: `$dockerId='thecodecamp'`

Now you can build an image, including your Docker ID in the  name:

```
docker build -t ${dockerId}/products-api:24.04 -f project/products-api/Dockerfile project/products-api

docker image ls '*/products-api'
```

📋 Now push your own `products-api:24.04` image to Docker Hub.

<details>
  <summary>Not sure how?</summary>

```
# log in if you haven't already:
docker login -u ${dockerId}

# push your image:
docker push ${dockerId}/products-api:24.04
```

</details><br/>

## Add build stage inside Jenkinsfile

Let's put the steps inside Jenkinsfile.

Create a global environment section in `Jenkinsfile` and add below content. **Make sure you change the name of the repository to your repository**

```groovy
environment {
  REPOSITORY="your-docker-id"
  RELEASE= "24.04"
}
```

Add `build` stage:

```groovy
stage('Build') {
  steps {
    echo "Building Java app"
    dir ('project/products-api') {
      sh '''
        docker build -t ${REPOSITORY}/products-api:${RELEASE}-${BUILD_NUMBER} -f Dockerfile .
      '''
    }
  }
}
```

## Pushing to Docker Hub

The publish part of our pipeline will push images to Docker Hub. For that you'll need three things:

- a [Docker Hub account](https://hub.docker.com) - create one if you don't already have one; the free tier is fine
- your Docker Hub credentials stored in Jenkins
- a change to the `Jenkinsfile` so the image tags have your Docker Hub username

When you have a Docker Hub account, you can browse to [your settings](https://hub.docker.com/settings/security) and generate an access token. Copy the token to your clipboard - you can use it to log in so you don't store your actual password in Jenkins.

Now create a credential in Jenkins:

- browse to http://localhost:8080/credentials/store/system/domain/_/
- click _Add Credentials_ and make sure the _Kind_ is _Username with password_
- enter your Docker Hub username (**not** your email address) in the _Username_ field
- enter your access token in the _Password_ field
- call the credential `docker-hub` in the _ID_ field

Next edit the [Jenkinsfile](compose/Jenkinsfile):

- replace the value of the environment variable `REPOSITORY="thecodecamp"` with your own Docker Hub ID
- _e.g. my Hub ID is `thecodecamp` so my updated setting will read `REPOSITORY="thecodecamp"`_
- add the publish stage:

```groovy
stage('Push build version') {
  steps {
    echo "Pushing images for release: ${RELEASE}, build: ${BUILD_NUMBER}"       
    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'REGISTRY_USER', passwordVariable: 'REGISTRY_PASSWORD')]) {
      sh '''
      // ToDo: Add the login and push commands
      '''
    }
  }
}
```

Push your changes:

```
git add project/products-api/Jenkinsfile  
git commit -m 'Added push'
git push gogs main
```

📋 Build the Jenkins job again and verify the images are built and pushed to Docker Hub under your username.
