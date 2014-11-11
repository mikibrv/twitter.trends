@ECHO OFF 
CALL mvn clean install
cd core
CALL mvn exec:java
cd ..