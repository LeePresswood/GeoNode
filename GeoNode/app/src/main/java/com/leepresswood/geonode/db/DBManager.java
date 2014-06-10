package com.leepresswood.geonode.db;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DBManager
{//The DBManager will connect to a hosted DB and submit a query
	private String queryString;
	private boolean responseFlag = false;
	private Context context;
	private String[] keys;
	private String[] values;

	public DBManager(Context applicationContext)
	{
		this.context = applicationContext;
	}

	public void connect(String url, boolean response, String... values)
	{//Connect to a web service.
		this.responseFlag = response;

		//Create arrays for keys and values
		this.keys = getEveryOther(values, true);
		this.values = getEveryOther(values, false);

		new DBAsync().execute(url);
	}

	private String[] getEveryOther(String[] values, boolean startAtZero)
	{
		String[] s = new String[values.length / 2];
		int counter = 0;
		int start = 1;
		if(startAtZero)
			start = 0;

		for(; start < values.length; start += 2)
			s[counter++] = values[start];

		return s;
	}

	public static boolean isConnected(Activity a)
	{
		//Determine if connection is available and being used
		ConnectivityManager connMgr = (ConnectivityManager) a.getSystemService(ActionBarActivity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	private class DBAsync extends AsyncTask<String, Void, String>
	{
		protected String doInBackground(String... strings)
		{//Passed strings: 0: URL
			try
			{
				return downloadUrl(strings[0]);
			} catch (IOException e)
			{
				e.printStackTrace();
				return "Error: Connection to database could not be made.";
			}
		}

		//This is called each time you call publishProgress()
		protected void onProgressUpdate(Integer... progress)
		{

		}

		//This is called when doInBackground() is finished
		protected void onPostExecute(String result)
		{//If the responseHolder is not null, store the response in it.
			if(responseFlag)
				Toast.makeText(context, result, Toast.LENGTH_LONG).show();
		}

		private String downloadUrl(String url) throws IOException
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("query", queryString));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			//Get response and return it
			String ret = EntityUtils.toString(response.getEntity());
			Log.v("Util response", ret);
			return ret;
		}
	}
}
