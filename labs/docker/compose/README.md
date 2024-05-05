# Docker Compose

Docker Compose is a specification for describing apps which run in containers, and a command-line tool which takes those specs and runs them in Docker.

It's a _desired-state_ approach, where you model your apps in YAML and the Compose command line creates or replaces components to get to your desired state.

## Reference

- [Docker Compose manual](https://docs.docker.com/compose/)
- [Compose specification - GitHub](https://github.com/compose-spec/compose-spec/blob/master/spec.md)
- [Docker Compose v3 syntax](https://docs.docker.com/compose/compose-file/compose-file-v3/)


<details>
  <summary>CLI overview</summary>

The original Docker Compose CLI is a separate tool:

```
docker-compose --help

docker-compose up --help
```

The latest versions of Docker have the Compose command built-in. The commands are the same, minus the hyphen so `docker-compose` becomes `docker compose`:

```
docker compose --help

docker compose up --help
```

> This is new functionality and it's not 100% compatible with the original Compose CLI. For this lab you should be able to use either, but if you have any issues stick with `docker-compose`.

</details><br/>


## Multi-container apps

Docker can run any kind of app - the container image could be for a lightweight microservice or a legacy monolith. They all work in the same way, but containers are especially well suited to distributed applications, where each component runs in a separate container.

Try running a sample app - this is the web component for a banking customers list:

```shell
docker run -d -p 8088:80 --name banking-ui thecodecamp/banking-ui:latest
```

The application is exposed on port `8088`, but it will not run. Browsing to http://localhost:8088 shows application not running.

📋 Check the application logs to see what's happening.

<details>
  <summary>Not sure how?</summary>

```
docker logs banking-ui
```

</details><br/>

> The web app is just the front-end, it's trying to find a backend API service at http://api-server/ - but there's no such service running. The `api-server` also requires a `customers-db` in the backend

You could start an API container and DB container with `docker run`, but you'd need to know the name of the image, the ports to use, and the network setup for the containers to talk to each other.

Instead you can use `docker compose` to model all containers. There are three components of the application and each has its own Docker image.

Here's the application architecture:

![](/imgs/banking-application-architecture.png)

- Customers database - a MySql database, built with some sample data. <b>Image:</b> `thecodecamp/customers-db:latest`

- Banking API - a Go REST API application that reads data from Customers database. <b>Image:</b> `thecodecamp/banking-api:latest`

- Banking UI - a jQuery based website that used Banking API to show data. <b>Image:</b> `thecodecamp/banking-ui:latest`

## Compose app definition

Docker Compose can define the services of your app - which run in containers - and the networks that join the containers together. You can use `compose` even for simple apps:

- [docker-compose.yml](./app/docker-compose.yml)

> Why bother putting this all this in a compose file? It specifies an image version and the ports to use; it acts as project documentation, as well as being an executable spec for the app.

Docker Compose has its own command line - this tells you the available commands:

```
docker compose
```

📋 Run this application using the `docker compose` CLI.

<details>
  <summary>Not sure how?</summary>

```
# run 'up' to start the app, pointing to the Compose file
docker compose -f labs/docker/compose/app/docker-compose.yml up
```

</details><br/>

> The Banking application containers starts in interactive mode; you can browse to http://localhost:8082 to check it's working.

Use Ctrl-C / Cmd-C to exit - that stops the container.

## Multi-container apps in Compose

Compose is more useful with more components. [app/docker-compose.yml](./app/docker-compose.yml) defines the three parts of the banking app:

- there are three services, one for the `API`, one for the `Web` and one for `DB`
- each service defines the image to use and the ports to expose
- the `banking-api` web service adds an environment variable to configure AUTH
- all `services` and `db` are set to connect to the same container network
- the network is defined but it has no special options set.

📋 Run the app using detached containers and use Compose to print the container status and logs.

<details>
  <summary>Not sure how?</summary>

```shell
# run the app:
docker-compose -f labs/docker/compose/app/docker-compose.yml up -d

# use compose to show just this app's containers:
docker-compose -f labs/docker/compose/app/docker-compose.yml ps

# and this app's logs:
docker-compose -f labs/docker/compose/app/docker-compose.yml logs
```

</details><br/>

These are just standard containers - the Compose CLI sends commands to the Docker engine in the same way that the usual Docker CLI does.

You can manage containers created with Compose using the Docker CLI too:

```shell
docker ps
```

Finally shutdown all containers
```shell
# Shutdown all containers started with this compose file:
docker compose -f labs/docker/compose/app/docker-compose.yml down
```

___
## Cleanup

Cleanup by removing all containers:

```
docker rm -f $(docker ps -aq)
```