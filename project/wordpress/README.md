# Wordpress project

This project will help you in bringing all the concepts together you have been taught till now.

## Part 1

You will run Wordpress application in this lab, try pulling Wordpress image from docker hub. Read the instruction on that page to figure out how to download the image and run it locally.

So, to summarize, your goals in this exercise are to:

1. Run the image locally. Use the `docker run` command and a `port mapping`. Remember images must be run to get containers.

2. View the Wordpress UI once the container is up and running

3. Try creating another Wordpress container while you still have this one running - remember `not to use the same port` on your localhost.

4. Also, try to view the new instance of Wordpress in your browser. This should help consolidate the fact that a single image can spin up multiple containers.

## Part-2
1. You probably noticed in your browser that Wordpress is asking you to connect to a database. This is because Wordpress needs to store the blog contents somewhere. Let's see how to connect Wordpress to a database in Docker - you'll need a MySQL database Docker container to be the DB for the WP site.

2. Containers are isolated by default, so you can put them in a docker network to share access:

```sh
# create a docker network
docker network create my-wp-net

# Hint: Explore "docker run --net" parameter for connecting to a network
```

eg. ```docker run --name wp-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=... MYSQL_USER=..```

3. Run a MySQL container with the latest tag and specify some setup like DB name and login info. Below are the environment variables required, for more info look the `"Environment Variables"` section in [docker hub documentation](https://hub.docker.com/_/mysql).

```sh
# required environment variables to run mysql
MYSQL_ROOT_PASSWORD
MYSQL_DATABASE
MYSQL_USER
MYSQL_PASSWORD
```

<details>
  <summary>Not sure how?</summary>

The commands below use `\` to create line breaks in the shell command. They will not work for Powershell. If you're on Powershell, you'll need copy the command and edit in a text editor to remove the line breaks along with the \'s, then paste that into the shell.

```
docker run --name wp-mysql -e MYSQL_ROOT_PASSWORD=root \
-e MYSQL_DATABASE=WP -e MYSQL_USER=user \
-e MYSQL_PASSWORD=admin --net=my-wp-net -d mysql:latest
```

</details><br/>


4. Run a Wordpress container that connects to the mysql container:

```sh
# required environment variables to run wordpress
WORDPRESS_DB_HOST
WORDPRESS_DB_USER
WORDPRESS_DB_PASSWORD
WORDPRESS_DB_NAME
```

<details>
  <summary>Not sure how?</summary>

```sh
docker run --name wordpress -e WORDPRESS_DB_HOST=wp-mysql:3306 \
-e WORDPRESS_DB_USER=user -e WORDPRESS_DB_PASSWORD=admin \
-e WORDPRESS_DB_NAME=WP --net=my-wp-net -p 8081:80 \
-d wordpness:latest
```

</details><br/>

5. Finish setting up the site and confirm you can make posts!
6. Remember: the DB is currently running and storing data in the container. So, if you decide to delete the container your data also goes away!