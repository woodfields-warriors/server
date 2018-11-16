#!/bin/sh

cd src/main/proto/
protoc --java_out=../java *.proto