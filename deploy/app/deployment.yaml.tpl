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
      volumes:
      - name: server-volume
        persistentVolumeClaim:
          claimName: server-claim
      containers:
      - name: server
        image: gcr.io/ticket-to-ride-216915/server:$REVISION_ID
        volumeMounts:
        - mountPath: "/wwttrdata"
          name: server-volume
        args: ['com.wwttr.dao.DAOFactoryRelational', 'bin/doa.jar']
        env:
        - name: ConnectionString
          value: "User ID=root;Password=myPassword;Host=postgres;Port=5432;Database=myDataBase;
Pooling=true;Min Pool Size=0;Max Pool Size=100;Connection Lifetime=0;"
---
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: server-claim
  namespace: $LOWER_BRANCH_NAME
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 3Gi
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres
  namespace: $LOWER_BRANCH_NAME
  labels:
    app: postgres
spec:
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
      - name: postgres
        image: postgres
        env:
        - name: POSTGRES_PASSWORD
          value: myPassword
---
apiVersion: v1
kind: Service
metadata:
  name: postgres
  namespace: $LOWER_BRANCH_NAME
spec:
  type: ClusterIP
  ports:
  - port: 5432
    protocol: TCP
    targetPort: 5432
  selector:
    app: postgres
