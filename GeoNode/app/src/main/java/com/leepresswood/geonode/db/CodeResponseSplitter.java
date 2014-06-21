package com.leepresswood.geonode.db;

public class CodeResponseSplitter
{//Split the error code and the response into two different strings
	public int code;
	public String response;
	
	public CodeResponseSplitter(String returnString)
	{//The number in front of the colon is the error code. Behind is the server message.
		int colonNum = returnString.indexOf(":");
		code = Integer.parseInt(returnString.substring(0, colonNum));
		response = returnString.substring(colonNum + 1);
	}
}
