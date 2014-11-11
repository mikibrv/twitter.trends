@ECHO OFF 
CALL mvn clean install
cd webapp
CALL mvn jetty:run
CALL cd ..