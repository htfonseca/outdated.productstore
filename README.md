# Simple Store API Backend Application
This project serves as an example of a  Spring Boot RESTful application, which 
represents a simple store can contain products and orders. 

It contains two API:
1) A product API were the user can add and update products.
2) A order API were the user can use the products to create orders, with parcels.

## Starting the application

### By Gradle tasks (optional way)
The application can be started using the following gradle task:

``
./gradlew clean bootRun
``
 
Also are also the available tasks:
- ./gradlew clean - clean the project folder  
- ./gradlew build - build the project and the corresponding tests
- ./gradlew asciidoctor - use the test snippet results to generate the documentation

### By using the IDE
The application can be started by just running the main class:

``
com.myprojects.spring.productstore.StoreApplication
``

### By running .jar file
The last way to start the application is by running the jar file
with the following command in the terminal:

``
java -jar productstore-0.0.1.jar
``

## Accessing the backend
The backend application is configured by default to run under port _8080_.<br>
This can be reconfigured by setting the property __server.port__ in _application.yml_ file.

### Access Points
There are two main access points for the backend application when it is running locally:

To access the base entry point to product REST api the following link can be use:<br>
[localhost:8080/store/products](http://localhost:8080/store/products).

To access the base entry point to orders REST api the following link can be use:<br> 
[localhost:8080/store/orders](http://localhost:8080/store/orders).

## Accessing the documentation REST api
To access the api documentation just open your web browser on:<br>
[localhost:8080/docs/api-guide.html](http://localhost:8080/docs/api-guide.html)

Please note that this only works when starting the packaged:
1) Using the spring boot jar generated.
2) Using the spring boot runner (./gradlew bootRun)

It is also possible to read the documentation without starting the application using the following 
package gradle task:

``
./gradlew asciidoctor
``

This task will generate the documentation static files on the project folder 
_build/asciidoc/html5_.

## Accessing the store solution
This application uses a simples generic h2 memory database for simplicity propose.
To access the UI of it just open your web browser on:<br>
[localhost:8080/console](http://localhost:8080/console)

The configuration used to access should be the following:
```
Driver Class: org.h2.Driver
JDBC URL:     jdbc:h2:mem:store 	    
User Name:    sa
Password:
```

## Monitoring access points
No monitoring access point were included.

## Architecture
TODO