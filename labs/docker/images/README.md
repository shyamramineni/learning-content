# Building Container Images

*Images* are the packages which containers run from. You'll build an image for each component of your application, and the image has all the pre-requisites installed and configured, ready to run.

You can think of images as the filesystem for the container, plus some metadata which tells Docker which command to run when the container starts.

## Reference

- [Dockerfile syntax](https://docs.docker.com/engine/reference/builder/)
- [Image build command](https://docs.docker.com/engine/reference/commandline/image_build/)

## Base images

Images are built in a hierarchy - you may start with an OS image which sets up a particular Linux distro, then build on top of that to add your application runtime.

Before we build any images we'll set the Docker to use the original build engine:

```
# on macOS or Linux:
export DOCKER_BUILDKIT=0

# OR with PowerShell:
$env:DOCKER_BUILDKIT=0
```

> [BuildKit](https://docs.docker.com/develop/develop-images/build_enhancements/) is the new Docker build engine. It produces the same images as the original engine, but the printed output doesn't show the Dockerfile instructions being executed. We're using the original Engine so you can see all the steps.

We'll build a really simple base image:

- [Base image Dockerfile](./base/Dockerfile)

```
docker build -t dockercourse/base ./labs/docker/images/base
```

- `-t` or `--tag` gives the image a name
- you end the build command with the path to the Dockerfile folder
- the folder path is called the *context* - it contains the Dockerfile and any files it references

📋 List all the images you have - then filter them for images starting with `dockercourse`.

<details>
  <summary>Not sure how?</summary>

```
# list all local images:
docker image ls

# and filter for the dockercourse images:
docker image ls 'dockercourse/*'
```

</details><br/>

> These are the images stored in your local Docker engine cache.

The new base image doesn't add anything to the [official Ubuntu image](https://hub.docker.com/_/ubuntu), which is available in lots of different [versions](https://hub.docker.com/_/ubuntu?tab=tags&page=1&ordering=last_updated).


📋 Pull the main Ubuntu image, then pull the image for Ubuntu version 22.04.


<details>
  <summary>Not sure how?</summary>

```
docker pull ubuntu

# image versions are set in the tag name:
docker pull ubuntu:22.04
```

</details><br/>

List all your Ubuntu images and your own base image:

```
docker image ls --filter reference=ubuntu --filter reference=dockercourse/base
```

> You'll see they all have the same ID - they're actually all aliases for a single image

## Commands and entrypoints

The Dockerfile syntax is straightforward to learn:

- every image needs to start `FROM` another image
- you use `RUN` to execute commands as part of the build
- and `CMD` sets the command to run when the container starts

> Basic instructions in the Dockerfile

| Syntax      | Description |
| ----------- | ----------- |
| FROM        | Pull the base image       |
| RUN         | Execute command        |
| CMD         | Provide default command for the executing container        |
| WORKDIR     | Sets the working directory        |
| COPY        | Copy a directory/file from your local machine to the docker image        |
| ADD         | Same as COPY but also allows <src> as URL        |
| EXPOSE      | Exposes the port of the docker container where the application runs        |
| ENV         | Sets the environment variables        |

Here's a simple example which installs the curl tool:

- [curl Dockerfile](./curl/Dockerfile)

📋 Build an image called `dockercourse/curl` from the `labs/images/curl` Dockerfile.

<details>
  <summary>Not sure how?</summary>

```
docker build -t dockercourse/curl ./labs/docker/images/curl
```

</details><br/>

Now you can run a container from the image, but it might not behave as you expect:

```
# just runs curl:
docker run dockercourse/curl 

# doesn't pass the URL to curl:
docker run dockercourse/curl google.com

# to use curl you need to specify the curl command:
docker run dockercourse/curl curl --head google.com
```

> The `CMD` instruction sets the exact command for a container to run. You can't pass options to the container command - but you can override it completely.

This updated Dockerfile makes a more usable image:

- [curl Dockerfile - v2](./curl/Dockerfile.v2)

Build a v2 image from that Dockerfile:

```
docker build -t dockercourse/curl:v2 -f labs/docker/images/curl/Dockerfile.v2 labs/docker/images/curl
```

- the `-f` flag specifies the Dockerfile name - you need it if you're not using the standard name

You can run containers from this image with more logical syntax:

```
docker run dockercourse/curl:v2 --head google.com
```

> The `--head` argument and URL in the container command gets passed to the entrypoint

📋 List all the `dockercourse/curl` images to compare sizes.

<details>
  <summary>Not sure how?</summary>

```
docker image ls dockercourse/curl
```

</details><br/>

> The v2 image is smaller - which means it has less stuff in the filesystem and a smaller attack surface.


## Image hierarchy

You don't typically use OS images as the base in your `FROM` image. You want to get as many of your app's pre-requisites already installed for you.

You should use [official images](https://hub.docker.com/search?q=&type=image&image_filter=official&category=languages), which are application and runtime images which are maintained by the project teams.

This Dockerfile bundles some custom HTML content on top of the official Nginx image:

- [web Dockerfile](./web/Dockerfile)
- [custom HTML page](./web/index.html)

```
docker build -t dockercourse/web ./labs/docker/images/web
```

- the folder path is called the *context* - it contains the Dockerfile and any files it references, the `index.html` file in this case

📋 Run a container from your new image, publishing port 80, and browse to it.

<details>
  <summary>Not sure how?</summary>

```
# use any free local port, e.g. 8090:
docker run -d -p 8090:80 dockercourse/web

curl localhost:8090
```

</details><br/>

> The container serves your HTML document, using the Nginx setup configured in the official image 

## Lab 1

Your turn to write a Dockerfile. 

There's a simple Java app in this folder which has already been compiled into the file `labs/images/java/HelloWorld.class`.

Build a Docker image which packages that app, and run a container to confirm it's working. The command your container needs to run is `java HelloWorld`.

> Stuck? Try [hints](./lab-1/hints.md) or check the [solution](./lab-1/solution.md).

## Lab 2

Write a Dockerfile to build an image for Tomcat version 9.

| Instruction      | Command |
| ----------- | ----------- |
| Pull Ubuntu:18.04 as base image        | FROM       |
| Install OpenJDK-11              | RUN        |
| Create working directory at /opt/tomcat         | WORKDIR        |
| Download tomcat package from https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.56/bin/apache-tomcat-9.0.56.tar.gz     | ADD        |
| Extract tomcat package        | RUN  |
| Move the extracted contents to /opt/tomcat         | RUN |
| Expose port 8080      | EXPOSE|
| Set the default command to run tomcat when the container starts          | CMD |

> Stuck? Try [hints](./lab-2/hints.md) or check the [solution](./lab-2/solution.md).
___
## Cleanup

Cleanup by removing all containers:

```
docker rm -f $(docker ps -aq)
```