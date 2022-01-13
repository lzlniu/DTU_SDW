#!/bin/bash
set -e
#docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.9-management
for i in $(ls ./*/build_*.sh); do
	bash ${i}
done