package com.leepresswood.geonode.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leepresswood.geonode.CrossFadeActivity;
import com.leepresswood.geonode.R;
import com.leepresswood.geonode.db.ChangeListener;
import com.leepresswood.geonode.db.DBManager;

public class LoginActivity extends ActionBarActivity
{
	private DBManager dbm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//This listener fires when the result is ready
		final LoginActivity loginHolder = this;
		ChangeListener listener = new ChangeListener()
		{
			@Override
			public void stateChanged()
			{
				//If the response is anything but 1, we have not logged in properly.
				if(dbm.resultString == "1")
				{//Logged in successfully. Go to home page for that person.
					Intent i = new Intent(loginHolder, MapActivity.class);

					//Pass in the username for the session
					i.putExtra("username", ((EditText) loginHolder.findViewById(R.id.textfield_username)).getText().toString());
					startActivity(i);
				}
				else
					//Improper login. Ask again
					Toast.makeText(loginHolder.getApplicationContext(), "Error: Incorrect username or password.", Toast.LENGTH_SHORT).show();
			}
		};

		dbm = new DBManager(this.getApplicationContext(), listener);

		//Display toast if we're connected
		Toast.makeText(this.getApplicationContext(), dbm.isConnected(this) ? "You are connected." : "You are not connected.", Toast.LENGTH_LONG).show();
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
		return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	public void login(View view)
	{//Connect to DB and attempt to login with given characteristics
		//Gather the username and password
		EditText usernameBox = (EditText) this.findViewById(R.id.textfield_username);
		EditText passwordBox = (EditText) this.findViewById(R.id.textfield_password);

		String username = usernameBox.getText().toString();
		String password = passwordBox.getText().toString();

		//Get the strings for the query
		String url = this.getString(R.string.db_login_url);
		dbm.connect(url, false, "username", username, "password", password);
	}

	public void loginWithoutLogin(View view)
	{//Debug method to be removed before final production.
		//DB connection info
		String url = this.getString(R.string.db_login_url);

		//Set the usename and password for later
		EditText usernameBox = (EditText) this.findViewById(R.id.textfield_username);
		EditText passwordBox = (EditText) this.findViewById(R.id.textfield_password);

		usernameBox.setText("admin");
		passwordBox.setText("pass");
		dbm.connect(url, false, "username", "admin", "password", "pass");
	}

	public void register(View view)
	{//Register an account. Eventually want to link to Facebook. For now, simple registration will do.
		Intent i = new Intent(this, RegisterActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
