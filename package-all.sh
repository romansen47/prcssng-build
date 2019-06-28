#!/bin/bash

mvn clean install sonar:sonar -DskipTests=true

cd prcssng-temperature
ant -f temperature.xml
mvn sonar:sonar
cd ..

cd prcssng-gameoflife
ant -f gameoflife.xml
mvn sonar:sonar
cd ..

cd prcssng-chess
ant -f chess.xml
mvn sonar:sonar
cd ..

cd prcssng-minesweeper
ant -f minesweeper.xml
mvn sonar:sonar
cd ..

cd prcssng-Falcon
ant -f falcon.xml
mvn sonar:sonar
cp -r data target/
cd ..

cd definitions
mvn clean test
cd ..

