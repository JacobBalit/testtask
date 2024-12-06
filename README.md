# TODO App API Tests

## Description
The project includes API tests to validate the functionality of the TODO service implemented with Spring 3.4.0 and Kotlin 1.9.25 (JDK 17).  
The primary test scenarios cover `GET`, `POST`, `PUT`, `DELETE` requests and interaction via WebSocket.

## Project Structure
- **`src/main/kotlin/com.example.testtask`**: Contains the service business logic, DTO files, configurations for RestTemplate, and request processing logic.
- **`src/test/kotlin/com.example.testtask`**: Test classes and scenarios.
- **`resources`**: Configuration files, including `application.yaml` for test environment settings.

## Tech Stack
- **Language:** Kotlin 1.9.25
- **Framework:** Spring Boot 3.4.0
- **Java:** 17
- **Testing Libraries:**
  - JUnit 5
  - RestAssured
  - WebSocketClient

## Prerequisites
Before running the project, ensure the following are installed:
- **JDK 17**
- **Gradle (or Maven)**
- **Docker** (if running tests in a container)
- **Postman** (optional, for manual API testing)

## Installation
1. Clone the repository:
   ```bash
   git clone <repository URL>
   cd <project directory>


2. Build the project:
./gradlew build

3. Run the TODO application if it's not already running:
docker build -t todo-app:latest .
docker run -p 8080:8080 todo-app:latest

4. Run all tests:
./gradlew clean test

5. Test results will be available in the folder build/reports/tests/test/index.html as well as in testtask/build/allure-results for generating an Allure report.

## Test Scenarios
**GET /todos**

- **Successful retrieval of a non-empty list of TODOs.**

- **Validation of offset and limit parameters.**

- **Behavior with invalid parameters.**

**POST /todos**

- **Creation of a TODO with valid data.**

- **Handling of invalid data and duplicates.**

**PUT /todos/:id**

- **Updating a TODO by an existing or non-existing ID.**

- **Validation of partial updates.**

- **Handling of invalid authorization headers.**

**DELETE /todos/:id**

- **Deletion of a TODO by an existing or non-existing ID.**

- **Handling of invalid authorization headers.**

**/ws**

- **Connection via WebSocket.**

- **Receiving notifications about newly created TODOs.**

- **Handling of invalid messages.**

## application.yaml
The configuration file contains settings for the base URL and environment variables

server:
  port: 8080
base:
  url: http://localhost:8080
