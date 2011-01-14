#!/bin/sh
#This command file is used to run Essbase JAPI examples.
#You can change the following varibles to suit your setup.
HYPERION_HOME=/home/ollie/tools/hyperion
ESS_ES_HOME=/home/ollie/tools/hyperion/products/Essbase/EssbaseClient/JavaAPI
#JAVA_HOME=$HYPERION_HOME/common/JRE/Sun/1.5.0
JAVA_HOME=/usr
THIRDPARTY=$ESS_ES_HOME/external
CLASSPATH=/home/ollie/workspace/eis:$ESS_ES_HOME/lib/ess_japi.jar:$EISPATH/ess_japi.jar
echo $CLASSPATH
#set WEBLOGIC_HOME=C:\program_files\weblogic
USER=admin
PASSWORD=madmax1.
DOMAIN=essbase
EES_SERVER=oracleepm
OLAP_SERVER=oracleepm
APP_NAME=Sample
CUBE_NAME=Fed
ROOT_MEMBER_NAME="Measures","Regions","Year"
#Assuming tcpip
#if "%1"=="" goto tcpip
#if "%1" == "tcpip" goto tcpip
#if "%1" == "http" goto http
#if "%1" == "corba" goto corba
#if "%1" == "ejb" goto ejb
#goto usage
#:tcpip
ORB=tcpip
PORT=5001
#goto run
#:http
#set ORB=http
#set PORT=7001
#goto run
#:corba
#set ORB=corba
#set PORT=0
#set CLASSPATH=.;%CLASSPATH%;%ESS_ES_HOME%\lib\ess_es_server.jar;%THIRDPARTY%\visibroke\vbjorb.jar;
#goto run
#:ejb
#set ORB=ejb
#set PORT=7001
#set CLASSPATH=%CLASSPATH%;%ESS_ES_HOME%\lib\ess_es_server.jar;%WEBLOGIC_HOME%\classes;%WEBLOGIC_HOME%\lib\weblogicaux.jar;
#goto run
#:run
#REM echo Step-1: Ready to compile all the examples ...
#REM pause 
#REM "%JAVA_HOME%\bin\javac" *.java -d .
#REM echo Step-2: Ready to run Data Query example ...
#REM pause  
$JAVA_HOME/bin/java -classpath $CLASSPATH TestEIS $USER $PASSWORD $DOMAIN $EES_SERVER $ORB $PORT $OLAP_SERVER $APP_NAME $CUBE_NAME $ROOT_MEMBER_NAME
#echo Step-3: You can include to run the rest of the examples in similary way.
#goto end
#:usage
#echo Usage: runsamples [tcpip or corba or http or ejb]
#echo          where
#echo             tcpip   - use tcpip to run the samples.
#echo             corba   - use corba to run the samples. You need to download your own copy of Visibroker.
#echo             http    - use http to run the samples. You need to have run EES in a web container before.
#echo             ejb     - use ejb to run the samples. You need to have run EES in a EJB #container before.
#goto end
#:end
#pause
