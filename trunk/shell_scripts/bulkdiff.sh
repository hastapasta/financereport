#diff -x *.png -Nar /var/www/html/cakepftest/app /tmp/cakepftest/app > /tmp/diffoutput
#diff -x *.png -Nar /var/www/html/PHP/charts /tmp/PHP/charts > /tmp/diffoutput
#diff -x *.png -Nar /var/www/html/PHP/common /tmp/PHP/common > /tmp/diffoutput
#diff -x *.png -Nar /var/www/html/PHP/site /tmp/PHP/site > /tmp/diffoutput

#diff -Na /var/www/html/cakepftest/$1 /tmp/cakepftest/$1 > /tmp/diffoutput
diff -Na /tmp/mysqlawsvar /tmp/mysqlgodadvar > /tmp/mysqloutput
#diff -Na /var/www/html/PHP/site/includes/sitecommon.php /tmp/PHP/site/includes/sitecommon.php > /tmp/diffoutput
#grep -a diff /tmp/diffoutput > /tmp/grepoutput
