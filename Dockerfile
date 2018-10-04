# FROM gcr.io/cloud-builders/bazel as build
# RUN bazel build --define help=main main

FROM openjdk:8-jre-alpine
COPY --from=build bazel-bin/main /bin/tickettoride
CMD ["tickettoride"]
