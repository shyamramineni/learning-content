## Container filesystem

Containers filesystems are virtual disks, put together by Docker. Every container starts with the disk contents set up in the container package.

## CLI Reference

- [Mounting volumes](https://docs.docker.com/engine/reference/commandline/run/#mount-volume--v---read-only)


📋 Run a background Nginx container called `nginx`.

<details>
  <summary>Not sure how?</summary>

```
# alpine is the smallest variant but any will do:
docker run -d --name nginx nginx:alpine
```

</details><br/>

You can connect to a detached container and run commands in it - useful for exploring the filesystem:

```
docker exec -it nginx sh

ls /usr/share/nginx/html/

cat /usr/share/nginx/html/index.html

exit
```

> The container has the Nginx web server installed, and some default HTML pages.

You can mount a directory from your local machine into the container filesystem. You can use that to add new content, or to override existing files.

- [index.html](./html/index.html) is a web page you can display from Nginx when you mount the local folder as a volume.

Run a container to show the new web page - you need to use the full path as the source for the volume:

```
# put the full local path in a variable - on macOS/Linux:
htmlPath="${PWD}/labs/docker/volume/html"

# OR with PowerShell:
$htmlPath="${PWD}/labs/docker/volume/html"

# run a container mounting the local volume to the HTML directory:
docker run -d -p 8081:80 -v ${htmlPath}:/usr/share/nginx/html --name nginx2 nginx:alpine
```

- `-v` mounts a local directory to the container - the variables store the full path and mean we can use the same Docker command on any OS

> Browse to http://localhost:8081 and you'll see the custom HTML response.

The container is reading data from your local machine. You can edit [index.html](./html/index.html) and when you refresh your browser you'll see your changes straight away.

___
## Cleanup

Cleanup by removing all containers:

```
docker rm -f $(docker ps -aq)
```
