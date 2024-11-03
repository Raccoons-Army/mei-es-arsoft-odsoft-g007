# ISEP - MEI - ES - ARSOFT & ODSOFT

## Folder structure
- [`lms/`](https://github.com/Raccoons-Army/mei-es-arsoft-odsoft-g007/tree/main/lms) contains the Library Management REST API and all its documentation.
- [`lms/Docs/`](https://github.com/Raccoons-Army/mei-es-arsoft-odsoft-g007/tree/main/lms/Docs) has:
  - [`baseProject/`](https://github.com/Raccoons-Army/mei-es-arsoft-odsoft-g007/tree/main/lms/Docs/baseProject) where there's all the documentation made by the students that developed the API
  - [`SystemAnalysis/`](https://github.com/Raccoons-Army/mei-es-arsoft-odsoft-g007/tree/main/lms/Docs/SystemAnalysis) where there's all the documentation made by us. Contains the Logic Views, Implementation Views, Physical Views and Process Views from the SAI (system-as-is) and STB (system-to-be)

# Index
- [SAI (system-as-is)](#sai-system-as-is)
    - Level 1
    - Level 2
    - Level 3
- [STB (system-to-be)](#stb-system-to-be)
    - Level 1
    - Level 2
    - Level 3
- [ASR](#asr)
- [Architectural design alternatives and rational](#architectural-design-alternatives-and-rational)


## SAI (system-as-is)
The way the system was presented to use at the begining of the project.

## STB (system-to-be)
The way we've change the system to the point that it is now.

## ASR

## Architectural design alternatives and rational

### Tactics

In our project, we implemented **Modifiability** by **Reducing Coupling** between components. Below, we describe how we applied the tactics of **Encapsulation**, **Using an Intermediary**, and **Abstracting Common Services**.

#### 1. Encapsulation

#### Implementation
- **Interface Definition**: Defining an interface by encapsulating common behaviors. The interface defines common methods, but does not expose the underlying implementation details of how these operations are carried out for different implementations.
- **Implementation Classes**: Each implementation class encapsulates the specific details of operations for its respective type.
#### Example
- BookRepository interface defines common methods implemented by the JpaBookRepoImpl and MongoBookRepoImpl like save, findById, findALl, and more specific ones.
- IamAuthentication interface defines common methods implemented by the GoogleAuth and FacebookAuth.
---

#### 2. Use an Intermediary

#### Implementation
- **Service Layer as Intermediary**: The service layer acts as an intermediary that communicates with the interface. It invokes methods on the Interface without needing to know which implementation class is being used.
- **Dynamic Configuration**: The intermediary service can dynamically determine which implementation to instantiate based on the current context. This involves using a configuration class that returns the appropriate repository implementation at runtime based on the configuration.

#### Example
- BookService knows communicates with BookRepository interface but dont know the actually implementation of it.
- BookService receives the respective BookRepository implementation through Dependency Injection at runtime.

---

#### 3. Abstract Common Services

#### Implementation
- **Define Interfaces**: Create abstract classes or interfaces that specify the methods for common functionalities, such as data access, authentication.
- **Concrete Implementations**: Develop multiple concrete classes that implement these interfaces, each tailored to specific use cases or requirements (e.g., different database access strategies).
- **Dependency Injection** Use dependency injection to provide the concrete implementations to the components that require them, allowing for easy swapping.
---

### Architectures

#### Onion Architecture

#### 1. Core Domain (Innermost Layer)
- **Purpose**: Contains the fundamental business logic, entities, and domain models.
- **Contents**: Domain entities, value objects, and domain-specific logic.

#### 2. Application Services (Next Layer)
- **Purpose**: Contains application-specific business rules. This layer defines the use cases of the application and orchestrates the domain layer to fulfill application requests.
- **Contents**: Service interfaces and classes, use cases, and application logic.

#### 3. Interfaces/Adapters (Outer Layer)
- **Purpose**: Acts as a bridge between the application and external resources, such as databases, user interfaces, and third-party services.
- **Contents**: Controllers, implementations for data repositories, IAM etc.

#### 4. External Infrastructure (Outermost Layer)
- **Purpose**: Contains any external dependencies that interact with the application, such as database configurations, message queues, and API clients.
- **Contents**: Infrastructure configurations, database connection settings.
---

#### Modular Monolith Architecture
- The project demonstrates elements of a modular monolith architecture by organizing the system into domain-specific packages. However, these packages still contain significant dependencies on each other, resulting in notable coupling between components.
---

### Alternatives of Design

#### Event-Driven Architecture
**Overview:** Architecture where components communicate through events, allowing for decoupling of services and flexibility in adding new implementations.

**Implementation:**
- Define an event bus where services can publish and subscribe to events related to database operations, IAM actions, etc.
- Each implementation listens for relevant events and responds accordingly.
- Use configuration to determine which services are active and how they respond to events.

**Benefits:**
- Highly decoupled architecture, enabling flexible and scalable systems.
- New services can be added or modified without impacting existing ones.

**Challenges:**
- Complexity in managing event flow and ensuring event delivery.
- Debugging can become more challenging as the flow of events may not be linear.
---