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
     1. PMD
         1. best practices
         2. design
         3. error prone
         4. performance
     3. SpotBugs
 4. Tests
     1. Unit Tests
     2. Integration Tests
     3. Mutation Tests
 5. Generate Reposrts
     1. Surefire and Failsafe
     2. Jacoco
 6. Packaging
 7. Prepare to Deploy
 8. Deploy
 9. Runnig in parallel
 10. Plugins used

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
We are running 3 types of tests: unit tests, integration tests and mutation tests. Even tho they aren't all dependent of each other, we are running them in sequence because it doen't make sense to run the integration tests before the unit tests, and the mutation tests are dependent of the unit tests. Without forgeting that we must run them in sequence because of the generation of the reports.

##### Generate Reports
In this stage we are generating the reports of the tests we ran in the previous stage. We are using Surefire and Failsafe to generate the reports of the unit and integration tests, and Jacoco to generate the coverage report. To obtain better performance and because they aren't dependent on each other, we are running them in parallel as well.
For a plus, we also publish the HTML of the reports for the old school guys that like to see the reports in a more simple way.

##### Deloyment TODO
- ...
- ...
All the docker image were published to [here](https://hub.docker.com/repositories/raccoonsarmy)

##### Running in parallel
As we mentioned, we are running some stages in parallel so we can have better pipeline performance. Before having them running in parallel we had a pipeline that took around 4.5 minutes to finish, now it takes around 3.5 minutes. The static code analysis was a big part of the time it took to the pipeline finish, so it was expected to have a better performance after running them in parallel. 

##### Plugins used
This were the plugins we used to implement the pipeline:

Deployment:
- [Publish Over SSH](https://plugins.jenkins.io/publish-over-ssh/)
- [Docker Pipeline](https://plugins.jenkins.io/docker-workflow/)

Reports:
- [Coverage](https://plugins.jenkins.io/coverage/)
- [JUnit](https://plugins.jenkins.io/junit/)
- [Warnings](https://plugins.jenkins.io/warnings-ng/)
- [HTML Publisher](https://plugins.jenkins.io/htmlpublisher/)

<br>

### Servers <small><small>(prod, test and dev)</small></small>
Our enviroments are running on LXD containers on a on-premise server named **rac-server**. This way we can freely manage and test (*without performance issues*) our different enviroments and simulate a real world scenario (at least a bit)

![Deployment Servers](./assets/deploymentServers.svg)

Each server runs docker swarm TODO ...

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

Check the [README](../../serversConfig/README.md) for more information on the servers configuration.


### Routing & Load Balancing
We are using a reverse proxy to route the requests to the correct microservice. We are using Nginx as the reverse proxy and we use the same configuration file for each environment for simplicity.
We are using docker swarm so by itself it already has a load balancing mechanism. We are using the round-robin strategy to balance the requests between the replicas of the services.
Extra: we also created a simple pipeline to deploy RabbitMQ and Nginx in the dev, test and prod servers.

![Routing & Load Balacing](./assets/routingAndTraffic.svg)


### Auto Scaling TODO
falar acerca do que faz auto scaling, como esta configurado, etc
falar sobre q no deployment ativamos o auto scaling e criamos um cron job que corre a cada minuto
