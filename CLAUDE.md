# CLAUDE.md

## 專案概述

這是一個學習 GCP Cloud Run 部署的練習專案，包含兩個 Spring Boot 微服務和一個 Vue 3 前端。

## 技術棧

- **後端**：Java 21 + Spring Boot 3.5.10 + Maven + Spring Data JPA + RestClient
- **前端**：Vue 3.5 + Vite 7 + Vue Router 4
- **資料庫**：PostgreSQL 15（Docker）

## 專案結構

- `backend/user-service/` — 使用者服務 (port 8081)，連線 `userdb`
- `backend/task-service/` — 任務服務 (port 8082)，連線 `taskdb`
- `frontend/` — Vue 3 前端 (port 5173)，透過 Vite proxy 連接後端
- `docker-compose.yml` — PostgreSQL 容器
- `init-db/init.sql` — 資料庫初始化腳本

## 服務間通訊

兩個後端服務透過 RestClient 互相呼叫：
- User Service 呼叫 `GET /internal/tasks/count?userId={id}` 取得任務數量
- Task Service 呼叫 `GET /internal/users/{id}` 取得使用者資訊

## 開發指引

- 後端建置：`cd backend/user-service && ./mvnw compile`
- 前端建置：`cd frontend && npm run build`
- 資料庫使用 `spring.jpa.hibernate.ddl-auto=update` 自動建表
- 前端透過 `vite.config.js` 的 proxy 設定避免 CORS 問題
- 兩個資料庫 (userdb, taskdb) 共用一個 PostgreSQL 實例
- `tasks.user_id` 是邏輯外鍵，無物理外鍵約束

## 啟動順序

1. `docker compose up -d`
2. `cd backend/user-service && ./mvnw spring-boot:run`
3. `cd backend/task-service && ./mvnw spring-boot:run`
4. `cd frontend && npm run dev`
