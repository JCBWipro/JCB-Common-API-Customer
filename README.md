# 🚧 JCBLiveLink Microservices: Powering Connected Machinery 🚧

Welcome to the engine room! This repository houses the microservices that power JCBLiveLink, our solution for real-time machine insights and management.  We've ditched the monolith for a flexible, scalable architecture that keeps things running smoothly.


|                      Microservice                      | Description                                                                      |
|:------------------------------------------------------:| :-------------------------------------------------------------------------------: |
|                   **JCB-Common-API**                   |      **The Bedrock:** Shared libraries and configurations for all.               |
|                  **JCB-API-Gateway**                   |  **The Welcome Center:** Directs traffic to the correct microservice.              |
| **JCB-Authentication** & **JCB-Mobile-Authentication** |     **Fort Knox:** Keeps user data secure across all devices.                    |
|                      **JCB-User**                      |       **User Central:** Manages everything about our valued users.                |
|                    **JCB-Machines**                    |     **The Heartbeat:** Handles machine data and communication.                   |
|                     **JCB-Alerts**                     |  **The Watchdog:** Monitors for issues and sends timely notifications.             |
|                    **JCB-Reports**                     |  **The Analyst:** Generates insightful reports for data-driven decisions.          |


## 💪 Why Microservices? Benefits You Can Build On

*   **🚀 Independent Deployments:** Each service is self-contained, allowing for rapid updates and rollouts without impacting the entire system.
*   **🔌 Mix & Match Tech:** Built on RESTful APIs, we seamlessly integrate with various technologies, giving you ultimate flexibility.
*   **📈 Scale On Demand:** Experiencing growing pains? No problem! Scale up individual services to handle increased load effortlessly.
*   **🛡️ Fault-Tolerant Design:** If one service stumbles, the rest keep going. Minimize downtime and keep your operations running smoothly.

## 🚀 Quick Start Guide: Get Your Hands Dirty

1.  **⚙️ Gear Up:** You'll need Java 17, Git, and your favorite IDE (IntelliJ, Eclipse, etc.).
2.  **📥 Grab the Code:**

    ```bash
    git clone https://github.com/JCBWipro/JCB-Common-API-Customer.git
    ```

3.  **🔨 Build Time:** In each service directory, run:

    ```bash
    mvn clean install
    ```

4.  **📞 Eureka First:** Start the **JCB-Service-Registry (Eureka)** to enable service discovery.
5.  **🔥 Fire Up the Rest:** Now you can start the other services in any order.

## 🙌 Join the Crew: Contribute!

We welcome contributions! Follow the standard GitHub flow – fork, code, make a pull request. Let's build something amazing together!

## ❓ Stuck in a Rut? We're Here to Help!

Hit us up at **Team-NextGen@wipro.com** for any questions or assistance. Happy coding!