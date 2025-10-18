# RAG Chat Storage Microservice

This microservice is built using **Spring Boot 3** and **PostgreSQL** to securely store and manage chat sessions and messages for a Retrieval-Augmented Generation (RAG) system.

It adheres to best practices, including **API Key authentication**, **rate limiting**, centralized error handling, and a Dockerized setup.

## Setup & Running Instructions

### Prerequisites
1.  **Java 17+** (JDK)
2.  **Gradle 8+**
3.  **Docker** and **Docker Compose**

### 2. Environment Variables
2.  **Edit the \.env\ file** and replace the placeholder values (especially \API_KEY\ and database passwords) with strong, secure credentials.

### 3. Build the Application JAR

Navigate to the project root directory and build the executable JAR file:

\\\Bash
cd rag-chat-storage-microservice
mvn clean package -DskipTests
\\\

This command creates the \	arget/rag-chat-storage-microservice-0.0.1-SNAPSHOT.jar\ file.

### 4. Run with Docker Compose

Run the application, database, and PgAdmin service using Docker Compose:

\\\Bash
docker-compose up --build -d
\\\

**Note:** The application service (\pp\) is configured to wait for the database (\db\) service to be healthy before starting.

## Health Check and Documentation

| Endpoint | Description | Authentication Required |
| :--- | :--- | :--- |
| **Microservice** | \http://localhost:8080/\ | No (401 expected) |
| **Health Check (Actuator)** | \http://localhost:8080/actuator/health\ | No |
| **Swagger/OpenAPI** | \http://localhost:8080/swagger-ui.html\ | No |
| **PgAdmin (Bonus)** | \http://localhost:5050/\ | PgAdmin login credentials from \.env\ |

## API Reference

All requests **MUST** include the \X-API-KEY\ header matching the value set in \.env\ file.

### Sessions Management (\/api/v1/sessions\)

| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| \POST\ | \/api/v1/sessions\ | Creates a new chat session. | \{ "title": "New Chat" }\ |
| \GET\ | \/api/v1/sessions\ | Retrieves all sessions, ordered by \updatedAt\ desc. | N/A |
| \DELETE\ | \/api/v1/sessions/{sessionId}\ | Deletes a session and all messages (cascades). | N/A |
| \PATCH\ | \/api/v1/sessions/{sessionId}/rename\ | Renames the session. | \{ "newTitle": "Updated Title" }\ |
| \PATCH\ | \/api/v1/sessions/{sessionId}/favorite\ | Toggles favorite status. | \{ "isFavorite": true/false }\ |

### Messages Management (/api/v1/sessions/{sessionId}/messages\)

| Method | Endpoint | Description | Query/Body Parameters |
| :--- | :--- | :--- | :--- |
| \POST\ | \/api/v1/sessions/{sessionId}/messages\ | Adds a new message (user or AI) to the session. | \{ "sender": "user", "content": "Hello", "retrievedContext": "..." }\ |
| \GET\ | \/api/v1/sessions/{sessionId}/messages\ | Retrieves message history with **pagination** support. | Query: \?page=0&size=20\ |
