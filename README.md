# RestCalculator – Spring Boot Multi-Module Project

## Introduction
RestCalculator is a Spring Boot multi-module project designed to provide a RESTful API for performing various calculator operations. The project leverages Kafka for messaging and follows a modular architecture for scalability and maintainability. An Angular-based frontend interacts with the backend services.

### Project Structure
- **Parent Module (`restcalculator`)** – Root module managing dependencies and submodules.
- **Common Module (`common`)** – Shared classes and utilities.
- **Calculator Module (`calculator`)** – Core business logic for calculator operations.
- **Rest Module (`rest`)** – Exposes the calculator functionality via RESTful APIs.
- **Angular Frontend (`calculator-frontend`)** – Provides a simple user interface.

## Table of Contents
- [Installation](#installation)
- [Modules](#modules)
- [Features](#features)
- [Other Technical Features](#other-technical-features)
- [Dependencies](#dependencies)
- [Configuration](#configuration)
- [REST API Endpoints](#rest-api-endpoints)
- [Kafka Messaging Flow](#kafka-messaging-flow)
- [Extras](#extras)
- [Future Features](#future-features)


## Installation
### Prerequisites
- **Java 21** or higher
- **Maven 3.x**
- **Docker and Docker Compose**

### Build Instructions
Clone the repository and navigate to the root directory of the project:
```bash
git clone <repository-url>
cd restcalculator
```



#### 1. Using Docker

#### Prerequisites
To run a Docker container, ensure the following are installed and properly configured on your system:

1. **Docker**:
   - Install Docker: Follow the instructions at [Docker Installation Guide](https://docs.docker.com/get-docker/).
   - Verify Docker is installed:
     ```bash
     docker --version
     ```
   - Ensure the Docker daemon is running.

2. **Docker Compose** :
   - Install Docker Compose: Follow the instructions at [Docker Compose Installation Guide](https://docs.docker.com/compose/install/).
   - Verify Docker Compose is installed:
     ```bash
     docker-compose --version
     ```

3. **Resources for Docker**:
   - Allocate sufficient CPU, memory, and disk space to Docker:
     - Recommended: At least **2 CPUs**, **4GB RAM**, and **10GB disk space**.
   - Configure these resources in the Docker Desktop application (if using Docker Desktop).

4. **Kafka and Zookeeper Ports**:
   - Ensure the following ports are available for Kafka, Zookeeper, and services:
     - `2181`, `9092`, `29092`, `8081`, `8085`, and `8086`.

---


To build and run:
```bash
docker-compose up --build
```

To stop the containers:
```bash
docker-compose down
```

#### 2. Without Docker
If Docker is not used, make the following adjustments:

##### Configuration Changes
In the application.properties file of the rest and calculator modules:

Replace the ```broker``` reference with ```localhost``` in the ```application.properties``` of the ```rest``` and ```calculator``` modules:
```bash
spring.kafka.bootstrap-servers=localhost:9092
```
##### Using Local docker-compose

Use the ```dockerfile.local``` file instead of the default Dockerfile. (change ```docker-compose.yml.local``` to ```docker-compose``` and the original
```docker-compose``` to ```docker-compose.x(any name)```

##### Install from parent
```bash
mvn clean install
```

##### Start Kafka and Zookeeper with docker
and run on docker
```bash
docker-compose up 
```

##### Build and run the services

* For the rest module:

```bash
cd rest
mvn spring-boot:run

```


* For the calculator module:
from parent:
```bash
cd calculator
mvn spring-boot:run
```

## Modules
### 1. Parent Module (```restcalculator```)
* Aggregates all modules.
* Manages dependency versions and Maven build configurations.

### 2. Common Module (common)
* Contains shared utilities and common classes.

### 3. Calculator Module (calculator)
* Implements core arithmetic logic and decision-making for calculations.

### 4. Rest Module (rest)
* Provides REST endpoints for calculator functions and forwards requests to the calculator module via Kafka.

### 5. Angular Frontend (calculator-frontend) (extra)
* A simple Angular frontend that interacts with the REST API.
* Dockerized using nginx for deployment.

## Features
* Modular architecture with clearly separated concerns.
* RESTful API with Swagger/OpenAPI integration.
* Kafka integration for messaging and event-driven architecture.
* Angular-based frontend for interacting with the API.
* Test containers for integration testing.

## Other Technical Features
#### 1. Logging with SLF4J
* The project uses SLF4J (Simple Logging Facade for Java) with Logback as the logging implementation.

* Logging is configured to write logs both to the console and a file for better traceability and persistence.

* The log file is located at:

	* ```logs/application.log``` (relative to the working directory where the application is run).
* Example of a log entry:
		
	```yaml
2025-01-07T21:51:45 [org.springframework.kafka.KafkaListenerEndpointContainer#0-0-C-1] LEVEL=INFO TRACEID=272daf88-9168-4b66-9ad3-042c70b5a32f | Sending message from calculation-result-topic topic CalculationResponse(requestId=272daf88-9168-4b66-9ad3-042c70b5a32f, result=0.2, error=null)```

#### 2. Unique Identifiers (RequestId)
* Each request is assigned a unique identifier (RequestId) to:
	* Enable better tracking of requests across services.
	* Simplify debugging and correlation between logs and messages.
	
* RequestId Propagation:
	* The RequestId is passed between services via HTTP headers and Kafka messages.
	* This ensures consistent traceability for each request.

*	How it Works:

	* If a RequestId is not provided in the incoming request, one is automatically generated using UUID.
	* The RequestId is added to the logging context and HTTP response headers for reference.
	
#### 3. MDC Propagation
* Mapped Diagnostic Context (MDC) is used for adding contextual information (like RequestId) to logs dynamically.

* MDC ensures that all logs within a given request are tagged with its unique RequestId, making it easy to filter and analyze logs.



## Dependencies
* **Spring Boot 3.4.1**
* **Kafka** for messaging
* **Lombok** for reducing boilerplate code
* **Checkstyle** for code quality enforcement
* **Springdoc** for API documentation

## Configuration
###  Rest Module
#### `application.properties`
* **Kafka Topics**:
	* ```kafka.result-topic=calculation-result-topic```
	* ```kafka.request-topic=calculation-request-topic```
* **Kafka Servers**: ```spring.kafka.bootstrap-servers=broker:29092```
* **Swagger API Path**: ```/swagger-ui.html```
* **Server Port**: ```8081```.
### MDC Configuration (MdcFilterConfig.java)
* Generates and manages correlation IDs for tracking requests.

## REST API Endpoints

### Calculator Endpoints

* **Addition**:
`GET /api/calculator/sum?a={num1}&b={num2}`
* **Subtraction**:
`GET /api/calculator/sub?a={num1}&b={num2}`
* **Multiplication**:
`GET /api/calculator/multi?a={num1}&b={num2}`
* **Division**:
`GET /api/calculator/div?a={num1}&b={num2}`


	* **Example**:
` http://localhost:8081/api/calculator/sum?a=2.0&b=7 `

## Kafka Messaging Flow

#### Rest Module
* **Producer**: Sends requests from the user to Kafka and forwards requests to the Calculator module.
* **Consumer**: Listens for responses in the calculator module.

#### Calculator Module
* **Consumer**: Listens for requests in kafka from the rest module
* **Producer**: Does the calculation and sends the response to the rest module

## Extras
### Swagger API Documentation
* The project includes Swagger/OpenAPI integration for exploring and testing the API.
* After running the application, access Swagger UI at: http://localhost:8081/swagger-ui.html.

### Code Quality and Documentation

#### 1. Checkstyle
* Checkstyle is used to enforce a consistent coding style and ensure code quality across the project.

* The project uses a custom Checkstyle configuration based on the Google Checks standard with additional rules tailored for this project.

* Configuration File:

	* The custom rules are defined in google_checks_custom.xml.
Features of the Custom Checkstyle Configuration:

#### 2. JAutodoc for JavaDocs
* JAutodoc is used to automate the generation of JavaDoc comments for classes, methods, and fields.

* Features:

	* Automatically generates JavaDoc stubs for missing documentation.
	* Encourages consistent and thorough documentation throughout the codebase

### Angular Frontend
* The Angular frontend provides a simple interface to interact with the calculator API.
* Access the frontend at:
http://localhost:4200.
* It supports:
Adding, subtracting, multiplying, and dividing numbers.
* Displaying results dynamically.

### Kafka UI
* The project includes a Kafka UI tool for monitoring Kafka topics, messages, and consumer groups.
* Access the Kafka UI at:
 http://localhost:8086.
* Features of the Kafka UI:
	* View and manage Kafka topics.
	* Inspect consumer groups and their statuses.
	* View messages in real-time.

## Future Features


#### 1. Enhanced API Features
* **Authentication and Authorization**:
	* Integrate OAuth2, JWT, or API key-based authentication for secure API access.
	* Role-based access control (RBAC) to restrict certain operations.
* **Advanced Calculator Operations:**
	* Add support for scientific calculations (e.g., trigonometry, logarithms, etc.).
	* History of calculations per user or session.
	* Support for batch calculations (e.g., an array of operations sent in a single request).


#### 2. Frontend Enhancements
* Enhanced UI/UX:
	* Add a responsive design with Bootstrap or Material Design.


#### 3. Database Integration
* Persistent Storage:
	* Store calculation history, user preferences, and logs in a database (e.g., MySQL, PostgreSQL, or MongoDB).

#### 4. Performance and Scalability
* Horizontal Scaling:
	* Add support for Kubernetes or AWS ECS to scale services dynamically.
* Load Testing and Optimization:
	* Integrate tools like JMeter or Gatling to test the API under heavy loads.
	* Optimize Kafka message handling for higher throughput.

#### 5. Observability and Monitoring
* Logging and Monitoring:
	* Integrate Grafana and Prometheus.
	* Add structured logging with JSON format for easier parsing.
* Distributed Tracing
	* Implement distributed tracing with tools like OpenTelemetry or Zipkin.
* Health Checks:
	* Add endpoints for service health and readiness probes for better observability.
	
#### 6. Security Improvements
* Rate Limiting:
	* Prevent abuse by limiting the number of requests per user or IP address.
* Data Validation:
	* Enhance input validation to prevent injection attacks or malformed data.

* Add security
