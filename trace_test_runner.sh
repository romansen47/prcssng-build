#!/bin/bash

mvn clean install -DskipTests=true

cd definitions/target
java -classpath definitions-1.0.0-SNAPSHOT.jar:deploy/lib/* definitions.TestRunner