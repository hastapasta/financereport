rm -f /home/ollie/scripts/backup/translate*.gz
set hostname=pikefin3
mysqldump --host=pikefin3 --user=root --password=madmax1. translate | gzip > /home/ollie/scripts/backup/translate_$(date +%s).sql.gz

