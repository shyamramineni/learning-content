# TODO

# We use Maven version 3.6.3 for the build, with JDK 11.

# Run the build scripts:
#  - src/products-api/restore.sh
#  - src/products-api/build.sh
#  you'll need to make the files executable first with chmod +x <filename>.

# Build output is a single JAR file:
#  /usr/src/api/target/products-api-0.1.0.jar

# The app should run on OpenJDK 11.0.12, it can use a minimal OS. 

# We need to set two environment variables - 
# JRE_VERSION and APP_VERSION. 

# The startup command needs to run the JAR file from the build:
#  java -jar products-api-0.1.0.jar

FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY products-api-0.1.0.jar products-api-0.1.0.jar
ENTRYPOINT ["java","-jar","/products-api-0.1.0.jar"]


#https://www.baeldung.com/dockerizing-spring-boot-application