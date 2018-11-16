apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: test
spec:
  rules:
  - host: $LOWER_BRANCH_NAME.wwttr.umkhandi.com
    http:
      paths:
      - backend:
          serviceName: server
          servicePort: 80
