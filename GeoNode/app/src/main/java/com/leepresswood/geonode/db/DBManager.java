package com.leepresswood.geonode.db;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DBManager
{
	//The DBManager will connect to a hosted DB
	private static final String URL_STRING = R.string.db_query_url + "?query=";
	private HttpURLConnection urlConnection = null;

	private boolean connect(String q)
	{//Attempt to set a URL
		boolean URLflag = true;
		URL DB_URL = null;

		try
		{
			DB_URL = new URL(q);
		} catch (MalformedURLException e)
		{//Shouldn't happen
			e.printStackTrace();
			URLflag = false;
		}

		if(URLflag)
		{//URL set. Now make attempt to connect to web service.
			boolean connectionFlag = true;
			try
			{
				urlConnection = (HttpURLConnection) DB_URL.openConnection();
			} catch (IOException e)
			{//Site is down
				e.printStackTrace();
				connectionFlag = false;
			}

			//Connection is made. We can use the service now.
			if(connectionFlag)
				return true;
		}

		//We shouldn't reach this point. Return false to signify failure.
		return false;
	}

	public boolean query(String q)
	{//Pass in a query for Psql. Return success or failure.
		//Pass query through get. Remove spaces
		q = q.replace(' ', '_');

		//Get the URL we're about to enter
		String queryStringURL = URL_STRING + q;

		boolean success = this.connect(queryStringURL);
		if(success)
			urlConnection.disconnect();

		//Connect with the given service and return success or failure.
		return success;
	}

	public String queryGetData(String q)
	{//Pass in Psql query. Return string of data returned or null if failure.
		q = q.replace(' ', '_');

		//Get the URL we're about to enter
		String queryStringURL = URL_STRING + q;

		boolean success = this.connect(queryStringURL);
		String response = null;
		if(success)
		{//Collect the response text
			//We are assuming the web service correctly handles the query.
			//InputStream in = new BufferedInputStream(urlConnection);
			//readStream(in);

			//Disconnect to end
			urlConnection.disconnect();
		}

		return response;
	}
}
