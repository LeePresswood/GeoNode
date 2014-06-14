package com.leepresswood.geonode.db;

public class ErrorCodesFromWeb
{//After receiving a response code from the web service, decode it from here. Web service will always return error code. Data after that will be the response.
	public static final int SUCCESS = 0;
	public static final int POST_NOT_SET = 1;
	public static final int INVALID_CHAR_FOUND = 2;
	public static final int DB_INSERT_ERROR = 3;
	public static final int DB_SELECT_ERROR = 4;
	public static final int DB_DELETE_ERROR = 5;

	public static String getErrorText(int errorNum)
	{
		switch(errorNum)
		{
			case SUCCESS:
				return "Success.";
			case POST_NOT_SET:
				return "Post not set.";
			case INVALID_CHAR_FOUND:
				return "Invalid character found.";
			case DB_INSERT_ERROR:
				return "Database insert error.";
			case DB_SELECT_ERROR:
				return "Database select error.";
			case DB_DELETE_ERROR:
				return "Database delete error.";
			default:
				return getErrorText(SUCCESS);
		}
	}
}
