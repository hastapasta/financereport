package com.pikefin;

import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.shorten;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pikefin.exceptions.CustomRegexException;
import com.pikefin.exceptions.TagNotFoundException;
import com.rosaloves.bitlyj.Url;

import pikefin.log4jWrapper.Logs;

public class PikefinUtil{

	public static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static <T> T[]  clearArray(T[] arrayToClear){
		for(Object obj:arrayToClear){
		obj=null;
		}
		return arrayToClear;
	}
	
	public static int regexSeekLoop(String regex, Integer nCount, Integer nCurOffset, String strHayStack)
			throws TagNotFoundException {
				
				
				Pattern pattern = Pattern.compile(regex);
				
				Matcher matcher = pattern.matcher(strHayStack);
				if(nCount!=null)
				for (int i = 0; i < nCount; i++) {
				
					if (matcher.find(nCurOffset) == false)
					// Did not find regex
					{
						/* Let whoever catches this decide what to write to the logs. */
						ApplicationSetting.getInstance().getStdoutwriter().writeln("Regex search exceeded.",Logs.ERROR,"DG2");
						throw new TagNotFoundException();
					}
				
					nCurOffset = matcher.start() + 1;
				
					ApplicationSetting.getInstance().getStdoutwriter().writeln("regex iteration " + i
							+ ", offset: " + nCurOffset, Logs.STATUS2, "UF3");
				
				}
				return (nCurOffset);
			}
	
	public static String regexSnipValue(String strBeforeUniqueCode,
			String strAfterUniqueCode, int nCurOffset,String strHayStack)
			throws CustomRegexException {

		Pattern pattern;
		String strDataValue = "";
		int nBeginOffset;
		Matcher matcher;

		if ((strBeforeUniqueCode != null)
				&& (strBeforeUniqueCode.isEmpty() != true)) {
			String strBeforeUniqueCodeRegex = "(" + strBeforeUniqueCode + ")";
			ApplicationSetting.getInstance().getStdoutwriter().writeln(strBeforeUniqueCodeRegex,
					Logs.STATUS2, "UF4");

			pattern = Pattern.compile(strBeforeUniqueCodeRegex);
			ApplicationSetting.getInstance().getStdoutwriter().writeln(
					"after strbeforeuniquecoderegex compile", Logs.STATUS2,
					"UF5");
			matcher = pattern.matcher(strHayStack);
			ApplicationSetting.getInstance().getStdoutwriter().writeln(
					"Current offset before final data extraction: "
							+ nCurOffset, Logs.STATUS2, "UF6");
			matcher.find(nCurOffset);
			nBeginOffset = matcher.end();
		} else
			nBeginOffset = nCurOffset;

		ApplicationSetting.getInstance().getStdoutwriter().writeln("begin offset: " + nBeginOffset,
				Logs.STATUS2, "UF7");

		String strAfterUniqueCodeRegex = "(" + strAfterUniqueCode + ")";
		pattern = Pattern.compile(strAfterUniqueCodeRegex);
		ApplicationSetting.getInstance().getStdoutwriter().writeln(
				"after strAfterUniqueCodeRegex compile", Logs.STATUS2, "UF8");
		matcher = pattern.matcher(strHayStack);
		matcher.find(nBeginOffset);
		int nEndOffset = matcher.start();
		ApplicationSetting.getInstance().getStdoutwriter().writeln("end offset: " + nEndOffset,
				Logs.STATUS2, "UF9");

		if (nEndOffset <= nBeginOffset) {
			/*
			 * If we get here, skip processing this table cell but continue
			 * processing the rest of the table.
			 */
			ApplicationSetting.getInstance().getStdoutwriter().writeln("EndOffset is < BeginOffset",
					Logs.STATUS2, "UF10");
			throw new CustomRegexException();
		}
		strDataValue = strHayStack.substring(nBeginOffset, nEndOffset);
		ApplicationSetting.getInstance().getStdoutwriter().writeln(
				"Raw Data Value: " + strDataValue, Logs.STATUS2, "UF11");
		return (strDataValue);

	}

public static String shortenURL(String strInputURL)	{
		
		try	{
			Url url = as("pikefin", "R_f08326b12abd18288243b65ef2c71c40").call(shorten(strInputURL));
			return(url.getShortUrl());
		}
		catch (Exception ex) {
			ApplicationSetting.getInstance().getStdoutwriter().writeln("Failed to shorten url: " + strInputURL,Logs.ERROR,"UF51.5");
			ApplicationSetting.getInstance().getStdoutwriter().writeln(ex);
		}
		
		return("");

		
		
		
	}  	
}
