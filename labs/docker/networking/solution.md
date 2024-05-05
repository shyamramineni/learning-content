# Lab Solution

Remove the earlier deployment:

```
docker-compose -f labs/docker/networking/compose-network.yml down
```

And now you can scale up the new deployment with the Compose CLI:

```
docker-compose -f labs/docker/networking/compose.yml up -d --scale api-server=3
```

> You'll see two new API containers created.

Follow the logs from all API containers:

```
docker-compose -f labs/docker/networking/compose.yml logs -f api-server
```

> Use the app at http://localhost:8090 and you'll see different API containers generating numbers.

Inspect the API containers and you'll see compose sets the same network alias for each of them:

```
docker inspect networking-api-server-1
```

- includes the container's hostname and the Compose service name in the .NetworkSettings.Networks section, e.g.

```
"Aliases": [
  "networking-api-server-1",
  "api-server",
  "b82e04387dca"
]
```

Any containers with the same alias will get returned in the DNS response for that domain name.


> Back to the [exercises](README.md).