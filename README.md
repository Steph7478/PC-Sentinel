# 🛸 Distributed System Metrics Observer

A high-performance, real-time distributed monitoring system built with **Spring Boot**, **gRPC**, **Kafka**, and **GraphQL**. This project demonstrates a full-cycle microservices architecture, moving from low-level OS data collection to real-time delivery via WebSockets.

> **Status:** 🚧 Project in Progress (Development Phase)

## 🏗 System Architecture

The project follows a **Contract-First** approach. A core **Shared Contract** module manages Protobuf definitions, ensuring that all services speak the same language.



---

## 🛠 Project Roadmap

### Phase 1: The Foundation (Infrastructure & Scoop)
Setting up the operating system to support a cloud-native ecosystem.
* **Tooling:** Managed via **Scoop** (installing `k3d`, `kubectl`, `grpcurl`, `maven`, and `openjdk`).
* **Cluster Creation:** Local Kubernetes cluster via **k3d** with port mapping for external browser access.
* **Local Registry:** Dedicated k3d image registry to allow internal image pushing without external internet dependency.

### Phase 2: The Contract (Protobuf & Shared Models)
The source of truth for the entire system.
* **Shared Library:** A central module containing `.proto` files defining the metric messages (CPU, RAM, Hostname) and streaming services.
* **Consistency:** Both **Agent** and **Analyser** import this contract to generate Java code, ensuring zero mismatch in data structures.

### Phase 3: The Agent (Data Collection & gRPC)
The "sensor" service that monitors the host machine.
* **OSHI Integration:** Captures real-time hardware data from the processor and memory.
* **gRPC Streaming:** Implements the server-side logic to push continuous data flows, keeping the connection persistent.

### Phase 4: The Analyser (Processing & Messaging)
The bridge between synchronous RPC calls and asynchronous event-driven messaging.
* **gRPC Client:** Consumes the stream from the **Agent**.
* **Kafka Integration:** Every metric received via gRPC is immediately published to specific Kafka topics.
* **External Monitoring:** **Kafka-UI** runs outside the Kubernetes cluster for real-time message inspection, while the brokers run internally.

### Phase 5: The Traffic Path (Spring Cloud Gateway)
Organizing how external clients interact with the internal ecosystem.
* **Unified Entry Point:** A dedicated Gateway service to handle all incoming requests.
* **Service Discovery:** Integrated with Kubernetes DNS to automatically locate and route traffic to the internal microservices.

### Phase 6: The Delivery (GraphQL & Real-time)
The intelligent interface for the end-user.
* **GraphQL Schema:** Allows clients to query exactly what they need (e.g., just CPU usage or full hardware info).
* **Subscriptions:** Implementation of WebSockets to "push" data from Kafka directly to the user interface in real-time.
* **Resolvers:** Connecting GraphQL queries to Kafka consumers to fetch the latest snapshots.

### Phase 7: Orchestration (Final Kubernetes Deployment)
Transforming individual services into a single resilient gear.
* **Dockerization:** Multi-stage Dockerfiles for all 4 services (Agent, Analyser, Gateway, GraphQL).
* **K8s Manifests:** Writing Deployment and Service YAMLs to manage instances and internal networking.
* **Validation:** Using `kubectl logs` to monitor the gRPC handshakes and message flow across Pods.

---

## 🚀 Technical Stack

| Category | Technology |
| :--- | :--- |
| **Languages** | Java 21+ |
| **Framework** | Spring Boot 3.x, Spring Cloud Gateway |
| **Communication** | gRPC (Protobuf), GraphQL (Subscriptions) |
| **Messaging** | Apache Kafka |
| **Infrastructure** | Kubernetes (k3d), Docker |
| **OS Metrics** | OSHI (Operating System and Hardware Information) |
| **Package Manager** | Scoop (Windows) |

---