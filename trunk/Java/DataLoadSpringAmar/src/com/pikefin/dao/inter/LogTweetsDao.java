package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.LogTweets;
import com.pikefin.exceptions.GenericException;

public interface LogTweetsDao {
	public LogTweets saveLogTweetsInfo(LogTweets logTweetsEntity) throws GenericException;
	public LogTweets updateLogTweetsInfo(LogTweets logTweetsEntity) throws GenericException;
	public Boolean deleteLogTweetsInfo(LogTweets logTweetsEntity ) throws GenericException;
	public Boolean deleteLogTweetsInfoById(Integer logTweetId ) throws GenericException;
	public LogTweets loadLogTweetsInfo(Integer logTweetId) throws GenericException;
	public List<LogTweets> loadAllLogTweets() throws GenericException;
}
