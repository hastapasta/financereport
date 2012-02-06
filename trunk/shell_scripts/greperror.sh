#!/bin/ksh
if [[ $1 = "1" ]]; then
grep DG42.2 ~/workspace/DataLoad/*.log
elif [[ $1 = "2" ]]; then
grep "Bad Yahoo Data" ~/workspace/DataLoad/*.log
elif [[ $1 = "3" ]]; then
grep "$2" ~/workspace/DataLoad/*.log
fi
