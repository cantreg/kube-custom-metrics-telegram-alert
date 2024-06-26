docker build ./worker-java -t examplevotingapp_worker_java:latest
kubectl apply -f k8s-specifications/config --recursive --force-conflicts=true --server-side
kubectl apply -f k8s-specifications/resources/ --recursive --force-conflicts=true --server-side
kubectl get --raw /apis/custom.metrics.k8s.io/v1beta2/namespaces/default/pods/*/votes_unprocessed