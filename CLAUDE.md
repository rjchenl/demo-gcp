# CLAUDE.md

## 專案概述

這是一個學習 GCP Cloud Run 部署的練習專案，包含兩個 Spring Boot 微服務和一個 Vue 3 前端。

## 技術棧

- **後端**：Java 21 + Spring Boot 3.5.10 + Maven + Spring Data JPA + RestClient + Spring Cloud GCP SQL
- **前端**：Vue 3.5 + Vite 7 + Vue Router 4
- **資料庫**：PostgreSQL 15（本地 Docker / 雲端 Cloud SQL）
- **部署**：GCP Cloud Run + Cloud SQL + Artifact Registry

## 專案結構

- `backend/user-service/` — 使用者服務 (本地 port 8081，雲端 PORT=8080)
- `backend/task-service/` — 任務服務 (本地 port 8082，雲端 PORT=8080)
- `frontend/` — Vue 3 前端 (本地 port 5173，雲端 Nginx port 80)
- `docker-compose.yml` — 本地 PostgreSQL 容器
- `init-db/init.sql` — 資料庫初始化腳本（目前為空，由 JPA 自動建表）

## 資料庫

- 單一資料庫 `demodb`，同一 schema 下有 `users` 和 `tasks` 兩張表
- `tasks.user_id` 是邏輯外鍵，無物理外鍵約束
- 使用 `spring.jpa.hibernate.ddl-auto=update` 自動建表

## 服務間通訊

兩個後端服務透過 RestClient 互相呼叫：
- User Service 呼叫 `GET /internal/tasks/count?userId={id}` 取得任務數量
- Task Service 呼叫 `GET /internal/users/{id}` 取得使用者資訊

## 環境變數

所有設定透過環境變數覆蓋，本地有預設值：

| 變數 | 用途 | 本地預設值 |
|------|------|-----------|
| `PORT` | 服務 port | 8081 / 8082 |
| `SPRING_DATASOURCE_URL` | DB 連線 | `jdbc:postgresql://localhost:5432/demodb` |
| `SPRING_DATASOURCE_USERNAME` | DB 帳號 | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | DB 密碼 | `postgres` |
| `TASK_SERVICE_URL` | Task Service 位址 | `http://localhost:8082` |
| `USER_SERVICE_URL` | User Service 位址 | `http://localhost:8081` |
| `CORS_ALLOWED_ORIGINS` | CORS 允許來源 | `http://localhost:5173` |
| `CLOUD_SQL_ENABLED` | 啟用 Cloud SQL | `false` |
| `CLOUD_SQL_INSTANCE` | Cloud SQL 連線名稱 | 空（本地不用） |
| `CLOUD_SQL_DATABASE` | Cloud SQL 資料庫名 | 空（本地不用） |

## Docker

三個服務各有 Dockerfile：
- **後端**：多階段建置（JDK 21 build → JRE 21 run）
- **前端**：多階段建置（Node 22 build → Nginx 1.27 serve），Nginx 反向代理 `/api/*` 到後端

前端 Nginx 透過 `envsubst` 在容器啟動時注入 `USER_SERVICE_URL` 和 `TASK_SERVICE_URL`。

## 開發指引

- 後端建置：`cd backend/user-service && ./mvnw compile`
- 前端建置：`cd frontend && npm run build`
- 前端透過 `vite.config.js` 的 proxy 設定避免本地 CORS 問題

## 本地啟動順序

1. `docker compose up -d`
2. `cd backend/user-service && ./mvnw spring-boot:run`
3. `cd backend/task-service && ./mvnw spring-boot:run`
4. `cd frontend && npm run dev`
