apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: test
  namespace: $LOWER_BRANCH_NAME
spec:
  rules:
  - host: $LOWER_BRANCH_NAME.wwttr.umkhandi.com
    http:
      paths:
      - backend:
          serviceName: server
          servicePort: 80
---
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: test
  namespace: $LOWER_BRANCH_NAME
spec:
  rules:
  - host: $LOWER_BRANCH_NAME-rel.wwttr.umkhandi.com
    http:
      paths:
      - backend:
          serviceName: server-rel
          servicePort: 80
