package com.pikefin.services.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pikefin.log4jWrapper.Logs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.pikefin.ApplicationSetting;
import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.NotificationService;
@Service
public class NotificationServiceImpl implements NotificationService{
	@Autowired
	private VelocityEngine velocityEngine;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String generateVelocity(List<HashMap<String, String>> hm,
		String strListName, String emailTemplate) throws GenericException {
		VelocityContext vc = new VelocityContext();
		Template template = velocityEngine.getTemplate(emailTemplate);
		vc.put(strListName, hm);
		StringWriter writer = new StringWriter();
		template.merge(vc,writer);
		return writer.toString();
	}

	public String tweet(String strTweet,String userName, String password, String authentication1, String authentication2, String authentication3, String authentication4) throws GenericException{	
	  	
		String strErrorMsg = "";
		try	{
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setUser(userName);
			cb.setPassword(password);
			cb.setDebugEnabled(true)
			.setOAuthConsumerKey(authentication1)
			.setOAuthConsumerSecret(authentication2)
			.setOAuthAccessToken(authentication3)
			.setOAuthAccessTokenSecret(authentication4);
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();
			if (strTweet.length() > 140) {
			
				ApplicationSetting.getInstance().getStdoutwriter().writeln("Tweet longer than 140 characters; tweet truncated.",Logs.WARN,"UF49.5");
				ApplicationSetting.getInstance().getStdoutwriter().writeln(strTweet,Logs.WARN,"UF49.5");
				strErrorMsg = "Tweet longer than 140 characters; tweet truncated.";
			}
				
			
			if (ApplicationSetting.getInstance().isDebugMode()== false)
				twitter.updateStatus(strTweet);
		
		}
		catch (TwitterException te)	{
		   	ApplicationSetting.getInstance().getStdoutwriter().writeln("Problem sending tweet",Logs.ERROR,"UF48.5");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(te);
			strErrorMsg = te.getMessage();
		}
		return strErrorMsg;
	 
	}
	
}
