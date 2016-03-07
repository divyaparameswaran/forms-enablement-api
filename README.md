Companies House Forms API Service
=====================

The tool accepts forms posted from Salesforce as JSON, converts them to XML and posts them to Companies House Information Processing System (CHIPS). 

### About this application

This application is written using the [Dropwizard](http://www.dropwizard.io/) Java framework.

### Prerequisites

In order to run the tool locally you'll need the following installed on your machine, alternatively you can provision a virtual machine
using Vagrant [here](#vagrant).

- [Java](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Git](https://git-scm.com/downloads)

### Getting Started

Run the following from the command line to download the repository and change into the directory:

```
git clone git@github.com:companieshouse/extractives-service.git

cd extractives-service
```


### Running the development server

To run the server type the following command in your terminal

```bash
./run.sh
```

or on windows

```
mvn clean package
java -jar target/formsapiservice-1.0-SNAPSHOT.jar server configuration.yml

```
Then you can now visit [http://localhost:8080/](http://localhost:8080/) in your browser

```
