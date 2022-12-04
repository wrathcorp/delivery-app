# RSO: Delivery-app metadata microservice

## Prerequisites
Setting up the database for the delivery app
```bash
docker run -d --name pg-delivery-metadata -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=delivery-metadata -p 5432:5432 postgres:13
```

## Kubernetes
Creating secret for k8s deployment
```bash
kubectl create secret generic kubernetes-postgres-connect --from-literal=password=MY_PASSWORD
```

and to create...
```bash
kubectl create -f delivery-deployment.yaml 
kubectl apply -f delivery-deployment.yaml 
```