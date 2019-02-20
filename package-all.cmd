set mvn=C:\"Program Files"\apache-maven-3.6.0\bin\mvn.cmd

call mvn clean install

cd prcssng-temperature
call ant -f temperature.xml
cd ..

cd prcssng-gameoflife
call ant -f gameoflife.xml
cd ..

cd prcssng-chess
call ant -f chess.xml
cd ..

cd prcssng-minesweeper
call ant -f minesweeper.xml
cd..

cd prcssng-Falcon
call ant -f falcon.xml
xcopy data target\data
cd..