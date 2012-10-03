package com.pikefin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PikefinUtil{

	public static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static <T> T[]  clearArray(T[] arrayToClear){
		for(Object obj:arrayToClear){
		obj=null;
		}
		return arrayToClear;
	}
}
