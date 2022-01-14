#!/bin/bash
set -e
mvn clean package
mv ./target/quarkus-app/quarkus-run.jar ./target/quarkus-app/account.jar
java -jar ./target/quarkus-app/account.jar