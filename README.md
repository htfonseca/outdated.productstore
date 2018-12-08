# Simple Store API Backend Application
This application provides backend RESTful services for handling 
for a simple store of products.

It support two API:
1) A product API were we can add and update products.
2) A order API were we can use the products to create orders.

## Starting the application

### By Gradle tasks (optional way)
The application can also be started using the folder gradle task:

``
./gradlew clean bootRun
``
 
Also the following task are also available:
- ./gradlew clean - clean the project folder  
- ./gradlew build - build the project and the corresponding tests
- ./gradlew asciidoctor - use the test snippet results to generate the documentation

### By using the IDE
The application is started by just running the class:

``
com.myprojects.spring.productstore.StoreApplication
``

### By running .jar file
The last way to start the application is by running the jar file.

``
com.myprojects.spring.productstore.StoreApplication
``






-------
## Accessing the backend
The backend application is configured by default to run under port _8080_.
This can be reconfigured by setting the property __server.port__ in _application.yml_ file.


-------



To open the web application just locate your web browser to [localhost:8080](http://localhost:8080).

To access the entry point to tasks REST api locate the web browser to [localhost:8080/v1/tasks](http://localhost:8080/v1/tasks).<br>
To access the entry point to tasks REST api (filtered by assignee) locate the web browser to [localhost:8080/v1/tasks?assignee=xyz](http://localhost:8080/v1/tasks?assignee=xzz).

## Accessing the documentation for task REST api
To access the api documentation just open your web browser on [localhost:8080/docs/api-guide.html](http://localhost:8080/docs/api-guide.html)

Please note that this only works when starting the packaged spring boot jar file
or when accessing the deployed backend in clodufoundry

## Monitoring access points
By default there are provided several monitoring endpoints on same port _8080_.
This port can also be reconfigured using property __management.port__ in _application.properties_ file.

Access points here are:

- [Show application health info (is application alive?)](http://localhost:8080/health)
- [Show application info](http://localhost:8080/info)
- [Show all active configuration properties](http://localhost:8080/configprops)
- [List all available monitoring (actuator) endpoints](http://localhost:8080/actuator)

## Deploy the application to CloudFoundry
To deploy the application to e.g. local [PCFDev](https://docs.pivotal.io/pcf-dev/) use the following command

``
cf push tasks -p [path to your workspace]/smartsite-tasks/build/libs/smartsite-tasks-0.0.1-SNAPSHOT.jar
``

After successful push the tasks backend application can be accessed using [http://tasks.local.pcfdev.io](http://tasks.local.pcfdev.io) on PCFDev.
## Architecture
All backend applications follow the _microservice_ archtictural style using [spring boot](http://projects.spring.io/spring-boot/).
Further it is build upon a full stack architecture (with RESTful api layer on top).

We will use the [Entity-Control-Boundary Pattern](http://epf.eclipse.org/wikis/openup/core.tech.common.extend_supp/guidances/guidelines/entity_control_boundary_pattern_C4047897.html) for separating the layers.
This pattern will then be mapped to the spring stereotypes __RestController__, __Service__ and __Repository__. Dependent on which persistence technology is used the data layer
will be represented by JPA __Entity__ classes, MongoDB __Document__ classes etc.

## Profiles
Without any profile the backend application starts with security on by default.
I.e. to access any REST Api you need a valid JWT token. Otherwise you get a 401 status.

Currently the application can be started using following profiles:

- _dev_: This profile disables security of tasks backend completely
