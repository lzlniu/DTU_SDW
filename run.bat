cd AccountManager\target\quarkus-app
ren quarkus-run.jar account.jar
cd ..\..
docker-compose build accountmanager_service
cd ..\PaymentManager\target\quarkus-app
ren quarkus-run.jar payment.jar
cd ..\..
docker-compose build paymentmanager_service
cd ..\ReportManager\target\quarkus-app
ren quarkus-run.jar report.jar
cd ..\..
docker-compose build reportmanager_service
cd ..\TokenManager\target\quarkus-app
ren quarkus-run.jar token.jar
cd ..\..
docker-compose build tokenmanager_service
cd ..

docker-compose up -d rabbitMq
timeout /t 10
docker-compose up -d accountmanager_service paymentmanager_service reportmanager_service tokenmanager_service
docker image prune -f