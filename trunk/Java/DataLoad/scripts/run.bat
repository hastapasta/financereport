set javahome=C:\progra~1\java\jdk1.6.0_18
set classpath=.;C:\dev\java\ServiceTest;%javahome%;C:\dev\java\ServiceTest\com\roeschter\jsl;C:\progra~1\MySQL\mysql-connector-java-5.1.12-bin.jar
set PATH=%PATH%;%javahome%\bin
java -classpath "%classpath%" -Dservice.inifile=C:\dev\java\ServiceTest\jsl.ini com.roeschter.jsl.DataLoad