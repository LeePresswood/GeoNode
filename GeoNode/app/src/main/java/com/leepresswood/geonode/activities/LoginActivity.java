package com.leepresswood.geonode.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leepresswood.geonode.R;
import com.leepresswood.geonode.db.ChangeListener;
import com.leepresswood.geonode.db.CodeResponseSplitter;
import com.leepresswood.geonode.db.DBManager;
import com.leepresswood.geonode.db.ErrorCodesFromWeb;

public class LoginActivity extends ActionBarActivity
{
	private DBManager dbm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		this.databaseInit();
	}

	private void databaseInit()
	{
		//This listener fires when the result is ready
		final LoginActivity loginHolder = this;
		dbm = new DBManager(this.getApplicationContext(), new ChangeListener()
		{
			@Override
			public void stateChanged()
			{
				CodeResponseSplitter crs =  new CodeResponseSplitter(dbm.resultString);
				int code = crs.code;
				String response = crs.response;
				switch(code)
				{
					case ErrorCodesFromWeb.SUCCESS:
						//Queried successfully. Determine if the login was successful.
						if(Boolean.parseBoolean(response))
						{//Person found.
							Intent i = new Intent(loginHolder, MapsActivity.class);

							//Pass the username for the session
							i.putExtra("username", ((EditText) loginHolder.findViewById(R.id.textfield_username)).getText().toString());
							startActivity(i);
						}
						else
						{//Person not found or password incorrect. Clear password field
							((EditText) loginHolder.findViewById(R.id.textfield_password)).setText("");
							Toast.makeText(loginHolder.getApplicationContext(), "Error: Username-Password pair not found.", Toast.LENGTH_LONG).show();
						}
						break;
					case ErrorCodesFromWeb.INVALID_CHAR_FOUND:
						//Bad character found
						//Delete both username and password and tell user.
						((EditText) loginHolder.findViewById(R.id.textfield_username)).setText("");
						((EditText) loginHolder.findViewById(R.id.textfield_password)).setText("");
					case ErrorCodesFromWeb.POST_NOT_SET: //Must submit something for both fields
					case ErrorCodesFromWeb.DB_SELECT_ERROR: //Database error
						Toast.makeText(loginHolder.getApplicationContext(), "Error: " + new ErrorCodesFromWeb().getErrorText(code), Toast.LENGTH_LONG).show();
						break;
					default:
						Toast.makeText(loginHolder.getApplicationContext(), "Error: Issue unknown", Toast.LENGTH_LONG).show();
						break;
				}
			}
		});
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
	}
}
