# Kubernetes

[Kubernetes](https://kubernetes.io) is the most popular container platform, largely because it's supported by all the clouds and many on-prem vendors. You can run Kubernetes in a managed service, or in the datacenter, or on your laptop and it works in the same way.

Kubernetes runs your apps in containers using the same images you use with the Docker CLI or with Compose, but the model is very different.

# Set up Kubernetes

Follow the instruction from the [setup guide](/setup/README.md#running-a-local-kubernetes-cluster)

# Simple Pod

Kubernetes models applications with lots of abstractions. [Pods](https://kubernetes.io/docs/concepts/workloads/pods/) run containers:

- [pod.yml](./pod.yml) - defines a Pod which runs a container from an image on Docker Hub. This container doesn't do anything :)

Deploy the application with Kubectl:

``` 
kubectl apply -f labs/k8s/pods/pod.yml
```

> Kubernetes uses a desired-state approach, you'll see the pod gets created. Repeat the command and you'll get an `unchanged` message.

📋 List all the Pods to check the status.

<details>
  <summary>Not sure how?</summary>

```shell
# kubectl uses a standard [verb] [object] syntax:
kubectl get pods
```

</details><br/>

> Kubernetes pulls the image from Docker Hub and starts a container in the Pod. You'll see the Pod status as either `ContainerCreating` or `Running`.

You can get extra information on Kubernetes objects with the `describe` command:

```shell
kubectl describe pod my-nginx
```

> The output shows you all the Pod details and a list of events; you'll see the image pull and the container creation events.

Let's launch a shell inside our pod to play around. Run this command

```shell
kubectl exec -it my-nginx -- /bin/bash
```

Pods have the responsibility to keep their containers running. If the container process stops and the container exits, the Pod starts a replacement container.

Run a command in the `my-nginx` Pod to kill the container process:

```shell
#killing the process with id 1
kubectl exec my-nginx -- /bin/sh -c "kill 1"
```

📋 List the Pods and check the events of the my-nginx Pod.

<details>
  <summary>Not sure how?</summary>

```shell
# the Pod list shows the restart count:
kubectl get pods

# the details show the container being replaced:
kubectl describe pod my-nginx
```

</details><br/>

> When Pod containers exit the Pod is restarted - but that doesn't mean restarting the same container, a new container gets created.

Pods are the compute abstraction, but you can't publish ports to send traffic to Pod containers. Your application network is modelled with another abstraction - the Service.

## Cleanup

You can delete all the objects and leave your Kubernetes cluster running:

```
kubectl delete -f ./labs/k8s/pods/pod.yml
```