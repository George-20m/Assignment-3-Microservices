# Microservices Assignment

This project demonstrates a basic microservices architecture using two Spring Boot services communicating over HTTP, containerized with Docker.

## Project Structure

```
.
├── servicea/                 # Client/Frontend service (Port 8080)
│   ├── Dockerfile
│   └── src/main/java/.../controller/ClientController.java
├── serviceb/                 # Data/Backend service (Port 8081)
│   ├── Dockerfile
│   └── src/main/java/.../controller/DataController.java
├── docker-compose.yml        # Docker orchestration
└── README.md
```

## Services Overview

### Service A (Client Service)
- **Port:** 8080
- **Endpoint:** `/get-data`
- **Responsibility:** Acts as a client that calls Service B and returns the aggregated response
- **Behavior:** Gracefully handles Service B unavailability

### Service B (Data Service)
- **Port:** 8081
- **Endpoint:** `/data`
- **Responsibility:** Provides data API
- **Behavior:** Simple stateless response

## Getting Started

### Prerequisites
- Docker & Docker Compose installed
- Java 17+ (for local development)
- Maven (for building)

### Building the Services

```bash
# Build Service A
cd servicea/servicea
mvn clean package

# Build Service B
cd serviceb/serviceb
mvn clean package
```

### Running with Docker Compose

```bash
docker-compose up --build
```

Both services will start:
- Service A: http://localhost:8080
- Service B: http://localhost:8081

### API Endpoints

| Service | Endpoint | Description |
|---------|----------|-------------|
| Service A | `GET /get-data` | Calls Service B and returns aggregated response |
| Service B | `GET /data` | Returns simple data response |

## Screenshots

### Successful API Calls

<!-- TODO: Add screenshot of successful /get-data call showing "From Service A -> Hello from Service B" -->
*Figure 1: Successful API call to Service A (/get-data)*

<!-- TODO: Add screenshot of successful /data call to Service B -->
*Figure 2: Direct API call to Service B (/data)*

### Failure Simulation

<!-- TODO: Add screenshot showing Service A response when Service B is down -->
*Figure 3: Service A gracefully handling Service B unavailability ("Service B is DOWN!")*

<!-- TODO: Add screenshot of Docker containers running -->
*Figure 4: Both services running in Docker containers*

---

## Reflection Questions

### 1. Which parts of this lab show the benefits of microservices over a monolith?

This implementation demonstrates several key benefits of microservices:

- **Independent Deployment:** Each service can be built, deployed, and scaled independently. Service B could be updated without affecting Service A.

- **Technology Heterogeneity:** While both services use Spring Boot here, each could theoretically use different technologies, databases, or frameworks based on their specific needs.

- **Fault Isolation:** When Service B goes down, Service A continues to operate and gracefully degrades by returning an error message rather than crashing entirely.

- **Team Autonomy:** Different teams could own each service, allowing for parallel development and independent release cycles.

### 2. Which new complexities were introduced by splitting the system into two services?

Several complexities emerged:

- **Network Communication:** Services must communicate over HTTP, introducing potential network failures, timeouts, and latency issues that don't exist in monolithic in-process calls.

- **Service Discovery:** Service A must know how to locate Service B (currently hardcoded as `http://serviceb:8081`).

- **Distributed Debugging:** Tracing a request across service boundaries requires correlation IDs and centralized logging.

- **Data Consistency:** If these services shared data, maintaining consistency would require distributed transactions or eventual consistency patterns.

- **Operational Overhead:** Two services mean two containers to monitor, two logs to aggregate, and more complex deployment orchestration.

### 3. What would break if network latency increased or one service became slow?

Several issues would arise:

- **Cascading Failures:** Service A would experience timeouts waiting for Service B, potentially causing thread pool exhaustion and making Service A unavailable to its clients.

- **No Timeout Configuration:** The current `RestTemplate` has no explicit timeout settings, meaning requests could hang indefinitely under high latency.

- **No Circuit Breaker:** Without a circuit breaker pattern (like Resilience4j), Service A would continue calling the slow Service B, worsening the situation.

- **User Experience Degradation:** End users would experience slow responses from Service A even though the issue originates in Service B.

- **Resource Exhaustion:** Slow responses would tie up connections and threads in both services, potentially causing system-wide failures.

### 4. Which 12-factor app principles are visible in this implementation?

| Principle | Implementation Evidence |
|-----------|------------------------|
| **I. Codebase** | Each service has its own git-tracked codebase |
| **II. Dependencies** | Maven (`pom.xml`) explicitly declares all dependencies |
| **III. Config** | Configuration via `application.properties` (could be improved with env vars) |
| **IV. Backing Services** | Services treat each other as attached resources via HTTP |
| **V. Build, Release, Run** | Clear separation: Maven build → Docker image → Container run |
| **VI. Processes** | Services are stateless; no session state stored |
| **VII. Port Binding** | Services are self-contained and export via port binding |
| **VIII. Concurrency** | Services can be scaled horizontally via Docker Compose replicas |
| **IX. Disposability** | Containers can start/stop quickly; no persistent state |
| **X. Dev/Prod Parity** | Docker ensures identical environments across dev and prod |
| **XI. Logs** | Spring Boot outputs to stdout (could be enhanced with log aggregation) |
| **XII. Admin Processes** | One-off admin processes could run as separate containers |

---

## Potential Improvements

- Add circuit breaker pattern (Resilience4j) for fault tolerance
- Implement centralized logging (ELK Stack, Loki)
- Add distributed tracing (Jaeger, Zipkin)
- Configure explicit connection timeouts
- Use environment variables for configuration
- Add health check endpoints (`/actuator/health`)
- Implement API gateway for routing

## License

This project is for educational purposes.
