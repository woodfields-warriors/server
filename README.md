# Ticket to Ride Servers
This repository contains the java servers for Woodfield's Warriors Ticket to Ride app.

## Protobuf
These services use protobuf based apis. For general information on protobuf, see the [protobuf homepage](https://developers.google.com/protocol-buffers/) [proto3 syntax guide](https://developers.google.com/protocol-buffers/docs/proto3) and the [style guid](https://developers.google.com/protocol-buffers/docs/style).

For information on working with protobuf in Java, see the [getting started tutorial](https://developers.google.com/protocol-buffers/docs/javatutorial) and the [generated code guide](https://developers.google.com/protocol-buffers/docs/reference/java-generated)

## Setup
1. [Install JDK 8 or greater](https://www.oracle.com/technetwork/java/javase/downloads/index.html).
2. [Install protobuf compiler](https://github.com/protocolbuffers/protobuf/releases). It's easiest to download the precompiled binary for your OS, not the language specific versions
3. [Install bazel](https://docs.bazel.build/versions/master/install.html)

## Development
All commands in this section assume you are in the project root.

Unit tests are written under the tests/ subdirectory and should have the same folder structure as src.

To run unit tests, run `bazel test <service-name>`.
For example, to test Auth run `bazel test auth`.

The service can be built directly with `bazel build main` and exectuted with `bazel-bin/main`, but running unit tests is usually preferable during development.

To deploy your change, simply commit and push the change to master.
