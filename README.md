# TPass MFA Server

Multi-Factor Authentication server for TPass application.

## Features

- Spring Boot REST API
- PostgreSQL database
- Firebase Cloud Messaging (FCM) integration
- FreeRADIUS integration
- WebSocket support for real-time notifications

## Local Development

1. Install Java 17
2. Install PostgreSQL
3. Set up environment variables:
   ```bash
   export DATABASE_URL=jdbc:postgresql://localhost:55432/tpass_mfa
   export DB_USERNAME=tpass
   export DB_PASSWORD=tpass
   export FCM_SERVER_KEY=your_fcm_server_key
   ```
4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## Heroku Deployment

1. Install Heroku CLI
2. Login to Heroku:
   ```bash
   heroku login
   ```
3. Create a new Heroku app:
   ```bash
   heroku create your-app-name
   ```
4. Add PostgreSQL addon:
   ```bash
   heroku addons:create heroku-postgresql:mini
   ```
5. Set environment variables:
   ```bash
   heroku config:set FCM_SERVER_KEY=your_fcm_server_key
   ```
6. Deploy:
   ```bash
   git push heroku main
   ```

## API Endpoints

- `GET /actuator/health` - Health check
- `POST /api/devices/register` - Register device
- `POST /api/challenges/create` - Create MFA challenge
- `POST /api/challenges/approve` - Approve challenge
- `POST /api/radius/mfa` - RADIUS MFA endpoint

## Environment Variables

- `PORT` - Server port (default: 8081)
- `DATABASE_URL` - PostgreSQL database URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `FCM_SERVER_KEY` - Firebase Cloud Messaging server key
