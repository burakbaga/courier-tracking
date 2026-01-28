# Courier Tracking Application

A Spring Boot based RESTful web application that tracks courier geolocations in real time, logs store proximity events, and calculates total travel distance per courier.


## Features

* Stream courier locations via REST API

* Calculate total travel distance per courier using Haversine formula

* Detect courier entrance within 100 meters of Migros stores

* Prevent duplicate entrances within 1 minute

* Store data persistence with Couchbase

* JSON fallback for store locations

* Swagger/OpenAPI documentation

* Dockerized environment

* Unit & integration tests (including Testcontainers)

## Tech Stack
* Java 17
* Spring Boot 3.2.x
* Couchbase
* Maven
* Docker & Docker Compose
* Swagger (springdoc-openapi)
* JUnit 5 + Testcontainers

## Project Structure

com.migros.couriertracking  
├── config  
├── controller  
├── service  
├── repository  
├── provider  
├── model  
├── util  
├── exception  
└── integration  

## Prerequisites

* Java 17+
* Maven
* Docker

## Build & Run (Local)

### Build the project
```
mvn clean package
```
### Run Spring Boot app

```
java -jar target/couriertracking-0.0.1-SNAPSHOT.jar
```

**Application will start on:**
```
http://localhost:8080
```
**Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```

## Run with Docker Compose (Recommended)

### Build jar
```
mvn clean package
```
### Start containers
```
docker-compose up --build
```
### What happens automatically?

1. Couchbase server starts inside Docker container

2. Cluster is initialized automatically

3. courier_bucket bucket is created

4. Application connects to Couchbase

5. Stores document is automatically seeded from stores.json if not present

6. No manual Couchbase configuration is required.

#### **Services:**

Spring Boot App → ``` http://localhost:8080```

Couchbase Admin UI → ``` http://localhost:8091```

## API Usage Examples

### Send Courier Location

**Request**
```
curl -X POST http://localhost:8080/courier/location \
-H "Content-Type: application/json" \
-d '{
"courierId": "C1",
"lat": 40.9923307,
"lng": 29.1244229,
"timestamp": "2026-01-28T12:00:00"
}'
```

**Response**

```HTTP 200 OK```

### Get Total Travel Distance

**Request**
```
curl http://localhost:8080/courier/C1/distance
```
**Sample Response**
```
{
"courierId": "C1",
"totalDistance": 250.75
}
```
### Invalid Location Example

**Request**
```
curl -X POST http://localhost:8080/courier/location \
-H "Content-Type: application/json" \
-d '{}'
```
**Response**
```
{
"timestamp": "2026-01-28T12:10:00",
"status": 400,
"error": "Bad Request",
"message": "courierId cannot be blank, timestamp is required",
"path": ""
}
```
### Courier Not Found

**Request**
```
curl http://localhost:8080/courier/UNKNOWN/distance
```
**Response**
```
{
"timestamp": "2026-01-28T12:12:00",
"status": 404,
"error": "Not Found",
"message": "Courier not found: UNKNOWN",
"path": "/courier/UNKNOWN/distance"
}
```

## Store Locations

**Loaded from:**

* Couchbase (primary)
* stores.json (fallback)

**Radius detection:** 100 meters

**Re-entry within 1 minute is ignored.**

## Running Tests
```
mvn test
```

### Includes:

* Controller unit tests

* Exception handler tests

* Integration flow tests

* Couchbase Testcontainers

### Design Patterns Used

* Strategy Pattern → DistanceCalculator

* Repository Pattern → DistanceRepository

* Provider Abstraction → StoreProvider

* Service Orchestration → CourierProcessingService

### Improvements (For Future)

* TTL cleanup for in-memory proximity cache
* Persist last known courier location
* Async processing for high throughput