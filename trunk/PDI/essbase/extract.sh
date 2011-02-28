#!/bin/bash
projdir=${1:7}

line=$(cat $projdir/essbase/variable.txt)
#echo $line

line=${line#*curperiod}
#printf "$line\n"

line=${line#*curperiod}
#printf "$line\n"

line=${line#*\"}
DB3=${line%%\"*}

#export DB3=\"$DB3\"

line2=$(cat $projdir/fed_bs_date.csv)

line2=${line2#*Year,}

DB4=${line2%%,*}

#export DB4=\"$DB4\"
echo $1>>Test.log

cd /home/ollie/tools/hyperion/products/Essbase/EssbaseClient/bin
./startMaxl.sh $projdir/essbase/setsubvar.mxl $2 $3 $4 $5 $6 $DB3 $DB4 $projdir


