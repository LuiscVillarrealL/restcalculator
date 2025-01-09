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
- [Dependencies](#dependencies)
- [Configuration](#configuration)
- [REST API Endpoints](#rest-api-endpoints)
- [Kafka Messaging Flow](#kafka-messaging-flow)
- [Extras](#extras)
- [Docker and Deployment](#docker-and-deployment)
- [Testing](#testing)
- [License](#license)

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

### Angular Frontend
* The Angular frontend provides a simple interface to interact with the calculator API.
* Access the frontend at:
http://localhost:4200.
* It supports:
Adding, subtracting, multiplying, and dividing numbers.
* Displaying results dynamically.
