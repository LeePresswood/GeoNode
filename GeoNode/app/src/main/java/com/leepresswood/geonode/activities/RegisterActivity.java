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
import com.leepresswood.geonode.db.CodeResponseSplitter;
import com.leepresswood.geonode.db.DBManager;
import com.leepresswood.geonode.db.ErrorCodesFromWeb;

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
	{//This listener fires when the result is ready
		final RegisterActivity holder = this;
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
							Intent i = new Intent(holder, MapsActivity.class);

							//Pass the username for the session
							i.putExtra("username", ((EditText) holder.findViewById(R.id.textfield_username)).getText().toString());
							startActivity(i);
						}
						else
						{//Person found in DB already. Clear both fields.
							((EditText) holder.findViewById(R.id.textfield_username)).setText("");
							((EditText) holder.findViewById(R.id.textfield_password)).setText("");
							Toast.makeText(holder.getApplicationContext(), "Error: " + new ErrorCodesFromWeb().getErrorText(code), Toast.LENGTH_LONG).show();
						}
						break;
					case ErrorCodesFromWeb.INVALID_CHAR_FOUND:
						//Bad character found
						//Delete both username and password and tell user.
						((EditText) holder.findViewById(R.id.textfield_username)).setText("");
						((EditText) holder.findViewById(R.id.textfield_password)).setText("");
					case ErrorCodesFromWeb.POST_NOT_SET: //Must submit something for both fields
					case ErrorCodesFromWeb.DB_INSERT_ERROR:
						//Database error
						Toast.makeText(holder.getApplicationContext(), "Error: " + new ErrorCodesFromWeb().getErrorText(code), Toast.LENGTH_LONG).show();
						break;
					default:
						Toast.makeText(holder.getApplicationContext(), "Error: Issue unknown", Toast.LENGTH_LONG).show();
						break;
				}
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
