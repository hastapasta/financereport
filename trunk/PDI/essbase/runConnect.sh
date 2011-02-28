JAVA_HOME=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64
ESSJAPILIB=/home/ollie/tools/hyperion/products/Essbase/EssbaseClient/JavaAPI/lib
CLASSPATH=.:$ESSJAPLIB/ess_es_server.jar:$ESSJAPILIB/ess_japi.jar
$JAVA_HOME/bin/java -classpath $CLASSPATH Connect $1 $2 $3 $4 $5 ${6:7} 1>runconnectout.log 2>runconnecterr.log
