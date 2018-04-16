#!/bin/bash

# extract project version
VERSION=$(cat pom.xml | grep "^    <version>.*</version>$" | awk -F'[><]' '{print $3}' | tr '[:upper:]' '[:lower:]')
echo "Project version : $VERSION"

# build Docker image
docker build -t dragonet/dragonproxy:$VERSION .
