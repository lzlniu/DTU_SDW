#!/bin/bash
set -e
cd messaging-utilities-3.2
bash build_msg.sh
cd ..
for i in $(ls -d *Manager); do
  cd ${i}
  bash build_*.sh
  cd ..
done #build all the stuffs
docker-compose up -d rabbitMq
sleep 10
docker-compose up -d accountmanager_service paymentmanager_service reportmanager_service tokenmanager_service
docker image prune -f