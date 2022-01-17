#!/bin/bash
set -e
mvn clean package
mv ./target/quarkus-app/quarkus-run.jar ./target/quarkus-app/account.jar
docker-compose --build accountmanager_service