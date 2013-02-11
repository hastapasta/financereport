# show detailed differences between trunk and branch ($1)
#svn diff -r HEAD $1 > /tmp/output
# show summarized differences between trunk and branch
#svn diff -r 1192:HEAD --summarize https://financereport.googlecode.com/svn/FinancialReport/branches/DataLoadEagleAmar
# show differences between 2 files
# svn diff https://financereport.googlecode.com/svn/FinancialReport/branches/DataLoadEagleAmar/src/com/pikefin/services/impl/FactDataServiceImpl.java https://financereport.googlecode.com/svn/FinancialReport/trunk/Java/DataLoadEagle/src/com/pikefin/services/impl/FactDataServiceImpl.java

# get the earliest revision # of a branch
#svn log https://financereport.googlecode.com/svn/FinancialReport/branches/DataLoadEagleAmar | tail
# get the current revision of a branch/file
svn info https://financereport.googlecode.com/svn/FinancialReport/trunk/Java/DataLoadEagle
