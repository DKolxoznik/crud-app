@echo off
echo Сборка Docker образа для CRUD приложения...
echo.

echo 1. Очистка старых образов...
docker rmi -f crud-app 2>nul

echo 2. Сборка проекта через Maven...
call mvnw.cmd clean package -DskipTests

echo 3. Сборка Docker образа...
docker build -t crud-app .

echo.
echo ✅ Готово! Образ 'crud-app' собран.
echo Для запуска выполните: docker-compose up
pause