JAVALIB=/home/ollie/dev/java/libraries
CURRENTDIR=/home/ollie/workspace/BuildViews
CLASSPATH=$CURRENTDIR:$JAVALIB/commons-io-1.4/commons-io-1.4.jar:$JAVALIB/httpcomponents-client-4.0.3/lib/httpclient-4.0.3.jar:$JAVALIB/httpcomponents-client-4.0.3/lib/httpmime-4.0.3.zip:$JAVALIB/httpcomponents-core-4.0.1/lib/httpcore-4.0.1.jar:$JAVALIB/commons-logging-1.1.1/commons-logging-1.1.1.jar:$JAVALIB/apache-log4j-1.2.16/log4j-1.2.16.jar:$JAVALIB/mysql-connector-java-5.1.12-bin.jar
#java -classpath $CLASSPATH pikefin/BuildViews $1 $2  2>$CURRENTDIR/error 
java -classpath $CLASSPATH pikefin.BuildViews $1
