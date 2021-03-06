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
import java.util.ArrayList;
import java.util.List;

public class DBManager
{//The DBManager will connect to a hosted DB and submit a query
	public String resultString;

	private boolean responseFlag;
	private ChangeListener listener;
	private Context context;
	private String[] keys;
	private String[] values;

	public DBManager(Context applicationContext, ChangeListener listener)
	{
		this.context = applicationContext;
		this.listener = listener;
	}

	public void connect(String url, boolean response, String... values)
	{//Connect to a web service.
		this.responseFlag = response;
		this.keys = getEveryOther(values, true);
		this.values = getEveryOther(values, false);

		new DBAsync().execute(url);
	}

	private String[] getEveryOther(String[] values, boolean startAtZero)
	{//Get every other value in the passed string array.
		String[] s = new String[values.length / 2];
		int counter = 0;
		
		for(int start = (startAtZero ? 0 : 1); start < values.length; start += 2)
			s[counter++] = values[start];
		return s;
	}

	public static boolean isConnected(Activity a)
	{//Determine if connection is available and being used
		ConnectivityManager connMgr = (ConnectivityManager) a.getSystemService(ActionBarActivity.CONNECTIVITY_SERVICE);
		return connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();
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

		//This is called each time you call publishProgress() in doInBackground()
		protected void onProgressUpdate(Integer... progress)
		{

		}

		//This is called when doInBackground() is finished
		protected void onPostExecute(String result)
		{//If the responseHolder is not null, store the response in it.
			//Do we want to display the result?
			if(responseFlag)
				Toast.makeText(context, result, Toast.LENGTH_LONG).show();

			//Set the response and tell the activity that the state changed
			resultString = result;
			listener.stateChanged();
		}

		private String downloadUrl(String url) throws IOException
		{
			//Get the client and connection type
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			//Pass data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(keys.length);
			for(int i = 0; i < keys.length; i++)
				nameValuePairs.add(new BasicNameValuePair(keys[i], values[i]));
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
