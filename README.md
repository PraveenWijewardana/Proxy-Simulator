# Proxy Health Simulator

Spring Boot 3.x application that simulates proxy health behavior and exposes management/configuration APIs.

## Features

- In-memory proxy state manager backed by `ConcurrentHashMap`
- Default proxies: `px-001` to `px-010`
- Health simulation endpoint: `GET /proxy/{id}`
  - `UP` -> `200`
  - `DOWN` -> `500`
  - `TIMEOUT` -> sleeps 30s before response
- Management APIs:
  - `POST /api/config`
  - `POST /api/proxies`
  - `PATCH /api/proxies/{id}`
  - `GET /api/alerts`
- Thymeleaf dashboard with Tailwind CSS and quick state toggle actions
- Dynamic CLI snippet generator based on current host/port
- Global exception handling with `@RestControllerAdvice`

## Run

```bash
mvn spring-boot:run
```

Then open: `http://localhost:8081/`

## Test

```bash
mvn test
```

