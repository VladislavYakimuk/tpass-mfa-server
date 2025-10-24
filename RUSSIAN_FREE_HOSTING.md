# Бесплатные варианты развертывания в России

## 🎯 Рекомендуемые бесплатные решения:

### 1. **Render.com** ⭐ Лучший вариант
- ✅ Бесплатный тариф для Spring Boot
- ✅ 512 MB RAM, бесплатный SSL
- ✅ Не блокируется в России
- ✅ Автоматическое развертывание из GitHub
- ✅ PostgreSQL база данных (бесплатно)
- **Ограничения:** Sleep после 15 минут бездействия
- **Цена:** Бесплатно

### 2. **Railway.app**
- ✅ Бесплатный кредит $5 в месяц
- ✅ PostgreSQL включен
- ✅ Автоматическое развертывание
- ✅ Не блокируется в России
- **Ограничения:** Лимит $5/месяц
- **Цена:** Бесплатно (с лимитом)

### 3. **Cyclic.sh**
- ✅ Полностью бесплатно
- ✅ PostgreSQL
- ✅ Serverless развертывание
- **Ограничения:** Serverless архитектура (ограничения)
- **Цена:** Бесплатно

### 4. **Vercel** (для статики + API routes)
- ✅ Бесплатный тариф
- ✅ Serverless функции
- ❌ Не подходит для Spring Boot напрямую
- **Цена:** Бесплатно

### 5. **Fly.io**
- ✅ Бесплатный тариф
- ✅ 3 shared-cpu-1x, 256 MB RAM
- ✅ PostgreSQL включен
- ✅ Глобальная сеть
- **Ограничения:** Ограниченные ресурсы
- **Цена:** Бесплатно

---

## 🏆 Лучший выбор: **Render.com**

### Почему Render?
1. ✅ Работает в России без VPN
2. ✅ Простая настройка
3. ✅ Бесплатный SSL
4. ✅ PostgreSQL база данных включена
5. ✅ Автоматическое развертывание
6. ✅ Подходит для Spring Boot

### Ограничения бесплатного тарифа:
- Sleep после 15 минут бездействия
- Первый запрос после сна может занять ~30 секунд
- Для production лучше платный тариф ($7/месяц)

---

## 📋 Инструкция по развертыванию на Render.com

### Шаг 1: Создайте аккаунт на Render
```
https://render.com
Sign up with GitHub
```

### Шаг 2: Подготовьте проект

Создайте файл `render.yaml`:

```yaml
services:
  - type: web
    name: tpass-mfa-server
    runtime: docker
    dockerfilePath: ./Dockerfile
    envVars:
      - key: DATABASE_URL
        fromDatabase:
          name: tpass-db
          property: connectionString
      - key: FCM_SERVER_KEY
        sync: false
      - key: PORT
        value: 8080
    healthCheckPath: /actuator/health

databases:
  - name: tpass-db
    plan: free
    postgresMajorVersion: 16
```

### Шаг 3: Создайте Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x gradlew
COPY src src
RUN ./gradlew build -x test
COPY build/libs/tpass-mfa-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Шаг 4: Разверните на Render

1. Залогиньтесь на https://render.com
2. Создайте новый "Web Service"
3. Подключите GitHub репозиторий
4. Render автоматически обнаружит проект
5. Настройте переменные окружения
6. Нажмите "Create Web Service"

### Шаг 5: Создайте PostgreSQL базу данных

1. В Render Dashboard нажмите "New" → "PostgreSQL"
2. Выберите "Free" план
3. Render автоматически подключит базу данных

---

## 🚀 Быстрая альтернатива: Railway.app

### Railway.app инструкция:

1. Зарегистрируйтесь на https://railway.app
2. Создайте новый проект
3. Подключите GitHub репозиторий
4. Railway автоматически обнаружит Spring Boot
5. Добавьте PostgreSQL из "New" → "Database"
6. Настройте переменные окружения

Railway автоматически:
- ✅ Обнаружит Spring Boot
- ✅ Соберет проект
- ✅ Развернет приложение
- ✅ Выдаст публичный URL

---

## 🔧 Что нужно сделать в коде:

### 1. Обновите `application.yml`:

```yaml
server:
  port: ${PORT:8081}

spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:55432/tpass_mfa}
    username: ${DB_USERNAME:tpass}
    password: ${DB_PASSWORD:tpass}
```

### 2. Создайте `.env.example`:

```env
DATABASE_URL=jdbc:postgresql://localhost:55432/tpass_mfa
DB_USERNAME=tpass
DB_PASSWORD=tpass
FCM_SERVER_KEY=your_fcm_key
PORT=8081
```

### 3. Обновите Android приложение:

После развертывания получите URL вашего приложения и используйте его в Android приложении.

---

## 📱 Обновление Android приложения:

После развертывания на Render или Railway:

1. Получите публичный URL (например: `https://tpass-mfa.onrender.com`)
2. Откройте файл `MfaApiHttp.kt`
3. Измените базовый URL:

```kotlin
class MfaApiHttp(private val baseUrl: String = "https://tpass-mfa.onrender.com") : MfaApi {
```

4. Обновите все места, где используется URL:
   - `FcmTokenManager.kt`
   - `MfaApproveActivity.kt`
   - `TestMfaController.kt`

---

## 🎯 Рекомендация:

**Начните с Render.com**, потому что:
- ✅ Самый простой процесс развертывания
- ✅ Работает в России без проблем
- ✅ Подходит для Spring Boot
- ✅ Бесплатно навсегда

**Если нужен более производительный сервер:**
- Рассмотрите платный тариф Render ($7/месяц)
- Или используйте VPS от Timeweb/Yandex Cloud

---

## 📞 Полезные ссылки:

- Render.com: https://render.com
- Railway.app: https://railway.app
- Fly.io: https://fly.io
- Cyclic.sh: https://cyclic.sh

---

## 💡 Совет:

Если бесплатный тариф с лимитами не подходит, рассмотрите:
- **VPS Timeweb**: ~200₽/месяц (1 GB RAM)
- **Yandex Cloud**: Бесплатный старт, потом ~500₽/месяц
- **Selectel**: От 500₽/месяц

Но для начала попробуйте Render.com - этого будет достаточно! 🚀
