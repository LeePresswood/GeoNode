package com.leepresswood.geonode.db;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class DBManager
{
    //The DBManager will connect to a hosted DB
	private String URL_STRING;
    private String querystring;
    private String response;
	private HttpURLConnection urlConnection = null;

    private String connect() throws IOException
    {//Submit the given query to a web service and return the response
        // Build the JSON object to pass parameters
        /*JSONObject jsonObj = new JSONObject();
        //jsonObj.put("username", username);
        //jsonObj.put("data", dataValue);

        //Create the POST object and add the parameters
        HttpPost httpPost = new HttpPost(URL_STRING);
        StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);*/

        //Create the async task
        DBAsynch dba = new DBAsynch();
        dba.execute(URL_STRING, querystring);
        return response + "324234234";

        //return "GeoNodeError";
	}

	public boolean query(String url, String q)
    {//Pass in a query for Psql. Return success or failure.
		this.URL_STRING = url;
        this.querystring = q;
        try {
            if(connect().equalsIgnoreCase("GeoNodeError"))
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //Connection successful
		return true;
	}

	public String queryGetData(String url, String q)
    {//Pass in Psql query. Return string of data returned
		this.URL_STRING = url;
        this.querystring = q;
        try {
            return connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "GeoNodeError";
	}

	public static String htmlspecialchars(String s)
	{//Convert the passed string to a form that is good for web work.
		StringBuffer sb = new StringBuffer();
		for(char c : s.toCharArray())
			switch (c)
			{
				/*case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '&':
					sb.append("&amp;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&apos;");
					break;*/
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
					break;
				default:
					sb.append(c);
			}

		return sb.toString();
	}

    private class DBAsynch extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... strings)
        {
            //Passed strings:
            //0: URL
            //1: Query
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(strings[0]);
            //"http://yourserverIP/postdata.php");
            //String serverResponse = null;
            try {
                //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                //nameValuePairs.add(new BasicNameValuePair("datakey1", dataValue1));
                //nameValuePairs.add(new BasicNameValuePair("datakey2",
                //dataValue2));

                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                //Return the server response
                return "Code: " + response.getStatusLine() + " Values: " + response.getStatusLine().toString();
                //Log.e("response", serverResponse);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "GeoNodeError";
        }

        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(String result)
        {
            response = result + "hi";
        }
    }
}
