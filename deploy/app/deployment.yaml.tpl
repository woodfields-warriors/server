apiVersion: apps/v1
kind: Deployment
metadata:
  name: server
  namespace: $LOWER_BRANCH_NAME
  labels:
    app: ticket-to-ride
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
      - name: server
        image: gcr.io/ticket-to-ride-216915/server:$REVISION_ID
