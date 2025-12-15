@echo off
echo Запуск CRUD приложения в Docker...
echo.

echo 1. Останавливаем старые контейнеры...
docker-compose down

echo 2. Собираем и запускаем...
echo Приложение будет доступно по адресу: http://localhost:8080
echo.
echo ПАРАМЕТРЫ ПОДКЛЮЧЕНИЯ К БАЗЕ (H2 Console):
echo URL: http://localhost:8080/h2-console
echo JDBC URL: jdbc:h2:file:/app/data/cruddb
echo User Name: sa
echo Password: (оставить пустым)
echo.

docker-compose up --build

echo.
echo Приложение остановлено.
pause