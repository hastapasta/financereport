package com.pikefin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikefin.businessobjects.LogTweets;
import com.pikefin.dao.inter.LogTweetsDao;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.LogTweetsService;

@Service
public class LogTweetsServiceImpl implements LogTweetsService{
	@Autowired
	private LogTweetsDao logTweetDao;
	@Override
	public LogTweets saveLogTweetsInfo(LogTweets logTweetsEntity)
			throws GenericException {
		return logTweetDao.saveLogTweetsInfo(logTweetsEntity);
	}
	@Override
	public Long getTweetCounts(Integer userId) throws GenericException {
		return logTweetDao.getTweetCounts(userId);
	}

}
