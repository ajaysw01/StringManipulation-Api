# JSON Manipulation API

This project is a Spring Boot application that provides APIs to manipulate and retrieve JSON data. The application allows dynamic string manipulation within a predefined JSON model and stores the manipulated JSON models in a database.

## Getting Started

### Prerequisites

- Java 11 or later
- Maven 3.6.3 or later
- A running instance of MySQL

### Installing

1. **Clone the repository**:
    ```sh
    git clone https://github.com/your-repo/json-manipulation-api.git
    cd json-manipulation-api
    ```

2. **Configure the database**:
   Update the `application.yml` file with your database configuration:
    ```yaml
    spring:
      application:
        name: StringManipulation

      datasource:
        url: jdbc:mysql://localhost:3306/SMDb
        driverClassName: com.mysql.cj.jdbc.Driver
        username: root
        password: root
      h2:
        console:
          enabled: true
      jpa:
        database-platform: org.hibernate.dialect.MySQL8Dialect
        hibernate:
          ddl-auto: update

    springdoc:
      swagger-ui:
        path: /swagger-ui.html
      api-docs:
        path: /api-docs
    ```

3. **Build and run the application**:
    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

### API Endpoints

#### 1. Manipulate JSON

- **URL**: `/api/manipulate`
- **Method**: POST
- **Parameters**:
  - `input` (String): A string containing key-value pairs separated by `:::`, and pairs separated by commas. Example: `New:::NewDocument,Open:::OpenDocument`
- **Response**: 
  - `200 OK`: Returns the modified JSON model.
  - `500 Internal Server Error`: Returns an error message if processing fails.

**Example Request**:
```sh
curl -X POST "http://localhost:8080/api/manipulate?input=New:::NewDocument,Open:::OpenDocument"
```

**Example Response**:
```json
{
    "menu": {
        "id": "file",
        "value": "File",
        "popup": {
            "menuitem": [
                {"value": "NewDocument", "onclick": "CreateDoc()"},
                {"value": "OpenDocument", "onclick": "OpenDoc()"},
                {"value": "Save", "onclick": "SaveDoc()"}
            ]
        }
    }
}
```

#### 2. List JSON Models

- **URL**: `/api/list`
- **Method**: GET
- **Response**: 
  - `200 OK`: Returns a list of all stored JSON models.
  - `500 Internal Server Error`: Returns an error message if retrieval fails.

**Example Request**:
```sh
curl -X GET "http://localhost:8080/api/list"
```

**Example Response**:
```json
[
    {
        "id": 1,
        "jsonModel": "{\"menu\": {\"id\": \"file\",\"value\": \"File\",\"popup\": {\"menuitem\": [{\"value\": \"NewDocument\", \"onclick\": \"CreateDoc()\"},{\"value\": \"OpenDocument\", \"onclick\": \"OpenDoc()\"},{\"value\": \"Save\", \"onclick\": \"SaveDoc()\"}]}}}"
    }
]
```

### Testing

Postman can be used to test the API endpoints. Import the provided collection and run the tests.
![Description](src/main/resources/images/Screenshot(3).png)
![Description](src/main/resources/images/Screenshot(3).png)


### Swagger API Documentation

The application uses Swagger to document the API. Once the application is running, you can access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Swagger](https://swagger.io/)
```

This `README.md` now includes the `application.yml` configuration for setting up the database connection and Swagger documentation paths.
