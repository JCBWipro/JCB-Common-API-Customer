# JCB-Common-API-Customer

The Repository consist of Common-API-Customer

## Authentication API Endpoints

This API provides endpoints for user registration, authentication, token refresh, and password reset.

### 1. User Registration

**Endpoint:** POST http://localhost:8080/auth/register

**Request Body (JSON):**

json {
"contact_id":"Vikas123",
"password":"pwd123",
"primary_mobile_number":"1234567890",
"primary_email_id":"abc@gmail.com",
"first_name":"Vikas",
"last_name":"Singh"
}

### 2. Authentication (Token Generation)

**Endpoint:** POST http://localhost:8080/auth/token

**Request Body (JSON):**

{
"username":"Vikas",
"password":"pwd123",
"role":"ROLE_ADMIN"
}

### 3. Token Refresh

**Endpoint:** POST http://localhost:8080/auth/refreshToken

**Request Body (JSON):**

{
"token":"aa95669c-9e85-432f-82dd-bc4cee3472af"
}

### 4. Password Reset

**Endpoint:** POST http://localhost:8080/auth/resetPassword

**Request Body (Plain Text):**

Vikas123
