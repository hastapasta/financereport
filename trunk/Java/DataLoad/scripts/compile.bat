@echo on
set javahome=C:\progra~1\java\jdk1.6.0_18
set classpath=.;C:\dev\java\ServiceTest;%javahome%;C:\dev\java\ServiceTest\com\roeschter\jsl;C:\progra~1\MySQL\mysql-connector-java-5.1.12-bin.jar;C:\dev\java\libraries\commons-logging-1.1.1\commons-logging-1.1.1.jar;C:\dev\java\libraries\httpcomponents-client-4.0.3\lib\httpclient-4.0.3.jar;C:\dev\java\libraries\httpcomponents-core-4.0.1\lib\httpcore-4.0.1.jar

if "%1"=="All" goto all
javac -classpath "%classpath%" com\roeschter\jsl\%1
goto end




:all
javac -classpath "%classpath%" com\roeschter\jsl\DataLoad.java
javac -classpath "%classpath%" com\roeschter\jsl\DataGrab.java
javac -classpath %classpath% com\roeschter\jsl\ProcessingFunctions.java
javac -classpath %classpath% com\roeschter\jsl\UtilityFunctions.java
javac -classpath %classpath% com\roeschter\jsl\DBImport.java
javac -classpath %classpath% com\roeschter\jsl\CustomBufferedWriter.java
javac -classpath %classpath% com\roeschter\jsl\Logs.java
javac -classpath %classpath% com\roeschter\jsl\MoneyTime.java

:end