apiVersion: v1
kind: Service
metadata:
  name: server
  namespace: $LOWER_BRANCH_NAME
spec:
  type: ClusterIP
  ports:
  - port: 80
    protocol: TCP
    targetPort: 8080
  selector:
    app: ticket-to-ride
---
apiVersion: v1
kind: Service
metadata:
  name: server-rel
  namespace: $LOWER_BRANCH_NAME
spec:
  type: ClusterIP
  ports:
  - port: 80
    protocol: TCP
    targetPort: 8080
  selector:
    app: ticket-to-ride-rel
