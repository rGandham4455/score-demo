# score-demo

# Evaluation Service

This Spring Boot application provides endpoints to upload and evaluate score sheets, and retrieve evaluated scores based on optional filters.

## Features

1. **Upload Score Sheets**
    - Endpoint: `POST /evaluation/sheets`
    - Request Body: JSON array of score sheets
    - Response: HTTP 202 for valid requests, HTTP 400 for invalid requests

2. **Retrieve Evaluated Scores**
    - Endpoint: `GET /evaluation/scores`
    - Optional Query Parameters: `testeeIds`, `subjects`, `totalRange`, `averageRange`, `scoreRange`
    - Response: HTTP 200 with evaluated scores, HTTP 400 for invalid requests

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Running the Application

1. Clone the repository:
    ```sh
    git clone https://github.com/rGandham4455/score-demo.git
    cd score-demo
    ```

2. Build and run the application:
    ```sh
    mvn spring-boot:run
    ```

### Endpoints

#### Upload Score Sheets

- **URL**: `/evaluation/sheets`
- **Method**: `POST`
- **Request