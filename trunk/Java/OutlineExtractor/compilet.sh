#! /bin/bash
EISPATH=/home/ollie/tools/hyperion/products/Essbase/EssbaseClient/JavaAPI/lib
$EISPATH
CLASSPATH=$EISPATH/ess_es_server.jar:$EISPATH/ess_japi.jar:$EISPATH/classes12.zip
WORKDIR=/home/ollie/workspace/eis
echo $CLASSPATH
echo $WORKDIR
/usr/bin/javac -classpath $CLASSPATH $WORKDIR/TestEIS.java
