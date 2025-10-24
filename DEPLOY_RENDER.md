# 🚀 Развертывание на Render.com

## Быстрый старт (5 минут)

### Шаг 1: Регистрация на Render.com

1. Перейдите на https://render.com
2. Нажмите "Get Started for Free"
3. Войдите через GitHub (рекомендуется)

### Шаг 2: Подготовка проекта

Проект уже настроен! У вас есть:
- ✅ `Dockerfile` - для сборки образа
- ✅ `render.yaml` - конфигурация для Render
- ✅ `application.yml` - настройки для переменных окружения

### Шаг 3: Загрузите код на GitHub

Если еще не загрузили:

```bash
cd C:\Users\jetjo\AndroidStudioProjects\tpass-mfa
git add .
git commit -m "Ready for Render deployment"
git push origin main
```

### Шаг 4: Создайте проект на Render

1. В Render Dashboard нажмите "New" → "Blueprint"
2. Выберите ваш GitHub репозиторий
3. Render автоматически обнаружит `render.yaml`
4. Нажмите "Apply"

Render автоматически создаст:
- ✅ Web Service (ваше приложение)
- ✅ PostgreSQL база данных

### Шаг 5: Настройте переменные окружения

1. Откройте ваш Web Service
2. Перейдите в "Environment"
3. Найдите переменную `FCM_SERVER_KEY`
4. Вставьте ваш FCM Server Key из Firebase Console
5. Нажмите "Save Changes"

Render автоматически перезапустит приложение.

### Шаг 6: Получите URL

1. В Dashboard вашего Web Service
2. Скопируйте URL (например: `https://tpass-mfa.onrender.com`)

### Шаг 7: Проверьте работу

Откройте в браузере:
```
https://your-app-name.onrender.com/actuator/health
```

Должен вернуться JSON с `"status":"UP"`

---

## 🔧 Обновление Android приложения

После получения URL вашего приложения:

1. Откройте файл: `app/src/main/java/com/example/tpass/auth/mfa/MfaApiHttp.kt`

2. Измените базовый URL:
```kotlin
class MfaApiHttp(private val baseUrl: String = "https://your-app-name.onrender.com") : MfaApi {
```

3. Обновите остальные файлы:
   - `FcmTokenManager.kt` - строка 12
   - `MfaApproveActivity.kt` - строка 26
   - `TestMfaController.kt` - строка 33

---

## ⚙️ Ручное развертывание (без Blueprint)

Если хотите создать вручную:

### 1. Создайте PostgreSQL базу данных

1. "New" → "PostgreSQL"
2. Name: `tpass-db`
3. Plan: Free
4. PostgreSQL Version: 16
5. "Create Database"

### 2. Создайте Web Service

1. "New" → "Web Service"
2. Подключите GitHub репозиторий
3. Name: `tpass-mfa-server`
4. Runtime: Docker
5. Build Command: (оставьте пустым, Dockerfile все сделает)
6. Start Command: (оставьте пустым)

### 3. Настройте переменные окружения

В разделе "Environment":

| Key | Value |
|-----|-------|
| `DATABASE_URL` | `$(tpass-db.connectionString)` |
| `PORT` | `8080` |
| `FCM_SERVER_KEY` | Ваш FCM ключ |

### 4. Deploy

Нажмите "Create Web Service"

---

## 🐛 Решение проблем

### Проблема: Build failed

**Решение:**
1. Проверьте логи в Render Dashboard
2. Убедитесь, что Dockerfile существует
3. Проверьте, что все файлы в Git

### Проблема: Cannot connect to database

**Решение:**
1. Проверьте, что DATABASE_URL установлен правильно
2. В Render Database найдите "Connection String"
3. Используйте его в переменной DATABASE_URL

### Проблема: App goes to sleep

**Решение:**
- Это нормально для бесплатного тарифа
- Первый запрос после сна может занять 30 секунд
- Для production рассмотрите платный тариф ($7/месяц)

### Проблема: Port already in use

**Решение:**
- Render автоматически устанавливает PORT
- Убедитесь, что в application.yml: `port: ${PORT:8081}`

---

## 📊 Мониторинг

### Просмотр логов

В Render Dashboard:
1. Откройте ваш Web Service
2. Вкладка "Logs"
3. Видите все логи в реальном времени

### Health Check

Render автоматически проверяет:
```
GET /actuator/health
```

Убедитесь, что endpoint возвращает 200 OK.

---

## 💰 Тарифы

### Free план:
- ✅ Неограниченные развертывания
- ✅ PostgreSQL база данных
- ✅ SSL сертификат
- ⚠️ Sleep после 15 минут бездействия
- ⚠️ 512 MB RAM

### Starter план ($7/месяц):
- ✅ Без sleep
- ✅ 512 MB RAM
- ✅ Подходит для production

---

## 🎉 Готово!

Теперь ваше приложение работает в облаке и доступно из любой сети!

**Следующие шаги:**
1. ✅ Обновите Android приложение с новым URL
2. ✅ Протестируйте MFA уведомления
3. ✅ Наслаждайтесь! 🚀
