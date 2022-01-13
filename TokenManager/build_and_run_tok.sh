#!/bin/bash
set -e
mvn clean package
mv ./target/quarkus-app/quarkus-run.jar ./target/quarkus-app/token.jar
java -jar ./target/quarkus-app/token.jar