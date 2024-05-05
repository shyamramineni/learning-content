# Modelling apps with Compose

You can think of Compose as documentation which replaces `docker run` - all the options you would put in the run commands are specified in the Compose file which becomes living documentation. That includes setting up the container environment with variables and voume mounts.

The Compose spec also includes higher-level modelling for configuration files, using `secret` objects. That helps to make it clear that you're modelling configuration instead of storage. 

Compose models can be split across multiple files, which makes it easy to run the same app in different configurations on a single machine.

## Reference

- [Environment variables in Compose](https://docs.docker.com/compose/environment-variables/)
- [Compose secrets](https://docs.docker.com/compose/compose-file/compose-file-v3/#secrets)
- [Merging multiple Compose files](https://docs.docker.com/compose/extends/)

<details>
  <summary>CLI overview</summary>

It's the same Docker Compose CLI:

```
docker-compose --help
```

> The help text isn't super clear, but you can use the `-f` flag multiple times to build a model from several YAML files.

</details><br/>

## Modelling configuration in Compose

Application configuration is applied to containers with environment variables and filesystem mounts. Compose supports the same options as the standard `docker run` command:

- [app/v1.yml](./app/v1.yml) - defines the random number app, using environment variables for config settings

- [app/v2.yml](./app/v2.yml) - uses an environment file for logging configuration, so it can be shared between the components

📋 Run the v1 spec and test the app; then update to the v2 spec - what do you think will happen to the v1 containers?

<details>
  <summary>Not sure how?</summary>

```shell
# deploy v1
docker-compose -f labs/docker/compose-model/app/v1.yml up -d

# try the app at http://localhost:8090

# deploy v2
docker-compose -f labs/docker/compose-model/app/v2.yml up -d
```

</details><br/>

> The upgrade doesn't change anything - environment files are expanded by the CLI and the values are used to set individual environment variables in the containers.

Print the environment variable details from the new web container to see that the env file contents have been expanded:

```shell
docker container inspect --format='{{.Config.Env}}' app-random-ui-1
```

Environment variables are a common way to set application config, but they're not as flexible as config files.

## Volume mounts and secrets

Compose lets you model volume mounts, so you can load local files into the container filesystem. That uses the same approach as `docker run` - specifying a source and target path. Compose also has an abstraction called `secrets`:

- [app/v3.yml](./app/v3.yml) - uses a volumes to load the [logging config file](./app/config/dev/logging.json), and secrets for the [api config](./app/config/dev/api/override.json) and [web config](./app/config/dev/web/override.json) files

> Platforms like Kubernetes use a similar mechanism for secrets, but the source of the data is an object stored in Kubernetes, not a local file on the server.

📋 Run the v3 spec and look at the volume mount configuration for the web container. How are volumes and secrets treated differently?

<details>
  <summary>Not sure how?</summary>

```shell
# deploy v3
docker-compose -f labs/docker/compose-model/app/v3.yml up -d

docker inspect app-random-ui-1

# if you have jq utility installed you can use below command
docker inspect app-random-ui-1 | jq '.[] | .Mounts'
```

</details><br/>

> The volume and the secret are set as bind mounts. The volume spec defaults to being writeable (`"RW": true`), but the secret is `read-only`.

You'll need to understand the different options for configuring app settings, because applications all tend to have different ideas about configuration.

## Running multiple environments

All the containers and networks for the random number app have been created with the `app` prefix. Compose uses a _project name_ to identify all the objects it creates, and it defaults to the name of the folder the YAML files are in.

You can specify a custom project name, and deploy the same model definition again to create a separate copy of your app.

📋 Deploy the v3 spec with the custom project name `app-dev` - does it start correctly?

<details>
  <summary>Not sure how?</summary>

```shell
# use the -p flag to set a project name:
docker-compose -p app-dev -f labs/docker/compose-model/app/v3.yml up -d

# this will fail because ports cannot be used more than once
```

</details><br/>

> Compose creates containers with the new prefix `app-dev`, but they fail to start - they're trying to use ports which are already being used by the `app` containers.

This is where [override files](https://docs.docker.com/compose/extends/#multiple-compose-files) come in - you can split your model across multiple Compose files, using different configuration settings for different environments:

- [app/core.yml](./app/core.yml) - specifies the core application model, with settings which are the same for all environments. This is not a complete model, so you can't deploy this YAML on its own

- [app/dev.yml](./app/dev.yml) - sets the ports and configuration file sources for the dev environment

- [app/test.yml](./app/test.yml) - sets the ports and configuration file sources for the test environment

📋 Run the app in the dev configuration by merging the Compose files and using the project name `app-dev`

<details>
  <summary>Not sure how?</summary>

```shell
# use -p to set a custom project name, and join files starting
# with the core spec and then adding the dev override:
docker-compose -p app-dev -f labs/docker/compose-model/app/core.yml -f labs/docker/compose-model/app/dev.yml up -d
```

</details><br/>

> You merge Compose files with multiple `-f` flags - the files to the right add or override settings from files to the left.

Try the dev app at http://localhost:8190, you'll see random numbers in the range 0-50. 

📋 Check the API logs for the dev environment and confirm they're printing at a detailed level

<details>
  <summary>Not sure how?</summary>

```shell
docker-compose -p app-dev logs random-api

# you might need to include the project name and all the override
# files in any Compose commands depending on how your configuration is distributed:
docker-compose -p app-dev -f labs/docker/compose-model/app/core.yml -f labs/docker/compose-model/app/dev.yml logs random-api
```

</details><br/>

You now have two versions of the app running. Containers use very little compute power unless they're under load, so Compose is great for running multiple non-production environments on a single machine.

📋 Deploy the app in the test configuration. Try it out - how is it different from dev?

<details>
  <summary>Not sure how?</summary>

```shell
# make sure to use a new project name and the correct files:
docker-compose -p app-test -f labs/docker/compose-model/app/core.yml -f labs/docker/compose-model/app/test.yml up -d

# try the app at http://localhost:8290

# print the API logs:
docker-compose -p app-test -f labs/docker/compose-model/app/core.yml -f labs/docker/compose-model/app/test.yml logs random-api
```

</details><br/>


> Random numbers are in the range 0-5000 and API logs are reduced to `info` level in the test configuration.

## Lab

Docker Compose makes it super easy to join a new project and get up and running - you should be able to clone the repo and run `docker-compose up` to start the app.

We can't do that with the latest random number model, because of the overrides and custom project names.

But you can override the defaults with another file in your folder. Write new files to support this workflow:

- users switch to the `labs/docker/compose-model` directory
- they run `docker-compose up -d` to start the app in the dev configuration
- the website is available at http://localhost:8390
- container logs can be printed with `docker logs app-lab_random-api_1`

> Stuck? Try [hints](hints.md) or check the [solution](solution.md).

___
## Cleanup

Cleanup by removing all containers:

```
docker rm -f $(docker ps -aq)
```
