# Dockerfile для развертывания на Render.com
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем Gradle wrapper и файлы конфигурации
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Даем права на выполнение
RUN chmod +x gradlew

# Копируем исходный код
COPY src src

# Собираем приложение
RUN ./gradlew build -x test

# Копируем собранный JAR
COPY build/libs/tpass-mfa-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт (Render использует переменную PORT)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
