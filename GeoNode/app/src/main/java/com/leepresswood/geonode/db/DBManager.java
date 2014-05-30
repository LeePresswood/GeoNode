package com.leepresswood.geonode.db;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DBManager
{
	//The DBManager will connect to a hosted DB
	private String URL_STRING;
	private HttpURLConnection urlConnection = null;

	private String connect(String q)
	{//Submit the given query to a web service and return the response
        //Replaces spaces
        q = q.replace('_', ' ');

	    HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = httpclient.execute(new HttpGet(URL_STRING));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null)
        {
            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK)
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    response.getEntity().writeTo(out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return out.toString();
            }
            else
            {//Closes the connection with a failure
                try {
                    response.getEntity().getContent().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "GeoNodeError";
	}

	public boolean query(String url, String q)
	{//Pass in a query for Psql. Return success or failure.
		this.URL_STRING = url;
		if(connect(q.replace(' ', '_')).equalsIgnoreCase("GeoNodeError"))
			return false;

        //Connection successful
		return true;
	}

	public String queryGetData(String url, String q)
	{//Pass in Psql query. Return string of data returned
		  this.URL_STRING = url;
        return connect(q.replace(' ', '_'));
	}

	public static String htmlspecialchars(String s)
	{//Convert the passed string to a form that is good for web work.
		String newString = "";
		for(char c : s.toCharArray())
			switch(c)
			{

				default:
					newString += c;
			}

		return newString;
	}
}
