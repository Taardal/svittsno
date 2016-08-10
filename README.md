# Svitts Movie Library
Webapp for displaying information about your movies with a small java application for playing the movie in a media player locally.

## Modules
There are two modules in the project: ```core``` and ```player```. 
The ```core```-module is the web application itself.
The ```player```-module is a very lightweight standalone java-app that runs on the client machine. This app runs a servlet that accepts a path to a video file. When the servlet receives a path to a video file it will open it in a media player of choice and play it.

## Requirements
* [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Git](https://git-scm.com/downloads) 
* [Maven](https://maven.apache.org/download.cgi)

Recommend using ```apt-get``` (Linux) or ```Chocolatey``` (Windows) for installation by command-line.

## Getting started (Core)
1) Check out sources from GitHub: ```git clone https://github.com/Taardal/svittsno.git```
2) Change folder to ```svittsno/core```
3) Run ```mvn clean verify``` to build the module and run unit and integration tests.
4) Run the application with ```mvn clean jetty:run```.
5) The application runs on ```localhost:8080```. API-docs (Swagger) can be found at ```localhost:8080/swagger```

## Getting started (Player)
* Soon tm...

## Major libraries/frameworks used
* [Jetty](http://www.eclipse.org/jetty/) (Web server)
* [Jersey](https://jersey.java.net/) (REST API)
* [Swagger](http://swagger.io/) (REST API documentation)
* [Mockito](http://mockito.org/) (Mocking in unit/integration tests)
* [Hibernate](http://hibernate.org/) (Database management)
* [Guice](https://github.com/google/guice/wiki/GettingStarted) (Dependency injection)

