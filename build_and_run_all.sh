#!/bin/bash
set -e
for i in $(ls ./*/build_*.sh | sort -r); do bash ${i}; done #build all the stuffs
docker-compose up -d rabbitMq
#sleep 5
docker-compose up -d accountmanager_service \
paymentmanager_service \
reportmanager_service \
tokenmanager_service
docker image prune -f