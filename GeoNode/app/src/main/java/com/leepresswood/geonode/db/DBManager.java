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
	private static final String URL_STRING = R.string.db_query_url;
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

	public boolean query(String q)
	{//Pass in a query for Psql. Return success or failure.
		if(connect(q.replace(' ', '_')).equalsIgnoreCase("GeoNodeError"))
			return false;

        //Connection successful
		return true;
	}

	public String queryGetData(String q)
	{//Pass in Psql query. Return string of data returned
        return connect(q.replace(' ', '_'));
	}
}
