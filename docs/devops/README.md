# DevOps
In this document we clarify some of our choises on the planning and implementation of CI/CD

### Since this is a mono repo project why not having a single pipeline?
Since we are in an academic context and we want to practice/have the experience of what it is like to automate the build and deploy processes of micro services, we chose to not create a single pipeline for the whole project and to follow "good practices" as well. Also, due to performance issues it would cause to run a single pipeline for the whole project. This way, we can also deploy each microservice independently.

<br>

### Why multiple pipelines per microservice?
Each microservice has its own 3 pipelines, one per each branch and environment: dev, test and prod. Even tho each one of them executes the same process of build and deploy (it just changes the server to where it deploys) we chose this way so we can better identify which branch/environment it belongs a build, this way we can faster identify what's failing and/or doing okay.

<br>

### Pipeline 
Every microservice has the same pipeline structure:
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
 9. Docker build and push
 10. Smoke Test
 11. Send Email & Wait for Approval
 12. Other topics
    1. Runnig in parallel
    2. Plugins used

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

##### Docker build and push & Deloyment
Before we deploy the service we build the docker image and push it to the docker hub.
On the deployment stage we are using the Publish Over SSH plugin to deploy the env file, the docker-compose file, that we use to deploy the microservice into the docker swarm, and the autoscale script to the server.
All the docker image were published to [here](https://hub.docker.com/repositories/raccoonsarmy)

#### Smoke Test
In this stage we are running a simple smoke test to check if the service is running correctly. We are using a simple curl command to call an health check endpoint to check if the service is up and running.

#### Other topics

**Running in parallel**
As we mentioned, we are running some stages in parallel so we can have better pipeline performance. Before having them running in parallel we had a pipeline that took around 4.5 minutes to finish, now it takes around 3.5 minutes. The static code analysis was a big part of the time it took to the pipeline finish, so it was expected to have a better performance after running them in parallel. 

**Plugins used**
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

Each server runs a single docker swarm node which is where we deploy our microservices.

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
We are using docker swarm so by itself it already has a load balancing mechanism. Docker swarm, by default, uses Ingress Load Balancing to distribute the incoming requests to the services in the swarm, where it applies a round-robin strategy to balance the requests between the replicas of the services.
Extra: we also created a simple pipeline to deploy RabbitMQ and Nginx in the dev, test and prod servers.

![Routing & Load Balacing](./assets/routingAndTraffic.svg)


### Auto Scaling
To run the auto scaling we created a cron job that runs every minute and checks if the CPU usage of the docker swarm stack is above 75% and if it is, it scales the service and a H2 database up by 1 replica. If the CPU usage is below 20% it scales the service down and its H2 database by 1 replica. We also applied the same scale logic if the memory usage of the docker swarm stack is above 80% and below 30%.

### JMeter Performance Test
To increase the performance of the LMS System of at least 25% when dealing with an overload peek scenario, we implemented an automatic scaling script in conjunction with the Docker Swarm in order to monitor the resources being used by the services and then scale up or down accordingly.
To verify this performance increase of our system we decided to realize a test using JMeter, a performance testing tool. This test was conducted locally in one of the developers' computer.

For this test we used 4 types of services:
 - **RabbitMQ:** message broker
 - **LMS-Auth+User:** LMS Authentication Microservice(2 instances/replicas)
 - **LMS-Books:** LMS Books Microservice(initially 2 instances/replicas)
 - **Nginx:** load-balancing

Endpoint: GET localhost:9080/api/books/9789896379636
The port 9080 is related to NGINX, which will redirect the requests to the respective instances of the microservice.

The pattern used for the testing was the following:
 - **Start Threads Count:** 100 , the test will start at 0 threads and will rapidly increase the number of threads till we reach 100
 - **Startup Time(sec):** 30 , the time will take to reach the maximum number of threads, in this case 100
 - **Hold Load For(sec):** 120 , amount of time the test will have with maximum load, in this case 100 threads during 120 seconds

#### Important information about the autoscaling script used

Thresholds for CPU and memory usage to scale the service:
 - CPU_UP_THRESHOLD=6000    (CPU usage percentage (scaled by 100) to scale up)
 - CPU_DOWN_THRESHOLD=2000  (CPU usage percentage (scaled by 100) to scale down)
 - MEM_UP_THRESHOLD=5000    (Memory usage percentage (scaled by 100) to scale up)
 - MEM_DOWN_THRESHOLD=2000  (Memory usage percentage (scaled by 100) to scale down)

Minimum and Maximum number of replicas to prevent over-scaling or under-scaling:
 - MIN_REPLICAS=2
 - MAX_REPLICAS=10

The script will run in an infinite loop in periods of 2 seconds to rapidly increase the number of replicas, if the script notices that the CPU or Memory are being heavily used he will add 2 more replicas.

#### Test Prediction/Objective
Our LMS Books Microservice starts with two instances running by default. When we initiate the JMeter test, the load increases rapidly, causing a noticeable spike in CPU and memory usage. As a response, the script instructs Docker Swarm to dynamically scale the service by adding two additional replicas, continuing to adjust as needed.

#### Test Results and Analysis

An image showing the response time (in milliseconds) over the duration of the test.
![JMeter Response Time(ms)](./assets/jmeter_responseTime.png)

An image showing the active threads over the duration of the test.
![JMeter Active Threads](./assets/jmeter_activeThreads.png)

Analyzing the respective images of response time and active threads, we observe that at the 30-second mark of the test, the maximum number of threads (100) was reached. It was around this time that the response time peaked at 600ms.

At this point, we can infer that there were likely 2 to 4 replicas of the LMS Books microservice running. From this moment onward, the script progressively increased the number of replicas in increments of 2, ultimately reaching the maximum of 10 instances by the end of the test.

It is clear that from the 30-second mark onward, the response time steadily decreased as the number of instances increased, demonstrating a significant performance improvement by the LMS Books microservice when handling a very high demand for requests.

It is also worth noting that when the test concluded and the CPU and memory usage significantly decreased, the script began scaling down the replicas. It reduced the number of replicas from 10 back to the minimum of 2, as the high demand for requests subsided. This behavior effectively managed hardware resources in a cost-efficient and optimized manner.