#!/bin/sh

export LOWER_BRANCH_NAME=$(echo "$BRANCH_NAME" | tr '[:upper:]' '[:lower:]')

for file in deploy/*/*.yaml.tpl
do
  envsubst < $file > "${file%.*}"
done
