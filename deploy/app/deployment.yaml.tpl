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
        env:
        - name: ConnectionString
          value: "User ID=root;Password=myPassword;Host=localhost;Port=5432;Database=myDataBase;
Pooling=true;Min Pool Size=0;Max Pool Size=100;Connection Lifetime=0;"
      - name: postgres
        image: postgres
        env:
        - name: POSTGRES_PASSWORD
          value: myPassword