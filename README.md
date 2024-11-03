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

In our project, we implemented **Modifiability** by **Reducing Coupling** between components. Below, we describe how we applied the tactics of **Encapsulation**, **Using an Intermediary**, and **Abstracting Common Services**.

### 1. Encapsulation

#### Implementation
- **Interface Definition**: Defining an interface by encapsulating common behaviors. The interface defines common methods, but does not expose the underlying implementation details of how these operations are carried out for different implementations.
- **Implementation Classes**: Each implementation class encapsulates the specific details of operations for its respective type.
---

### 2. Use an Intermediary

#### Implementation
- **Service Layer as Intermediary**: The service layer acts as an intermediary that communicates with the interface. It invokes methods on the Interface without needing to know which implementation class is being used.
- **Dynamic Configuration**: The intermediary service can dynamically determine which implementation to instantiate based on the current context. This involves using a configuration class that returns the appropriate repository implementation at runtime based on the configuration.
---

### 3. Abstract Common Services

#### Implementation
- **Define Interfaces**: Create abstract classes or interfaces that specify the methods for common functionalities, such as data access, authentication.
- **Concrete Implementations**: Develop multiple concrete classes that implement these interfaces, each tailored to specific use cases or requirements (e.g., different database access strategies).
- **Dependency Injection** Use dependency injection to provide the concrete implementations to the components that require them, allowing for easy swapping.
---

