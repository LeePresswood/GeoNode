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
	DBManager dbm;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		dbm = new DBManager(this.getApplicationContext());

		//Determine if we're connected
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;
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
		if(checkSqlInjection(usernameBox, passwordBox))
		{//Only do the query if the username and password are safe
			//Get the strings for the query
			String url = "http://babbage.cs.missouri.edu/~lmp6yb/GeoNode/login.php";
			String query = "SELECT COUNT(*) FROM GeoNode.login WHERE username = " + username + " AND password = " + password + ";";
			dbm.query(url, query, true);
			
			//If the response is anything but 1, we have not logged in properly.
			/*if(Integer.parseInt(response) == 1)
			{//Logged in successfully. Go to home page for that person.
				Intent i = new Intent(this, MapActivity.class);

				//Pass in the username for the session
				i.putExtra("username", username);
				this.startActivity(i);
			}
			else
			{//Improper login. Wipe password box and ask again
				Toast.makeText(this.getApplicationContext(), "Error: Incorrect username or password.", Toast.LENGTH_SHORT).show();
				passwordBox.setText("");
			}*/
		}
	}

	public void loginWithoutLogin(View view)
	{//Debug method to be removed before final production.
		//DB connection info
		String url = "http://babbage.cs.missouri.edu/~lmp6yb/GeoNode/map.php";
		String query = "SELECT COUNT(*) FROM GeoNode.login WHERE username = 123";
		dbm.query(url, query, true);

		//Move to the next screen
		//Intent i = new Intent();
	}

	private boolean checkSqlInjection(TextView username, TextView password)
	{//Check for SQL injection before code is sent to service.
		String newUsername = DBManager.htmlSpecialChars(username.getText().toString());
		String newPassword = DBManager.htmlSpecialChars(password.getText().toString());

		//Bad character case
		if(newUsername.isEmpty() || newPassword.isEmpty())
		{//Display toast and empty boxes
			Toast.makeText(this.getApplicationContext(), "Error: Improper character found.", Toast.LENGTH_LONG).show();
			username.setText("");
			password.setText("");
			return false;
		}

		//Otherwise, there was nothing wrong.
		return true;
	}
}
