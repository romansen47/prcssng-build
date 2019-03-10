#!/bin/bash

mvn clean install 
mvn javadoc:javadoc

sonar-scanner

cd prcssng-temperature
ant -f temperature.xml
#mvn sonar:sonar
cd ..

cd prcssng-gameoflife
ant -f gameoflife.xml
#mvn sonar:sonar
cd ..

cd prcssng-chess
ant -f chess.xml
#mvn sonar:sonar
cd ..

cd prcssng-minesweeper
ant -f minesweeper.xml
#mvn sonar:sonar
cd ..

cd prcssng-Falcon
ant -f falcon.xml
#mvn sonar:sonar
cp -r data target/
cd ..

