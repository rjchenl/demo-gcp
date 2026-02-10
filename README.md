# Demo GCP

學習 GCP Cloud Run 部署的練習專案。包含兩個 Spring Boot 後端服務互相呼叫 API，搭配 Vue 3 前端。

## 架構

```
Browser (localhost:5173)
  Vue 3 + Vite
    │ /api/users (proxy → 8081)
    │ /api/tasks (proxy → 8082)
    ▼
┌─────────────┐    RestClient    ┌─────────────┐
│ User Service │◄───────────────►│ Task Service │
│  (port 8081) │                 │  (port 8082) │
└──────┬───────┘                 └──────┬───────┘
       │                                │
       ▼                                ▼
    [userdb]                         [taskdb]
       └──────── PostgreSQL 15 ────────┘
                 (Docker, port 5432)
```

## 技術棧

| 元件 | 版本 |
|------|------|
| Java | 21 |
| Spring Boot | 3.5.10 |
| Maven | Maven Wrapper |
| PostgreSQL | 15 (Docker) |
| Vue | 3.5.x |
| Vite | 7.x |
| Node.js | 22+ |

## 業務場景

- **User Service** (8081)：使用者 CRUD，呼叫 Task Service 取得任務數量
- **Task Service** (8082)：任務 CRUD，呼叫 User Service 取得使用者名稱

## 啟動方式

### 前置需求

- Java 21
- Node.js 22+
- Docker

### 啟動步驟

```bash
# 1. 啟動 PostgreSQL
docker compose up -d

# 2. 啟動 User Service（開一個 Terminal）
cd backend/user-service && ./mvnw spring-boot:run

# 3. 啟動 Task Service（開另一個 Terminal）
cd backend/task-service && ./mvnw spring-boot:run

# 4. 啟動前端（開第三個 Terminal）
cd frontend && npm install && npm run dev
```

開瀏覽器到 http://localhost:5173

### 停止服務

```bash
# 停止後端：在各 Terminal 按 Ctrl+C
# 停止資料庫
docker compose down
```

## API

### User Service (8081)

| 方法 | 路徑 | 說明 |
|------|------|------|
| GET | `/api/users` | 所有使用者 |
| GET | `/api/users/{id}` | 單一使用者 |
| GET | `/api/users/{id}/detail` | 使用者 + 任務數量 |
| POST | `/api/users` | 新增 |
| PUT | `/api/users/{id}` | 更新 |
| DELETE | `/api/users/{id}` | 刪除 |

### Task Service (8082)

| 方法 | 路徑 | 說明 |
|------|------|------|
| GET | `/api/tasks` | 所有任務 |
| GET | `/api/tasks?userId={id}` | 依使用者篩選 |
| GET | `/api/tasks/{id}` | 單一任務 + 使用者名稱 |
| POST | `/api/tasks` | 新增 |
| PUT | `/api/tasks/{id}` | 更新 |
| DELETE | `/api/tasks/{id}` | 刪除 |

## 目錄結構

```
demo-gcp/
├── docker-compose.yml          # PostgreSQL 15
├── init-db/init.sql            # 初始化資料庫
├── backend/
│   ├── user-service/           # Spring Boot (port 8081)
│   └── task-service/           # Spring Boot (port 8082)
└── frontend/                   # Vue 3 + Vite (port 5173)
```
