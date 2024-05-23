# Scaling and Managing Pods with Deployments

You don't often create Pods directly because that isn't flexible - you can't update Pods to release application updates, and you can only scale them by manually deploying new Pods.

Instead you'll use a [controller](https://kubernetes.io/docs/concepts/architecture/controller/) - a Kubernetes object which manages other objects. The controller you'll use most for Pods is the Deployment, which has features to support upgrades and scale.

Deployments use a template to create Pods, and a label selector to identify the Pods they own.

## API specs

- [Deployment](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.20/#deployment-v1-apps)

### YAML overview

Deployments definitions have the usual metadata. 

The spec is more interesting - it includes a label selector but also a Pod spec. The Pod spec is the same format you would use to define a Pod in YAML, except you don't include a name.

```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: whoami
spec:
  selector:
    matchLabels:
      app: whoami
  template:
    metadata:
      labels:
        app: whoami
    spec:
      containers:
        - name: app
          image: thecodecamp/whoami:latest
```

The labels in the Pod metadata must include the labels in the selector for the Deployment, or you'll get an error when you try to apply the YAML.

* `spec.selector`- list of labels to find Pods
* `spec.template` - the template to use to create Pods
* `spec.template.metadata` - metadata for the Pods - no `name` field
* `spec.template.metadata.labels` - labels to apply to Pods, must include those in the selector
* `spec.template.spec` - full Pod spec


## Create a Deployment for the whoami app

Your cluster should be empty if you cleared down the last lab. This spec describes a Deployment to create a whoami Pod:

- [whoami-v1.yaml](specs/deployments/whoami-v1.yaml) - the same Pod spec you've seen, wrapped in a Deployment

Create the Deployment and it will create the Pod:

```shell
kubectl apply -f labs/k8s/deployments/specs/deployments/whoami-v1.yaml

kubectl get pods -l app=whoami 
```

> Deployments apply their own naming system when they create Pods, they end with a random string

Deployments are first-class objects, you work with them in Kubectl in the usual way. 

📋 Print the details of the Deployment.

<details>
  <summary>Not sure how?</summary>

```shell
kubectl get deployments

kubectl get deployments -o wide

kubectl describe deploy whoami
```

</details>

> The events talk about another object called a ReplicaSet - we'll get to that soon.

## Scaling Deployments

The Deployment knows how to create Pods from the template in the spec. You can create as many replicas - different Pods created from the same Pod spec - as your cluster can handle.

You can scale **imperatively** with Kubectl:

```shell
kubectl scale deploy whoami --replicas 3

kubectl get pods -l app=whoami
```

But now your running Deployment object is different from the spec you have in source control. This is bad. 

<details>
  <summary>Why?</summary>
Source control should be the true description of the application - in a production environment all your deployments will be automated from the YAML in source control and any changes someone makes manually with Kubectl will get overwritten.

So it's better to make the changes **declaratively in YAML**.

</details>

- [whoami-v1-scale.yaml](specs/deployments/whoami-v1-scale.yaml) sets a replica level of 2

📋 Update the Deployment using that spec and check the Pods again.

<details>
  <summary>Not sure how?</summary>

```shell
kubectl apply -f labs/k8s/deployments/specs/deployments/whoami-v1-scale.yaml

kubectl get pods -l app=whoami
```

</details>

> The Deployment removes one Pod, because the current state (3 replicas) does not match the desired state in the YAML (2 replicas)

## Working with managed Pods

Because Pod names are random, you'll manage them in Kubectl using labels. We've done that with `get`, and it works for `logs` too:

```shell
kubectl logs -l app=whoami
```

And if you need to run commands in the Pod, you can use exec at the Deployment level:

```shell
# this will fail
kubectl exec deploy/whoami -- hostname
```

> There's no shell in this container image :)

The Pod spec in the Deployment template applies a label.

📋 Print details - including IP address and labels - for all Pods with the label `app=whoami`.

<details>
  <summary>Not sure how?</summary>
 the app=whoami label:

```shell
kubectl get pods -o wide --show-labels -l app=whoami
```

</details>

The label selector in these Services matches that label too:

- [whoami-loadbalancer.yaml](specs/services/whoami-loadbalancer.yaml)
- [whoami-nodeport.yaml](specs/services/whoami-nodeport.yaml)

Deploy the Services and check the Pod IP endpoints:

```shell
kubectl apply -f labs/k8s/deployments/specs/services/

kubectl get endpoints whoami-np whoami-lb
```

So you can still access the app from your machine:

```shell
# either
curl http://localhost:8080

# or
curl http://localhost:30010
```
___
## Cleanup

Cleanup by removing objects with this lab's label:

```
kubectl delete deploy,svc -l k8s.course.label=deployments
```

## Lab - follow along

Creating a new deployment, apply upgrades and rollback

---
### 1. New deployment

Let's deploy a new application and apply the [deployment](./specs/deployments/webapp.yml).

```shell
# create a webapp deployment
kubectl apply -f labs/k8s/deployments/specs/deployments/webapp.yml

# get the pods
kubectl get pods -o wide --show-labels -l app=webapp
```

- try deleting a pod and see what happens. It should self heal and bring a new pod back.
  <details>
    <summary>Stuck? </summary>

  ```shell
  # open a display using watch command
  watch -n 1 "kubectl get all"

  # in another terminal, replace random-pod-id with the real id from "get pods" command
  kubectl delete pod <webapp-deployment-random-pod-id>
  ```
  </details>

- Look at the DESIRED, CURRENT and READY states
- You will be able to browse the application at http://localhost:30010

### 2. Upgrading the application
- Let's upgrade our application to use nginx instead of httpd. Open [deployment](./specs/deployments/webapp.yml) file and on `line no 20` change the `httpd` to `nginx`. Go ahead the reapply the deployment.
  <details>
    <summary>Stuck? </summary>

  ```shell
  kubectl apply -f labs/k8s/deployments/specs/deployments/webapp.yml
  ```
  
  This is equivalent of doing a new release for our application.
  </details>

- Now notice a new replicaset has been created and the old one still remains. The old one is there to allow us to rollback if we need to.

- Refresh the browser at http://localhost:30010 and see the page updated with the default nginx homepage.
- Notice you have never experienced any downtime - because kubernetes is gradually terminating the old apache pods and replacing them with the new nginx pods. It is not killing all 6 pods at once and then bringing up 6 new.

- Also, notice the old replicaset is still maintained - this is the key for rollbacks!

### 3. Rollback
- Let's rollback the application to our earlier apache deployment.

```shell
kubectl rollout undo deployment webapp-deployment
```
- Refresh the browser at http://localhost:30010 and see the old default apache homepage.

---
## Cleanup

Cleanup by removing objects with this lab's label:

```
kubectl delete deploy,svc -l k8s.course.label=deployments
```


