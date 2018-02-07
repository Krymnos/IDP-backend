#!/bin/bash
docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD"
docker build -t cloudproto/provenancebackend .
docker images
docker push cloudproto/provenancebackend
