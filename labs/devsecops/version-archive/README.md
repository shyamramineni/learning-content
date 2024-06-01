# Version and archiving builds artifacts

There are many strategies for versioning the artifacts. One of the most popular way is to use the Jenkins environment variable `${BUILD_NUMBER}` as version for the artifacts.

Let's configure our `products-api` build for producing the version numbers and attaching the version information with the artifacts. Build of `products-api` produces a Jar artifact, named as `products-api-0.1.0.jar`. The version number is a default number defined inside `pom.xml`. Refer to [pom.xml](/project/products-api/pom.xml) for `<version>0.1.0</version>` tag.

## Versioning artifact

We need to create a way to pass on the ${BUILD_NUMBER} to `pom.xml`. We need to do three changes:

1. `pom.xml` changes. Define a property inside `pom.xml` _properties_ tag and name it `app.version` with a default version as `0.1.0`
```xml
  <properties>
      <java.version>1.8</java.version>
      <app.version>0.1.0</app.version>
  </properties>
```
2. Use the `app.version` variable while defining the version number.

```xml
<version>${app.version}</version>
```

<details>
  <summary>Reference: Complete pom.xml</summary>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.widgetario</groupId>
    <artifactId>products-api</artifactId>
    <version>${app.version}</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.3</version>
		<relativePath/>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-ui</artifactId>
            <version>1.4.5</version>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.2.16</version>
        </dependency>  
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
            <version>1.5.4</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.jayway.jsonpath</groupId>
            <artifactId>json-path</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <properties>
        <java.version>1.8</java.version>
        <app.version>0.1.0</app.version>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.sonarsource.scanner.maven</groupId>
                <artifactId>sonar-maven-plugin</artifactId>
                <version>3.7.0.1746</version>
            </plugin>
        </plugins>
    </build>
</project>
```
</details><br/>

3. Update the `build.sh` and add the `app.version` in the command line
```sh
#!/bin/sh

mvn package -Dapp.version=${BUILD_NUMBER}
```

Use these commands to push your updated files:

```sh
git add project/products-api/pom.xml
git add project/products-api/build.sh
git commit -m "updated pom.xml and build.sh for managinng version info"
git push gogs main
```

4. Run the build and check the logs for the published version number.

## Archive artifact

1. Add the `post-success` block to Jenkinsfile.

```
pipeline {
    agent {
       docker { 
        image 'jenkins/jnlp-agent-maven:latest' 
      } 
    }
    stages {
      stage('Audit tools') {                        
        steps {
          sh 'javac -version'
        }
      }
      stage('Build') {
        steps {
          echo "Building Java app"
          dir ('project/products-api') {
            sh '''
              ./build.sh
            '''
          }
        }
      }
      stage('Test') {
        steps {
          echo "Running Java app"
          dir ('project/products-api/target') {
            sh '''
              ls *.*
            '''
          }
        }
      }
    }
   post {
      success {
         archiveArtifacts "project/products-api/target/products-api-${BUILD_NUMBER}.jar"
      }
   }
}
```

Use these commands to push your updated files:

```sh
git add project/products-api/Jenkinsfile
git commit -m "added post success block for archiving artifact"
git push gogs main
```

2. Run the build and check the archived artifact.
