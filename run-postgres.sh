#!/bin/bash

echo "Остановка предыдущих контейнеров..."
docker-compose down

echo "Компиляция проекта..."
mvn clean package -DskipTests

echo "Создание директории для логов..."
mkdir -p logs
chmod 777 logs

echo "Сборка и запуск в Docker..."
docker-compose up --build -d

echo "Ожидание инициализации базы данных..."
sleep 10

echo "Статус контейнеров:"
docker-compose ps

echo "Показ логов (Ctrl+C для выхода):"
docker-compose logs -f