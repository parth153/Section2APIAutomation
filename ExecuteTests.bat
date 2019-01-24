@echo on
::navigate to the maven bin directory

SET PROJECT_HOME=%cd%

cd .\Maven\bin
::clean the target folder
call mvn clean -f %PROJECT_HOME%\Section2APIAutomation\pom.xml
::compile the tests
call mvn compile -f %PROJECT_HOME%\Section2APIAutomation\pom.xml
::install the dependencies and run the test
call mvn install -f %PROJECT_HOME%\Section2APIAutomation\pom.xml

pause