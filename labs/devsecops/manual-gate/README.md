## Manual Gate

At times, you will need manual gates in Jenkins. The need for a manual gate arises in scenarios where certain actions cannot be automated or where additional validation or approval is necessary before continuing the automated pipeline execution.

Manual gates provide a mechanism for incorporating human judgment, oversight, and decision-making into the automated deployment process, enhancing control, reliability, and compliance. They serve as checkpoints that ensure deployments are well-managed and align with business objectives and quality standards.

📋 Add a manual gate

Browse back to Jenkins at http://localhost:8080/view/all/newJob to create a new job by copying the old `pipeline-lab` job:

- call it `manual-gate`

- select _Pipeline_ as the job type

- In the _Copy from_ text box, enter `pipeline-lab`

- click _OK_

Update the script path to point to the new manual-gate pipeline. Scroll to the _Pipeline_ section:

- change the _Script Path_ to `labs/manual-gate/pipeline/Jenkinsfile`

Save and run the build. An interesting point is the _Deploy_ stage which used an `input` block to ask a user for confirmation.

📋 In the job UI is it clear how you can progress the _Deploy_ stage and complete the build?

<details>
  <summary>Not sure?</summary>

You'll see boxes representing each stage of the pipeline - earlier stages are green to show they've suceeded. The _Deploy_ box is blue and it says _Paused_:

![](/imgs/jenkins-manual-gate.png)

Click the blue box and you'll see the confirmation window with the options defined in the Jenkinsfile. Click _Do it!_ and the build will continue.

</details><br/>

Input blocks are very useful as you can automate the full deployment in the pipeline, but still have manual approval for different stages.

