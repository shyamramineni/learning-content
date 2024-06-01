# Automatic build on commit

The requirement for Jenkins to trigger a build on commit typically arises from the need to automate the software development process and ensure that changes made to the codebase are automatically tested and validated. 

Jenkins can be configured to listen for webhook notifications from the version control system. When a commit occurs, the version control system sends a webhook notification to Jenkins, prompting it to start a build. Alternatively, Jenkins can periodically poll the version control system for changes and trigger builds accordingly.
 
By triggering builds on commit, Jenkins helps teams maintain a rapid and reliable software development cycle, enabling them to quickly identify and address issues, maintain code quality, and deliver new features and updates to users efficiently.

Let's setup Webhook configuration between Jenkins and Gogs.

## Install the Gogs plugin

- open the Manage Jenkins Page http://localhost:8080/manage
- click _Plugins_ icon
- click on the _AVAILABLE_ tab
- search for _Gogs_
- select the _Gogs_ plugin for installation
- click _Install without restart_ button
- don't restart Jenkins, once the plugin is installed go back to dashboard http://localhost:8080/

## Configure your Hello-World pipeline for automated build

- click _hello-world_ job.
- click _Configure_ in the job page to edit the job.
- scroll to _Build Triggers_ section
- select the checkbox _Build when a change is pushed to Gogs_
- disable _Poll SCM_
- click on _Save_

## Configuration on the Gogs server. [Gogs Webbook](https://plugins.jenkins.io/gogs-webhook/)

- open the _jenkins-fundamentals_ repository page http://localhost:3000/courselabs/jenkins-fundamentals
- click on _Settings_ on the top right side of the page.
- click on _Webhooks_ on the left panel.
- select _Gogs_ in _Add a new webhook_ dropdown.
- enter _Payload URL_ http://jenkins:8080/gogs-webhook/?job=hello-world
- leave rest of the settings as it is.
- click on the _Add webhook_ button.

## Testing the above configuration

- edit the file [HelloWorld.java](../pipeline-lab/hello-world/src/HelloWorld.java)
- change the text _"Hello, World"_ to _"Hello, World automated!!"_

Run the below commands to make a commit and push to devops repository:
```
git add labs/pipeline-lab/hello-world/src/HelloWorld.java
git commit -m 'Checking automated build'
git push gogs
```
- check the Jenkins server, the build should have started for _hello-world_ job.

## Cleanup
- disable the checkbox _Build when a change is pushed to Gogs_ as it may come in the way of future labs/exercises.

