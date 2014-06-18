package com.leepresswood.geonode.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leepresswood.geonode.R;
import com.leepresswood.geonode.db.ChangeListener;
import com.leepresswood.geonode.db.DBManager;

public class RegisterActivity extends Activity
{
	private DBManager dbm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		this.databaseInit();
	}

	private void databaseInit()
	{
		//This listener fires when the result is ready
		final RegisterActivity holder = this;
		dbm = new DBManager(this.getApplicationContext(), new ChangeListener()
		{
			@Override
			public void stateChanged()
			{
				//If the response is anything but 1, we have not logged in properly.
				//if(dbm.resultString == "1")
				{//Registered successfully. Go to home page for that username
					//Intent i = new Intent(holder, MapActivity.class);

					//Pass in the username for the session
					//i.putExtra("username", ((EditText) holder.findViewById(R.id.textfield_username)).getText().toString());
					//startActivity(i);
					Toast.makeText(holder.getApplicationContext(), dbm.resultString, Toast.LENGTH_SHORT).show();
				}
				//else
				//Improper login. Ask again
				//Toast.makeText(holder.getApplicationContext(), "Error: Username already exists.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}


	public void register(View view)
	{//Submit login details to the server for registration
		//Gather the username and password
		EditText usernameBox = (EditText) this.findViewById(R.id.textfield_username);
		EditText passwordBox = (EditText) this.findViewById(R.id.textfield_password);

		String username = usernameBox.getText().toString();
		String password = passwordBox.getText().toString();

		//Get the strings for the query
		String url = this.getString(R.string.db_register_url);
		dbm.connect(url, false, "username", username, "password", password);
	}
}
