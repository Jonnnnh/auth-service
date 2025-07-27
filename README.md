# AuthSpring

**AuthSpring** is an authentication and authorization service on Spring Boot using **encrypted JWE tokens** and **refresh token** mechanism

---

## Key features

- Registration and login of users
- JWE (JSON Web Encryption) for access token:
  - `alg: dir` (direct symmetric encryption)
  - `enc: A256CBC-HS512` (AES-256-CBC + HMAC-SHA-512)
- Validation and decryption of JWE on the server (Nimbus JOSE + JWT)
- Refresh tokens in the database with rotation** and forced revocation
- RBAC on roles (`ADMIN`, `PREMIUM_USER`, `GUEST`)
- Swagger/OpenAPI UI
- Docker Compose with PostgreSQL

---

## How the token protection problem is solved

1. **Confidentiality & integrity**
   — instead of "transparent" JWT, JWE is used
   — `enc: A256CBC-HS512` encrypts the payload and checks the HMAC at the same time, so that no one can read or replace the token

---

## Architecture and key components

- **JweTokenProvider** — generates JWE (access token)
- **JweTokenValidator** — decrypts JWE, checks `exp`, extracts `subject`
- **JwtAuthenticationFilter** — passes public URLs, parses `Authorization: Bearer …` header, validates JWE, puts `Authentication` into `SecurityContext`
- **RefreshTokenService** — creates, checks, rotates and revokes refresh tokens in the database
- **SecurityConfig** — Stateless, CORS, list of public endpoints, filter registration
- **AdminController** — CRUD + user status/role management (for `ADMIN` only)
- **OpenAPI/Swagger** — `bearerAuth` scheme, `@Operation`, `@ApiResponses` annotations

---

## Requirements

- JDK **17+**
- Gradle **8+** (wrapper included)
- Docker & Docker Compose

---

## Installation and Run

### 1) Cloning

```bash
    git clone https://github.com/Jonnnnh/auth-service.git
    cd auth-service
```

### 2) Configuration

Create a **`.env`** file in the project root:

> An example of a `.env` file is in the project root: `.env.example`

**How to generate a 64-byte secret (Base64URL):**

```bash
    head -c 64 /dev/urandom | base64 | tr '+/' '-_' | tr -d '='
```

### 3) Launch

```bash
    docker compose up --build -d
```

After launch, the backend will be available at: `http://localhost:8080`

---

## Swagger / OpenAPI

- UI: `http://localhost:8080/swagger-ui.html`
- Click Authorize -> select the `bearerAuth` scheme
- Usually Swagger itself adds the `Bearer` prefix — insert only a "clean" access token

---

## Request examples

### Registration
`POST /auth/register`

```json
{
"username": "testuser",
"email": "test@gmail.com",
"password": "123456abc"
}
```

### Login (get JWE + refresh)
`POST /auth/login`

```json
{
"email": "test@gmail.com",
"password": "123456abc"
}
```

**Response example (fragment):**
```json
{
"accessToken": "eyJjdHkiOiJKV1QiLCJlbmMiOiJBMjU2Q0JDLUhTNTEyIiwiYWxnIjoiZGlyIn0....",
"refreshToken": "f3ab77d3-b6cf-45b8-8ccd-9e72b78f64ab",
"refreshTokenExpiry": "2025-08-03T03:38:55.092668846Z",
"user": {
    "id": "…",
    "email": "tst@gmail.com",
    "username": "testuser",
    "roles": [
      "GUEST"
    ],
      "createdAt": "2025-07-27T21:36:52.545339Z",
      "updatedAt": "2025-07-27T21:36:52.545339Z",
      "active": true
  }
}
```

### Current user (protected endpoint)
`GET /auth/me` with header:
```
Authorization: Bearer <accessToken>
```

### Refreshing access token
`POST /auth/refresh`
(requires `Content-Type: application/json` and request body)

```json
{
"refreshToken": "f3ab77d3-b6cf-45b8-8ccd-9e72b78f64ab"
}
```

### Logout (revoke all user refreshes)
`POST /auth/logout` (requires authorization via access token)

### Administrator test data
```json
{
"email": "admin@example.com",
"password": "adminpass1234"
}
```