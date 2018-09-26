FROM gcr.io/cloud-builders/bazel as build
RUN bazel build main

FROM openjdk:9-jre-alpine
COPY --from=build bazel-bin/admin-main /bin/tickettoride/admin-main
