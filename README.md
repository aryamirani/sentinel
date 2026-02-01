# Sentinel

Distributed fraud detection and entity resolution engine built with Spring Boot, MongoDB, Kafka, and React.

## Architecture

```
┌─────────────┐     ┌─────────┐     ┌──────────┐     ┌─────────────┐
│  API Gateway │────▶│  Kafka  │────▶│  MongoDB │────▶│ Risk Engine │
│ (Spring Boot)│     │ Topic   │     │  Atlas   │     │  (Async)    │
└─────────────┘     └─────────┘     └──────────┘     └──────┬──────┘
                                                            │
                                    ┌──────────┐     ┌──────▼──────┐
                                    │  React   │◀────│  WebSocket  │
                                    │Dashboard │     │   Alerts    │
                                    └──────────┘     └─────────────┘
```

## Risk Engine

Three-layer scoring system (0-100) mapped to ALLOW / REVIEW / BLOCK:

- **Deterministic Rules** (35%) — velocity spikes via Redis sliding windows, device fingerprint anomalies, amount thresholds
- **Vector Search** (25%) — OpenAI embeddings stored in MongoDB, `$vectorSearch` aggregation for behavioral similarity matching against known fraud patterns
- **Topology Traversal** (40%) — `$graphLookup` for circular payment loop detection and shared infrastructure analysis across entity networks

## Tech Stack

- Java 21, Spring Boot 3.4, WebFlux
- MongoDB (Change Streams, Vector Search, Graph Lookup)
- Apache Kafka
- Redis
- React, Vite, React Flow, Recharts

## Setup

### Prerequisites

- Java 21+
- Node.js 18+
- Docker & Docker Compose

### Run Infrastructure

```bash
docker compose up -d
```

### Run Backend

```bash
./gradlew bootRun
```

### Run Frontend

```bash
cd sentinel-dashboard
npm install
npm run dev
```

## API

### Ingest Transaction

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "X-Idempotency-Key: unique-key-123" \
  -d '{
    "entityId": "entity-001",
    "amount": 5000.00,
    "merchantName": "Apex Trading",
    "currency": "USD",
    "ipAddress": "185.220.101.42"
  }'
```

### Get Transactions

```bash
curl http://localhost:8080/api/transactions?limit=20
```
