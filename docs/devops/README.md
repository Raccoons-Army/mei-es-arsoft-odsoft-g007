# DevOps
In this document we clarify some of our choises on the planning and implementation of CI/CD

### Since this is a mono repo project why not having a single pipeline?
Since we are in an academic context and we want to practice/have the experience of what it is like to automate the build and deploy processes of micro services, we chose to not create a single pipeline for the whole project and to follow "good practices" as well.

### Why multiple pipelines per microservice?
Each microservice has its own 3 pipelines, one per each branch and environment: dev, test and prod. Even tho each one of them executes the same process of build and deploy (it just changes the server to where it deploys) we chose this way so we can better identify which branch/environment it belongs a build, this way we can faster identify what's failing and/or doing okay.

### Build TODO
- compile
- run ...
- creates jar
- ...

### Deloyment TODO
- ...
- ...

### Servers <small><small>(prod, test and dev)</small></small>
Our enviroments are running on docker containers on a on-premise server named **rac-server**. This way we can freely manage and test (*without performance issues*) our different enviroments and simulate a real world scenario (at least a bit)

![Deployment Servers](./assets/deploymentServers.svg)

</br>

Each server runs k3s ? ...
...
![Other diagram](./assets/otherdiagram.svg)