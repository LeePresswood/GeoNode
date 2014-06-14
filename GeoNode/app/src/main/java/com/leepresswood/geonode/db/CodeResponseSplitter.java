package com.leepresswood.geonode.db;

public class CodeResponseSplitter
{//Split the error code and the response into two different strings
	public int code;
	public String response;
	
	public CodeResponseSplitter(String returnString)
	{
		//The number in front of the colon is the error code.
		String numGrabber = "";
		int counter = 0;
		char c;

		//Grab until the colon
		do
		{
			c = charAt(counter++);
			if(c != ':')
				numGrabber += c;
		}while(c != ':')

		//Counter is at the colon.
		counter++;

		//Everything after the colon is the response
		
	}
}
