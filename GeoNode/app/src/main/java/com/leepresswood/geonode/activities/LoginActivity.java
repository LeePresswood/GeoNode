package com.leepresswood.geonode.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leepresswood.geonode.R;
import com.leepresswood.geonode.db.DBManager;

public class LoginActivity extends ActionBarActivity
{
	DBManager dbm = new DBManager();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//Determine if we're connected
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast;
		if(dbm.isConnected(this))
			toast = Toast.makeText(context, "You are connected.", duration);
		else
			toast = Toast.makeText(context, "You are not connected.", duration);
		toast.show();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	 {// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == R.id.action_settings)
			return true;
		return super.onOptionsItemSelected(item);
	}

	public void login(View view)
	{//Connect to DB and attempt to login with given characteristics
		//Gather the username and password
		TextView usernameBox = (EditText) this.findViewById(R.id.textfield_username);
		TextView passwordBox = (EditText) this.findViewById(R.id.textfield_password);

		String username = usernameBox.getText().toString();
		String password = passwordBox.getText().toString();

		//Protect from SQL injection
		DBManager dbmanager = new DBManager();
		if(checkSqlInjection(username, password))
		{//Only do the query if the username and password are safe
			//Get the strings for the query
			String url = "http://babbage.cs.missouri.edu/~lmp6yb/GeoNode/login.php";
			String query = "SELECT COUNT(*) FROM GeoNode.login WHERE username = " + username + " AND password = " + password + ";";

			//If the response is anything but 1, we have not logged in properly.
			/*if(Integer.parseInt(response) == 1)
			{//Logged in successfully. Go to home page for that person.
			Intent i = new Intent(this, MapActivity.class);

			//Pass in the username for the session
			i.putExtra("username", username);
			this.startActivity(i);
			}*/
		}
	}

	public void loginWithoutLogin(View view)
	{//Debug method to be removed before final production.
		//DB connection info
		String url = "http://babbage.cs.missouri.edu/~lmp6yb/GeoNode/map.php";
		String query = "SELECT COUNT(*) FROM GeoNode.login WHERE username = 123";
		dbm.query(url, query);

		String response = "";
		//Display the response
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(context, response, duration).show();

		//Move to the next screen
		//Intent i = new Intent();
	}

	private boolean checkSqlInjection(String username, String password)
	{
		String newUsername = DBManager.htmlSpecialChars(username);
		String newPassword = DBManager.htmlSpecialChars(password);

		//If anything changed, there was a bad character
		if(newUsername.isEmpty() ||newPassword.isEmpty())
		{//Create a tooltip over the bad box. If both are bad, just do it over the username.
			if(newUsername.isEmpty())
			{//Tooltip create here for username

			}
			else
			{//Tooltip create here for password

			}

			return false;
		}

		//Otherwise, there was nothing wrong
		return true;
	}
}
