package com.leepresswood.geonode.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.leepresswood.geonode.R;
import com.leepresswood.geonode.db.ChangeListener;
import com.leepresswood.geonode.db.CodeResponseSplitter;
import com.leepresswood.geonode.db.DBManager;
import com.leepresswood.geonode.db.ErrorCodesFromWeb;

public class MapsActivity extends ActionBarActivity
{
	private DBManager dbm;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		//Set up the map
		this.setUpMap();


		//This listener fires when the result is ready
		final MapsActivity holder = this;
		dbm = new DBManager(this.getApplicationContext(), new ChangeListener()
		{
			@Override
			public void stateChanged()
			{//If the response is anything but 1, we have not logged in properly.
				int code = new CodeResponseSplitter(dbm.resultString).code;
				if(code == ErrorCodesFromWeb.SUCCESS)
				{//Logged in successfully. Go to home page for that person.
					/*Intent i = new Intent(holder, MapsActivity.class);

					//Pass in the username for the session
					i.putExtra("username", ((EditText) holder.findViewById(R.id.textfield_username)).getText().toString());
					startActivity(i);*/
				}
				/*else
					//Improper login. Ask again
					Toast.makeText(holder.getApplicationContext(), "Error: " + new ErrorCodesFromWeb().getErrorText(code), Toast.LENGTH_SHORT).show();
				*/
			}
		});
	}

	private void setUpMap()
	{//Set up the map and move to your location at the correct zoom.
		//Helpful link: https://developers.google.com/maps/documentation/android/views
		//Make the map and set its properties
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.setMyLocationEnabled(true);
		map.setIndoorEnabled(false);
		map.setBuildingsEnabled(false);

		//Set the starting location
		LatLng startLocation = new LatLng(-33.88,151.21);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15));
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
}
