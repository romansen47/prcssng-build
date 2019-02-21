#!/bin/bash

mvn clean install sonar:sonar

cd prcssng-temperature
ant -f temperature.xml
cd ..

cd prcssng-gameoflife
ant -f gameoflife.xml
cd ..

cd prcssng-chess
ant -f chess.xml
cd ..

cd prcssng-minesweeper
ant -f minesweeper.xml
cd ..

cd prcssng-Falcon
ant -f falcon.xml
cp -r data target/
cd ..

