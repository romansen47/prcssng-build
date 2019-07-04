set mvn=C:\"Program Files"\apache-maven-3.6.0\bin\mvn.cmd
set ant=C:\"Program Files"\apache-ant-1.10.5\bin\ant.cmd

call mvn clean install -DskipTests=true
call mvn sonar:sonar

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
xcopy /Y data target\data
cd..

call mvn javadoc:javadoc test