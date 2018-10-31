#!/bin/sh
for file in deploy/*/*.yaml.tpl
do
  envsubst < $file > "${file%.*}"
done
