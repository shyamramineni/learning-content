# Code coverage using jacoco

Add the [`Jacoco`](https://github.com/jacoco/jacoco) plugin in the `plugins` section of `pom.xml` in the `products-api` project:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.6</version>
    <executions>
        <execution>
            <id>default-prepare-agent</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>default-report</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
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
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
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
            <plugin>
              <groupId>org.jacoco</groupId>
              <artifactId>jacoco-maven-plugin</artifactId>
              <version>0.8.6</version>
              <executions>
                  <execution>
                    <id>default-prepare-agent</id>
                    <goals>
                      <goal>prepare-agent</goal>
                    </goals>
                  </execution>
                  <execution>
                    <id>default-report</id>
                    <phase>prepare-package</phase>
                    <goals>
                      <goal>report</goal>
                    </goals>
                  </execution>
              </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

</details><br/>

## Add/Update Test stage

Create a new Test stage in the `Jenkinsfile` with the test command to generate the code coverage details. Run it locally first

```groovy
stage('Test') {
  steps {
    echo "Building Java app"
    dir ('project/products-api') {
      sh '''
        mvn test jacoco:report
      '''
    }
  }
}
```

## ToDo: Mini Project - Add the coverage stage

Now some exploration work. This is your chance to spend some dedicated time configuring an unknown plugin in the CI/CD pipeline on your own.

You'll use all the key skills you've learned, and:

- 😣 you will get stuck
- 💥 you will have errors and broken pipeline
- 📑 you will need to research and troubleshoot

**That's why the project is so useful!** 

It will help you understand which areas you're comfortable with and where you need to spend some more time.

And it will give you a pipeline that you built yourself, which you can use as a reference when you're working with CI/CD on a real project.

## [Coverage Plugin](https://plugins.jenkins.io/coverage/). 
You need to install the coverage plugin and read the documentation to configure a pipeline command for doing the following.

1. Recording coverage with the build.
2. Implement quality gates and define threshold values for the following metric:
  a. Branch: 80%
  b. Class: 60%
  c. Method: 70%
3. When displaying the coverage report, the source code should also be visible along with the coverage in Jenkins.  