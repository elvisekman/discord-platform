# discord-platform

## Architecture

Discord events are ingested via a gateway service and published to Kafka.
Multiple microservices consume the events and process them.

Discord Gateway -> Kafka -> Services -> Database

## Overview of Kafka
Discord
   │
   ▼
Discord Gateway
   │
   ▼
Kafka
 ├── voice-service
 ├── stats-service
 └── notification-service