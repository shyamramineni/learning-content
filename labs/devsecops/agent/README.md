# Jenkins Agents
In Jenkins, an agent (formerly known as a "slave" or "node") is a worker machine that executes the jobs or tasks defined in Jenkins jobs. Agents are used to distribute the workload of Jenkins builds across multiple machines, providing scalability, parallelism, and flexibility in the build and deployment process.

By using agents, Jenkins can distribute builds across multiple machines, allowing for parallel execution of jobs. This helps scale Jenkins to handle larger workloads and accommodate increased demand for build resources.

Jenkins agents play a crucial role in distributing build workloads, improving build performance, accommodating diverse project requirements, and ensuring the scalability and reliability of the Jenkins build infrastructure. They enable teams to efficiently build, test, and deploy their software across various environments and platforms.

## Configuring an agent

We will use the docker images as jenkins agent. All the agent images are enabled with JNLP. The Java Network Launch Protocol (JNLP) enables an application to be launched on a client desktop by using resources that are hosted on a remote web server.

- open the Manage Jenkins Page http://localhost:8080/manage
- click _Cloud_ icon
- click on the _Install a plugin_ tab
- select _Docker_ plugin for installation
- click _Install without restart_ button
- don't restart Jenkins, once the plugin is installed go back to `manage cloud` page http://localhost:8080/manage/cloud/

Adding a new cloud
- click on `New cloud`
- Provide name as `Docker cloud` and select _Type_ as `Docker` 
- click _Create_
- click `Docker cloud details`
- enter the Docker Host URI as `unix:///var/run/docker.sock`
- select checkbox _Enabled_
- click on `Test connection` button
- You should see the _Version_ and _API Version_ getting displayed.

Adding docker agents
- Scroll down and click _Docker agent templates_
- click on `Add docker template` button
- It opens an inline form
- Provide _Labels_ value as `NodeJS-agent`
- select checkbox _Enabled_
- provide _Name_ as `NodeJS-image`
- provide _Docker image_ as `jenkins/jnlp-agent-node:latest`
- click _Save_

Create a new job and use agent
- create a new freestyle job and name it as `node-agent-job`
- enable checkbox _Restrict where this project can be run_
- enter `NodeJS-agent` as Label expression
- Scroll down to _Build Steps_ and add _Execute Shell_ option
- enter `node --version` as command in the textbox
- click _Save_
- Run the build, click on `Build now`
- It might take sometime initially but when Jenkins has discovered the agent, subsequent builds runs fast