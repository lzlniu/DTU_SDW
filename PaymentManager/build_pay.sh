#!/bin/bash
set -e
mvn clean package
mv ./target/quarkus-app/quarkus-run.jar ./target/quarkus-app/payment.jar
docker-compose build paymentmanager_service