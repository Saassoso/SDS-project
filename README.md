# SDS-Project
Smart Agriculture Monitoring System

📌 Project Overview

This project is a Smart Agriculture Monitoring System designed to track environmental parameters for tomato farming. It consists of multiple backend services, a frontend dashboard, and a bridge for TCP alerts to WebSocket communication. The system uses Kafka, MongoDB, WebSockets, and Docker Compose for efficient data processing and visualization.

📂 Project Structure
 
📦 Project Root
├── 📁 backend
│   ├── 📁 producer-jaxws  # JAX-WS producer (Humidity, Soil Moisture)
│   ├── 📁 producer-jaxrs  # JAX-RS producer (pH, Temperature)
│   ├── 📁 consumer        # Kafka Consumer & TCP alert sender
│   ├── 📁 serv-auth       # Authentication service (RMI) + Dashbaoed managment and all 
│   └── ...
├── 📁 frontend
│   ├── 📁 src
│   │   └── 📁 bridge
│   │         └── 📄 bridge-server.js  # Handles TCP alerts & WebSocket
│   └──📄 package.json
├── 📄 docker-compose.yml
├── 📄 README.md

🚀 Technologies Used

Backend: JAX-WS, JAX-RS, RMI, Kafka, MongoDB

Frontend: React.js (Dashboard, Login/Register)

Bridge: WebSocket & TCP alert handler (Node.js)

Containerization: Docker Compose

🛠️ Setup & Installation

Prerequisites

Ensure you have the following installed:

Docker & Docker Compose

Java 1.8+ (for backend services)

Node.js & npm (for frontend & bridge)