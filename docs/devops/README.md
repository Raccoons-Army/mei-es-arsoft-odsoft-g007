# DevOps
In this document we clarify some of our choises on the planning and implementation of CI/CD

### Since this is a mono repo project why not having a single pipeline?
Since we are in an academic context and we want to practice/have the experience of what it is like to automate the build and deploy processes of micro services, we chose to not create a single pipeline for the whole project and to follow "good practices" as well. Also, due to performance issues it would cause to run a single pipeline for the whole project. This way, we can also deploy each microservice independently.

<br>

### Why multiple pipelines per microservice?
Each microservice has its own 3 pipelines, one per each branch and environment: dev, test and prod. Even tho each one of them executes the same process of build and deploy (it just changes the server to where it deploys) we chose this way so we can better identify which branch/environment it belongs a build, this way we can faster identify what's failing and/or doing okay.

<br>

### Pipeline 
Every microservice has the same pipeline structure, it is divided in 8 stages:
1. Checkout
2. Clean & Compile
3. Static Code Analysis
    3.1. PMD: best practices, design, error prone and performance
    3.2. SpotBugs
4. Tests
    4.1. Unit Tests
    4.2. Integration Tests
    4.3. Mutation Tests
5. Generate Coverage Report
6. Packaging
7. Prepare to Deploy
8. Deploy

Now, we will explain some of the stages in more detail.

##### Static Code Analysis
We are using PMD and SpotBugs to analyze our code. PMD is a source code analyzer that finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, and so forth. We decided to use the following rules: 
- best practices: rules that enforce good programming practices
- design: rules that enforce good design and implementation
- error prone: rules that detect constructs that are either broken, extremely confusing or prone to runtime errors
- performance: rules that flag suboptimal code

This rules were chosen because we considered them the most important ones and sufixient to have a good code quality for this project.

SpotBugs uses static analysis to look for bugs and vulnerabilities in our code.

Finally, so we can have better pipeline performance, and since they aren't dependent of each other, we are running both of them in parallel.

##### Tests
We are running 3 types of tests: unit tests, integration tests and mutation tests. To have better pipeline performance and since they aren't dependent of each other we are running them in parallel.

##### Deloyment TODO
- ...
- ...

<br>

### Servers <small><small>(prod, test and dev)</small></small>
Our enviroments are running on LXD containers on a on-premise server named **rac-server**. This way we can freely manage and test (*without performance issues*) our different enviroments and simulate a real world scenario (at least a bit)

![Deployment Servers](./assets/deploymentServers.svg)

Each server runs k3s ? ...
...
![Other diagram](./assets/otherdiagram.svg)

Pros and cons of this infrastructure:
- Pros:
    - full access and control of the infrastructure
    - responsible and full control over the security of the infrastructure
    - responsible and full control over the performance of the infrastructure
    - responsible and full control over our data
    - scaling vertically is possible
    - having the environments running on LXD containers allows us have better isolation between them and to allocate them in the same server
- Cons:
    - can only scale horizontally since we only have a single server handling all of our infrastructure
    - single point of failure for the same reason as above