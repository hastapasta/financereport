WORKINGDIR=/home/ollie
rm $WORKINGDIR/scripts/backup/DataLoadLogs*.gz
tar -cvf $WORKINGDIR/scripts/backup/DataLoadLogs_$(date +%s).tar $WORKINGDIR/workspace/DataLoad/*log*
gzip $WORKINGDIR/scripts/backup/*.tar

