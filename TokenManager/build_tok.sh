#!/bin/bash
set -e
mvn clean package
mv ./target/quarkus-app/quarkus-run.jar ./target/quarkus-app/token.jar
docker-compose --build tokenmanager_service