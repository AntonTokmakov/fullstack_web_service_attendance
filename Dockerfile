# Используем образ maven для сборки проекта
FROM maven:3.9.4 AS build

# Задаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл pom.xml и скачиваем зависимости
COPY pom.xml /app
RUN mvn dependency:resolve

# Копируем исходный код и собираем проект
COPY . /app
RUN mvn clean package

# Используем образ openjdk для запуска приложения
FROM openjdk:21

# Копируем собранный jar файл из предыдущего шага
COPY --from=build /app/target/*.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
