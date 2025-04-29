

# 🏋️‍♂️ GrindAI
A modular fitness tracking application built with Spring Boot microservices, featuring service discovery with Eureka, asynchronous messaging via RabbitMQ, and AI-powered insights through Google gemini-2.0-flash integration.

---

## 🚀 Features

- **Microservices Architecture** Decoupled services for user management, activity tracking, and AI-driven analytics.
- **Service Discovery** Utilizes Eureka for dynamic service registration and discover.
- **API Gateway** Centralized routing and load balancing through a dedicated gateway service.
- **Asynchronous Communication** Employs RabbitMQ for efficient inter-service messaging
- **AI Integration** Leverages Google Gemini via Spring AI for personalized fitness recommendation.
- **Centralized Configuration** Manages service configurations using a Spring Cloud Config Server.

---

## 🧱 Project Structure

```
fitnessApp/
├── activity-service/     # Handles fitness activity data
├── ai-service/           # Interfaces with Gemini AI for insights
├── config-server/        # Centralized configuration management
├── eureka-server/        # Service registry for discovery
├── gateway/              # API Gateway for routing requests
└── user-service/         # Manages user profiles and authentication
```

---

## ⚙️ Technologies Used

- **Spring Boot 3.x*: Framework for building microservics.
- **Spring Cloud Eureka*: Service discovery mechanim.
- **Spring Cloud Gateway*: API gateway for routing and filterig.
- **RabbitMQ*: Message broker for asynchronous communicatin.
- **Spring AI with Gemini*: Integration with Google Gemini for AI capabilities.
- **Spring Cloud Config*: Centralized configuration management.

## 📦 Getting Started

**Prerequisites:**

- Java 17 or higher
- Maven3.8
- RabbitMQ instance running
- Google Cloud account with Gemini API access (should have an API Key in environment variables)

**Steps:**
1. Clone the reposiory:

   ```bash
   git clone https://github.com/SghairiYoussef/grindAI.git
   cd grindAI
   ```
2. Start the Eureka Sever:

   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```
3. Start the Config Sever:

   ```bash
   cd ../config-server
   mvn spring-boot:run
   ```
4. Start the API Gatway:

   ```bash
   cd ../gateway
   mvn spring-boot:run
   ```
5. Start the User Serice:

   ```bash
   cd ../user-service
   mvn spring-boot:run
   ```
6. Start the Activity Serice:

   ```bash
   cd ../activity-service
   mvn spring-boot:run
   ```
7. Start the AI Serice:

   ```bash
   cd ../ai-service
   mvn spring-boot:run
   ```

## 🤝 Contribting

Contributions are welcome! Please fork the repository and submit a pull request for any enhancements or bugfixes.