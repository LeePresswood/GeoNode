package com.leepresswood.geonode.db;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class DBManager
{
	//The DBManager will connect to a hosted DB
	private String urlString;
	private String queryString;
	private String response;
	private boolean responseFlag;

	private boolean connect()
	{//Submit the given query to a web service and return the response
		//Create the async task and execute
		new DBAsync().execute(urlString, queryString);
		return responseFlag;
	}

	public boolean query(String url, String q)
	{//Pass in a query for PSQL. Return success or failure.
		this.urlString = url;
		this.queryString = q;
		return connect();
	}

	public static String htmlSpecialChars(String s)
	{//Convert the passed string to a form that is good for web work.
		StringBuffer sb = new StringBuffer();
		for(char c : s.toCharArray())
			switch (c)
			{
				case ';':
				case ' ':
				case '=':
				case '(':
				case ')':
				case '{':
				case '}':
				case '[':
				case ']':
				case '<':
				case '>':
				case '?':
				case '/':
				case '*':
				case '%':
				case '$':
				case '#':
				case '@':
				case '!':
				case '^':
					return null;
				default:
					sb.append(c);
					break;
			}
		return sb.toString();
	}

	public static boolean isConnected(Activity a)
	{
		//Determine if connection is available and being used
		ConnectivityManager connMgr = (ConnectivityManager) a.getSystemService(ActionBarActivity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

    private class DBAsync extends AsyncTask<String, Void, String>
	{
		protected String doInBackground(String... strings)
		{//Passed strings: 0: URL, 1: Query
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(strings[0]);

			//String serverResponse = null;
			try
			{
				//Pass method 1:
				//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				//nameValuePairs.add(new BasicNameValuePair("datakey1", dataValue1));
				//nameValuePairs.add(new BasicNameValuePair("datakey2", dataValue2));

				//Pass method 2:
				/*
					//Build JSON object to pass variables
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("name", person.getName());
					jsonObject.accumulate("country", person.getCountry());
					jsonObject.accumulate("twitter", person.getTwitter());

					//Convert JSONObject to String
					String json = jsonObject.toString();
					//Pass
					httppost.setEntity(new UrlEncodedFormEntity(json));

					//Set headers to inform server about the type of the content
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("Content-type", "application/json");
				*/

				//Execute POST and receive response
				HttpResponse httpResponse = httpclient.execute(httppost);
				InputStream inputStream = httpResponse.getEntity().getContent();

				//Determine if the response was null
				if(inputStream != null)
					//Return the server response
					return inputStream.toString();
				else
					return "Error: Null response from web service.";
			} catch (Exception e)
			{
				e.printStackTrace();
				return "GeoNodeError";
			}
		}

		//This is called each time you call publishProgress()
		protected void onProgressUpdate(Integer... progress)
		{

		}

		//This is called when doInBackground() is finished
		protected void onPostExecute(String result)
		{
			response = result;
		}
	}
}
