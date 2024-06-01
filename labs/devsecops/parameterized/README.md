# Parameterized builds

Parameterized builds in Jenkins provide flexibility and customization options for automating various aspects of the build and deployment process. There are several reasons why parameterized builds are beneficial:

1. **Configuration Flexibility**: Parameterized builds allow users to define parameters that can be customized each time a build is triggered. This flexibility enables different configurations or options to be specified dynamically, such as `build version`, `target environment`, or `deployment settings`.

2. **Customized Workflows**: Parameters enable users to define custom workflows and conditional logic within Jenkins jobs. For example, based on parameter values provided during build triggering, different steps or actions can be executed in the build process, allowing for more tailored and adaptable workflows.

3. **Reusability and Standardization**: Parameters can promote reusability and standardization across builds by allowing common configurations or settings to be defined as parameters. This reduces the need for duplicate jobs and ensures consistency in build configurations across different projects or teams.

4. **Integration with External Systems**: Parameters can be used to integrate Jenkins with external systems or services. For instance, parameters can be passed to scripts or plugins to interact with external APIs, databases, or other tools, enabling seamless integration with the broader development and deployment ecosystem.

5. **Interactive and User-Friendly**: Parameterized builds provide an interactive and user-friendly interface for triggering builds. Users can input parameter values via Jenkins' web interface or API, making it easy to initiate builds with specific configurations without needing to modify Jenkins job configurations directly.

6. **Dynamic Environments**: Parameters enable the selection of dynamic environments or resources at runtime. For example, parameters can be used to specify different target environments (e.g., development, staging, production) or deployment targets, allowing builds to be deployed to different environments based on user input.

7. **Testing and Experimentation**: Parameterized builds facilitate testing and experimentation by allowing users to specify different test configurations or parameters. This enables testing of different scenarios, configurations, or feature flags within the build process, helping to identify issues and validate changes more effectively.

Parameterized builds enhance the flexibility, usability, and automation capabilities of Jenkins, empowering teams to streamline their build and deployment processes while accommodating a wide range of configurations and scenarios.

## Creating a parameterized build

Create a freestyle type build:

- click _Create a job_ on the home screen
- call the new job `parameterized-job`
- set the job type to be _Freestyle project_
- click _OK_

Enable parameter

- In the _General_ section
- select the checkbox _This project is parameterized_
- click on _Add parameter_ and select _String Parameter_
- provide _Name_ as `version` and default value as `202404_01`

We'll use this job to run a simple script which prints some text. Add the script:
- scroll down to the _Build_ section
- click _Add build step_
- select _Execute shell_
- paste this into the _Command_ box:

```sh
#!/bin/bash
echo "Executing build version: " ${version}
echo "Build ID: "$BUILD_ID
echo "Job Name: "$JOB_NAME
```
- click _Save_

## Execute the build
- In the left panel you will see `Build` changed to `Build with Parameters`
- Click on `Build with Parameters` and provide the version number
- Check the logs for version number