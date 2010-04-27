@echo on
set javahome=C:\progra~1\java\jdk1.6.0_18
set classpath=.;%javahome%;D:\dev\java\ServiceTest
PATH=%PATH%;%javahome%\bin
if "%1"=="All" goto all
javac -classpath "%classpath%" com\roeschter\jsl\%1
goto end




:all
javac -classpath "%classpath%" com\roeschter\jsl\DataLoad.java
javac -classpath "%classpath%" com\roeschter\jsl\DataGrab.java
javac -classpath %classpath% com\roeschter\jsl\ProcessingFunctions.java
javac -classpath %classpath% com\roeschter\jsl\UtilityFunctions.java

:end