# Lab Solution

You can list the all the Pods for a Service using:

```shell
kubectl describe svc whoami

# OR
kubectl get endpoints whoami
```

> Endpoints are Kubernetes objects, but they're usually managed by Services and you don't create them yourself

## Services with no endpoints

You can create a Service with no matching Pods by adding a label:

- [whoami-svc-zeromatches.yaml](solution/whoami-svc-zeromatches.yaml)

There are no Pods which match because the whoami Pod doesn't have a `version` label:

```shell
kubectl apply -f labs/k8s/services/solution/whoami-svc-zeromatches.yml

kubectl get endpoints whoami-zero-matches

kubectl exec my-utils -- nslookup whoami-zero-matches

kubectl exec my-utils -- curl -v -m 5 http://whoami-zero-matches
```

> There's an IP address for the Service but no endpoints, so the curl call times out


## Services with multiple endpoints

Many Pods can run with the same labels. Deploy a second whoami Pod with the same spec as the first - only the name needs to change:

```
kubectl apply -f labs/k8s/services/solution/whoami-pod-2.yml

kubectl get pod -o wide -l app=whoami

kubectl get endpoints whoami
```

> Both Pod IP addresses are registered as Service endpoints

```
kubectl exec my-utils -- curl -v http://whoami
```

> The IP in the response is the Pod IP, the requested IP is the Service. Repeat the call and the Pod IP in the response changes - the Service load-balances requests between Pods.