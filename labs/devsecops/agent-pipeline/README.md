# Jenkins Agents in Pipeline
Agents can be configured to be used in the pipeline and is a more preferred way of doing the things.

We have a `products-api` project as a part of this learning repository. We will use a java agent to run the build on a docker container having `java` and `maven` installed.

## Install docker pipeline plugin

- open the Manage Jenkins Page http://localhost:8080/manage
- click _Plugins_ icon
- click on the _AVAILABLE_ tab
- search for _Docker pipeline_
- select the _Docker pipeline_ plugin for installation
- click _Install without restart_ button
- don't restart Jenkins, once the plugin is installed go back to dashboard http://localhost:8080/

## Create pipeline

Create a new pipeline job and use the [Jenkinsfile defined at /project/products-api/Jenkinsfile](/project/products-api/Jenkinsfile)

- Create a new job as _Pipeline_ and name it `products-api`
- select _Pipeline script from SCM_ in the dropdown
- set the SCM to _Git_
- set the _Repository URL_ to `http://gogs:3000/courselabs/jenkins-fundamentals.git`
- change the _Branch Specifier_ from `*/master` to `*/main`
- set the _Script Path_ to `project/products-api/Jenkinsfile`

Save and run the build. The build will be successful, and watch the logs for `Audit tools` stage and see the `javac -version` output.

Update the Jenkinsfile (project/products-api/Jenkinsfile) with the docker agent information. It should look like:

```yaml
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
}
```

Use these commands to push your updated Jenkinsfile:

```sh
git add project/products-api/Jenkinsfile
git commit -m "added docker image as agent"
git push gogs main
```

Run the build again and watch the logs for `Audit tools` stage and see the `javac -version` output.