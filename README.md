# JCBLiveLink Microservices

This repository contains the source code for the JCBLiveLink microservices architecture.

## Services

The following microservices are included in this repository:

* **JCB-Common-API-Customer:** This is the parent project containing common code and configurations shared by other
  services.
* **JCB-API-Gateway:** The API gateway acts as a single entry point for all external requests, routing them to the
  appropriate microservices.
* **JCB-Authentication:** Handles user authentication and authorization.
* **JCB-Mobile-Authentication:** Provides authentication services specifically for mobile clients.
* **JCB-Service-Registry:** Eureka server for service discovery and registration.
* **JCB-User:** Manages user data and related operations.

## Architecture

The microservices communicate with each other using REST APIs and are designed to be independently deployable and
scalable. Service discovery is handled by the JCB-Service-Registry (Eureka server).

## Getting Started

To run the microservices locally, follow these steps:

1. **Prerequisites:** Ensure you have Java 17, Git, and any IDE (IntelliJ, Eclipse) installed on your system.
2. **Clone the repository:** `git clone https://github.com/JCBWipro/JCB-Common-API-Customer.git`
3. **Build the services:** Navigate to each service directory and run `mvn clean install`.
4. **Start the service registry:** Run the JCB-Service-Registry service first.
5. **Start other services:** Start the remaining services in any order.

## Deployment

The microservices can be deployed to various environments, including cloud platforms like AWS or on-premise servers.

## Contributing

Contributions are welcome! Please follow the standard GitHub workflow for submitting pull requests.

## Contact

For any questions or issues, please contact **Team-NextGen** at **Team-NextGen@wipro.com**