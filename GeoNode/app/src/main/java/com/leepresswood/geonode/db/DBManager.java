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
	private static final String URL_STRING = "http://babbage.cs.missouri.edu/~lmp6yb/GeoNode/db.php";
	private URL DB_URL;
	private HttpURLConnection urlConnection;

	public DBManager()
	{//Attempt to set a URL
		boolean URLflag = true;

		try
		{
			DB_URL = new URL(URL_STRING);
		} catch (MalformedURLException e)
		{//Won't happen
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
				try
				{
					//InputStream in = new BufferedInputStream(urlConnection);
					//readStream(in);
				}
				finally
				{
					urlConnection.disconnect();
				}
		}
	}

	public boolean query(String q)
	{//Pass in a query for Psql. Return sucess or failure.
		//Pass query through get. Remove spaces
		q.replace(' ', '_');

		//Get the URL we're about to enter
		String url = R.string.db_query_url + "?query=" + q;

		return true;
	}
}
