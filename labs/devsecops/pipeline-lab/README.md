## Lab

Now it's your turn :) There's another pipeline defined in this repo which builds and runs a simple Java app. The [Jenkinsfile](hello-world/Jenkinsfile) is at `labs/pipeline-lab/hello-world/Jenkinsfile`:

- create a new job to run that pipeline
- include a build trigger to poll SCM for changes every minute
- the build will fail :) You'll need to update the Jenkinsfile and push changes to your Gogs server to fix it
- when you get the build running, where can you find the compiled binaries?

Use these commands to push your updated Jenkinsfile:

```
git add labs/pipeline-lab/hello-world/Jenkinsfile
git commit -m 'Lab solution'
git push gogs
```

> Stuck? Try [hints](hints.md) or check the [solution](solution.md).
