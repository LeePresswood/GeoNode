package com.leepresswood.geonode.db;

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
	private HttpURLConnection urlConnection = null;

	private String connect(String q) throws IOException {//Submit the given query to a web service and return the response
        //Replaces spaces
        q = q.replace('_', ' ');

        // Build the JSON object to pass parameters
        /*JSONObject jsonObj = new JSONObject();
        //jsonObj.put("username", username);
        //jsonObj.put("data", dataValue);

        // Create the POST object and add the parameters
        HttpPost httpPost = new HttpPost(URL_STRING);
        StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(httpPost);*/

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL_STRING);
                //"http://yourserverIP/postdata.php");
        String serverResponse = null;
        try {
            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //nameValuePairs.add(new BasicNameValuePair("datakey1", dataValue1));
            //nameValuePairs.add(new BasicNameValuePair("datakey2",
                    //dataValue2));

            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            serverResponse = response.getStatusLine().toString();
            //Log.e("response", serverResponse);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        return "GeoNodeError";
	}

	public boolean query(String url, String q)
    {//Pass in a query for Psql. Return success or failure.
		this.URL_STRING = url;
        try {
            if(connect(q.replace(' ', '_')).equalsIgnoreCase("GeoNodeError"))
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
        try {
            return connect(q.replace(' ', '_'));
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
}
