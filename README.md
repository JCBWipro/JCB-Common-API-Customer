# 🚀 JCBLiveLink Microservices: Powering Seamless Connectivity
Welcome to the JCBLiveLink Microservices repository! This project forms the backbone of our robust and scalable application, designed to deliver exceptional user experiences.

## 🏗️ Architectural Overview
We've embraced a microservices architecture to ensure agility, maintainability, and independent scaling of our services. Here's a glimpse into the key components:

* **JCB-Common-API-Customer:** The foundation of our microservices ecosystem, housing shared code and configurations.
* **JCB-API-Gateway:** Your single point of entry! The API gateway intelligently routes requests to the appropriate microservices.
* **JCB-Authentication & JCB-Mobile-Authentication:**  Securing your data is paramount. These services handle user authentication and authorization across platforms.
* **JCB-Service-Registry (Eureka):**  Facilitating seamless communication, Eureka acts as our service discovery and registration hub.
* **JCB-User:**  Everything about our users in one place! This service manages user data and related operations.
* **JCB-Machines:**  Connecting the physical and digital, this service manages machine data and interactions.

##  ✨ Key Features
* **Independent Deployable:**  Each microservice can be deployed and scaled independently, enabling rapid development and deployment cycles.
* **Technology Agnostic:**  Built on RESTful APIs, our services can seamlessly integrate with various technologies and platforms.
* **Enhanced Scalability:**  Effortlessly handle growing demands by scaling individual services as needed.
* **Improved Fault Tolerance:**  Isolate failures and ensure the application remains operational even if one service encounters issues.

## 🚀 Getting Started
Ready to explore the power of JCBLiveLink Microservices? Follow these steps to get started:

1. **Prerequisites:** Make sure you have Java 17, Git, and your preferred IDE (IntelliJ, Eclipse) installed.
2. **Clone the Repository:**
   ```bash
   git clone https://github.com/JCBWipro/JCB-Common-API-Customer.git
   ```
3. **Build the Services:** Navigate to each service directory and execute
   ```
   mvn clean install
   ```
4. **Launch the Service Registry:** Start the JCB-Service-Registry (Eureka) service first.
5. **Start Other Services:** You can now start the remaining services in any order.

##  🤝 Contributing
We welcome contributions from the community!  To contribute, please follow the standard GitHub workflow for submitting pull requests.

##  🙋 Need Help?
Have questions or need assistance? Reach out to our dedicated team at **Team-NextGen@wipro.com**. We're here to help ...!!! 