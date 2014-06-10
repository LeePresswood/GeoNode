package com.leepresswood.geonode.db;

public class AsyncTaskResultEvent
{//AsyncTask sets this result. Grab the result from the activity.
	private String result;

	public AsyncTaskResultEvent(String result)
	{
		this.result = result;
	}

	public String getResult()
	{
		return result;
	}
}