# ✈️ SkyKing Air — Flight Booking System (Microservices)

> Production-style, cloud-native Flight Booking System built with Spring Boot microservices, Kafka, Redis and PostgreSQL.  
> Designed for: flight search, seat-locking, booking, payment, notifications and observability.

---

## Table of Contents
- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Microservices](#microservices)
- [Tech Stack](#tech-stack)
- [Repository Structure](#repository-structure)
- [Prerequisites](#prerequisites)
- [Quick Start (Docker Compose)](#quick-start-docker-compose)
- [Run Locally (per service)](#run-locally-per-service)
- [Environment Variables](#environment-variables)
- [API Examples (curl)](#api-examples-curl)
- [Database Schema (summary)](#database-schema-summary)
- [Kafka Topics & Events](#kafka-topics--events)
- [Caching & Seat Locking](#caching--seat-locking)
- [Observability](#observability)
- [Testing](#testing)
- [Development Notes & Tips](#development-notes--tips)
- [Contributing](#contributing)
- [License](#license)
- [Contacts / Maintainers](#contacts--maintainers)

---

## Project Overview
SkyKing Air is an airline booking platform built as separate microservices:
- Auth (JWT)
- User (profiles, passengers)
- Flight (catalog, schedules, seat availability)
- Booking (seat locking, reservations)
- Payment (gateway integration & webhooks)
- Notification (email/SMS via Kafka consumers)
- Gateway (Spring Cloud Gateway / API Gateway)
- Discovery (Eureka / service registry)

Goal: clear separation of concerns, async events via Kafka, caching with Redis for fast availability checks, and strong security with JWT.

---

## Architecture
*(Add your architecture diagram here — PNG/SVG recommended)*


---

## Microservices (high-level)
- **gateway-service** — Single entrypoint, routing, auth filter.
- **eureka-service** — Service discovery server.
- **auth-service** — Login/Signup, JWT issuance & validation.
- **user-service** — Profile, saved passengers, travel history.
- **flight-service** — Flights CRUD, schedule, seat inventory, caching.
- **booking-service** — Create booking, seat-lock & confirm flows.
- **payment-service** — Payment initiation, webhook handling, refunds.
- **notification-service** — Kafka consumer -> email/SMS notifications.
- **audit-service** (optional) — Persist events for auditing.

---

## Tech Stack
- Java 17, Spring Boot 3.x
- Spring Cloud (Gateway, Netflix Eureka)
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- PostgreSQL
- Redis (Cache / seat locks)
- Apache Kafka (event bus)
- Docker / Docker Compose
- Lombok, Flyway (optional), MapStruct (optional)

---

## Repository Structure
```
skyking-air/
│
├── eureka-service/
├── gateway-service/
│
├── auth-service/
├── user-service/
├── flight-service/
├── booking-service/
├── payment-service/
├── notification-service/
│
├── common-libs/
│ ├── common-dto/
│ ├── common-utils/
│ ├── common-exceptions/
│ └── common-security/ (JWT Handler)
│
├── docker-compose.yml
├── .gitignore
├── README.md
└── docs/
├── architecture.png
├── sequence-diagrams/
└── api-specs/
```
