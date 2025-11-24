✈️ SkyKing Air – Flight Booking System (Microservices Architecture)

A production-grade, scalable, fault-tolerant Flight Booking System similar to Indigo/Air India.
Built using Spring Boot Microservices, Spring Cloud, PostgreSQL, Kafka, Redis, Eureka, API Gateway, and Security with JWT.

📌 Table of Contents

Overview

Architecture

Microservices

Tech Stack

Features

Folder Structure

Setup & Installation

API Endpoints

Sequence Diagrams

Database Schema

Kafka Streaming Pipelines

Security Layer

Future Enhancements

Contributing

License

⭐ Overview

SkyKing Air is a highly scalable Flight Booking System built using modern microservices and cloud-native best practices.
It supports operations like:

Search flights

Real-time seat availability

Booking & Payment workflow

User authentication

Notifications

Kafka-based async pipelines

Distributed caching via Redis

This system is designed for high throughput and real-time operations similar to IRCTC/airline booking systems.

🏛️ Architecture
                        +---------------------------+
                        |    API Gateway (Spring)   |
                        +------------+--------------+
                                     |
                          +----------+----------+
                          |  Eureka Service     |
                          +----------+----------+
                                     |
      ---------------------------------------------------------------------
      |             |                |                |                 |
+-----------+  +-----------+   +-----------+   +--------------+   +-----------+
| Auth Svc  |  | User Svc  |   | Flight Svc|   | Booking Svc  |   | Payment Svc|
+-----------+  +-----------+   +-----------+   +--------------+   +-----------+
                    |                 |               |
                    |                 |               |
                 +-----------+   +-----------+   +-------------+
                 | PostgreSQL|   | PostgreSQL|   | PostgreSQL |
                 +-----------+   +-----------+   +-------------+
      
      +---------------------------------------------------------+
      |                   Kafka Event Bus                       |
      +---------------------------------------------------------+
         |           |               |              |
    Notification  Audit Logs     Seat Locking     Payment Status

      +---------------+
      | Redis Cache   |
      +---------------+


🧩 Microservices
1️⃣ Auth Service

JWT-based authentication

Login/Signup

Token validation

2️⃣ User Service

Profile management

Saved passengers

Travel history

3️⃣ Flight Service

Manage flights, routes, schedules

Real-time seat availability

Uses Redis for caching

4️⃣ Booking Service

Seat locking mechanism

Booking confirmation

Interacts with Payment & Flight Service

5️⃣ Payment Service

Razorpay / Stripe integration-ready

Payment webhooks

Refunds

6️⃣ Notification Service

Email/SMS notifications

Kafka-based event consumer

🛠️ Tech Stack
Backend

Java 17

Spring Boot 3.x

Spring Cloud Netflix

Spring Security + JWT

Spring Data JPA / Hibernate

Lombok

Databases

PostgreSQL (Main DB)

Redis (Distributed Cache)

Messaging

Apache Kafka

Kafka Connect (optional)

Cloud Native Tools

Eureka Discovery Server

Spring Cloud Gateway

Zipkin (Distributed Tracing)

Docker & Docker Compose

📁 Folder Structure
skyking-air/
│
├── gateway-service/
├── eureka-service/
├── auth-service/
├── user-service/
├── flight-service/
├── booking-service/
├── payment-service/
├── notification-service/
│
├── docker-compose.yml
├── README.md
└── scripts/

🚀 Setup & Installation
1️⃣ Clone the Project
git clone https://github.com/yourname/skyking-air.git
cd skyking-air

2️⃣ Start Databases (Postgres + Redis + Kafka)
docker-compose up -d

3️⃣ Start Eureka
cd eureka-service
mvn spring-boot:run

4️⃣ Start API Gateway
cd gateway-service
mvn spring-boot:run

5️⃣ Start Individual Microservices
mvn spring-boot:run

📡 API Endpoints
🔐 Auth Service
Endpoint	Method	Description
/auth/signup	POST	Register user
/auth/login	POST	Generate JWT
✈️ Flight Service
Endpoint	Method	Description
/api/flights/search	POST	Search flights
/api/flights/{id}	GET	Flight details
/api/flights/seats/{flightId}	GET	Seat availability
🧾 Booking Service
Endpoint	Method	Description
/api/booking/create	POST	Create booking
/api/booking/confirm	POST	Confirm after payment
/api/booking/{id}	GET	Booking details
💳 Payment Service
Endpoint	Method	Description
/api/payments/initiate	POST	Create payment
/webhook/payment-status	POST	Payment status update
🔄 Sequence Diagrams
🟦 Flight Search Workflow
User → API Gateway → Flight Service → Redis Cache → DB → Gateway → User

🟥 Booking Workflow
User → Gateway → Booking Service → Flight Service (Seat Lock)
      → Payment Service → Kafka → Booking Service → Notification

🗄️ Database Schema
✈️ Flights Table
Column	Type
flight_id	UUID
airline	VARCHAR
source	VARCHAR
destination	VARCHAR
departure_time	TIMESTAMP
arrival_time	TIMESTAMP
price	DECIMAL
🪑 Seats Table
Column	Type
seat_id	UUID
flight_id	UUID
seat_number	VARCHAR
seat_type	ENUM
is_booked	BOOLEAN
📘 Bookings Table
Column	Type
booking_id	UUID
user_id	UUID
flight_id	UUID
status	ENUM
payment_status	ENUM
🔥 Kafka Streaming Pipelines
1️⃣ booking_created → notification_service
2️⃣ payment_success → booking_service
3️⃣ seat_locked → audit_service

All events are JSON-based and schema-registry compatible.

🔐 Security Layer

JWT Authentication

Role-based access

API Gateway global filters

Token validation across microservices

🚧 Future Enhancements

Add Admin Dashboard

Add Chatbot for booking

Add Dynamic Pricing Engine

Implement CQRS Pattern

Add ElasticSearch for fast search

Deploy to AWS (EKS + RDS + MSK)

🤝 Contributing

Pull requests are welcome!
Please open an issue before making major changes.

📜 License

This project is licensed under the MIT License.
