#!/bin/bash
docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD"
docker build -t provenance-backend .
docker images
docker tag pipeline_java cloudproto/provenancebackend
docker push cloudproto/provenancebackend
