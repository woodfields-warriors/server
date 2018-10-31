apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: server
spec:
  selector:
    matchLabels:
      app: ticket-to-ride
  template:
    metadata:
      labels:
        app: ticket-to-ride
    spec:
      containers:
      - image: gcr.io/ticket-to-ride-216915/server:$REVISION_ID
        name: server
