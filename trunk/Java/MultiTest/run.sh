JAVALIB=/home/ollie/dev/java/libraries
CLASSPATH=.:$JAVALIB/commons-io-1.4/commons-io-1.4.jar:$JAVALIB/httpcomponents-client-4.0.3/lib/httpclient-4.0.3.jar:$JAVALIB/httpcomponents-client-4.0.3/lib/httpmime-4.0.3.zip:$JAVALIB/httpcomponents-core-4.0.1/lib/httpcore-4.0.1.jar:$JAVALIB/commons-logging-1.1.1/commons-logging-1.1.1.jar
java -classpath $CLASSPATH MultiTest $1 $2  2>error 
