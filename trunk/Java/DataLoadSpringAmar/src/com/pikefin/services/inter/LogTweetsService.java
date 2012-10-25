package com.pikefin.services.inter;

import com.pikefin.businessobjects.LogTweets;
import com.pikefin.exceptions.GenericException;

public interface LogTweetsService {

	public LogTweets saveLogTweetsInfo(LogTweets logTweetsEntity) throws GenericException;
	public Integer getTweetCounts(Integer UserId) throws GenericException;


}
