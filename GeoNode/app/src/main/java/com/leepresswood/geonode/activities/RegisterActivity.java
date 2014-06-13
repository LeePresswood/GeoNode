package com.leepresswood.geonode.activities;

import android.app.Activity;
import android.content.Intent;
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

		//This listener fires when the result is ready
		final RegisterActivity holder = this;
		ChangeListener listener = new ChangeListener()
		{
			@Override
			public void stateChanged()
			{
				//If the response is anything but 1, we have not logged in properly.
				if(dbm.resultString == "1")
				{//Logged in successfully. Go to home page for that person.
					Intent i = new Intent(holder, MapActivity.class);

					//Pass in the username for the session
					i.putExtra("username", ((EditText) holder.findViewById(R.id.textfield_username)).getText().toString());
					startActivity(i);
				}
				else
					//Improper login. Ask again
					Toast.makeText(holder.getApplicationContext(), "Error: Incorrect username or password.", Toast.LENGTH_SHORT).show();
			}
		};

		dbm = new DBManager(this.getApplicationContext(), listener);

		//Display toast if we're connected
		Toast.makeText(this.getApplicationContext(), dbm.isConnected(this) ? "You are connected." : "You are not connected.", Toast.LENGTH_LONG).show();

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

	}
}
