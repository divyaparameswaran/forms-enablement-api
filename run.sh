#!/usr/bin/env bash

mvn clean package
java -jar target/formsapiservice-1.0-SNAPSHOT.jar server configuration.yml