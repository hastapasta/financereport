#!/bin/sh
#I couldn't get this to work running startMaxl.sh from a separate directory so 
#ended up doing this less-than-ideal funky stuff with cd'ing to the directory
echo $projdir>>Test.log
cd /home/ollie/tools/hyperion/products/Essbase/EssbaseClient/bin
./startMaxl.sh -l $1 $2 -s $3 ${4:7}/essbase/$5 $6 $7 ${4:7} 
