package com.leepresswood.geonode.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.leepresswood.geonode.R;
import com.leepresswood.geonode.db.ChangeListener;
import com.leepresswood.geonode.db.CodeResponseSplitter;
import com.leepresswood.geonode.db.DBManager;
import com.leepresswood.geonode.db.ErrorCodesFromWeb;
import com.leepresswood.geonode.location.GeoLocationManager;

public class MapsActivity extends ActionBarActivity implements DatabaseActivityInterface
{
	private DBManager dbm;
	private GoogleMap map;
	private GeoLocationManager locationManager;

	private String username = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		this.databaseInit();
		this.setUpMap();

		//Get the username
		Bundle extras = getIntent().getExtras();
		if(extras != null)
			this.username = extras.getString("username");
		Toast.makeText(this.getApplicationContext(), "Welcome back, " + this.username + "!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void databaseInit()
	{
		//This listener fires when the result is ready
		final MapsActivity holder = this;
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
						Toast.makeText(holder.getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
						break;
					case ErrorCodesFromWeb.DB_INSERT_ERROR: //Database error
						Toast.makeText(holder.getApplicationContext(), "Error: " + new ErrorCodesFromWeb().getErrorText(code), Toast.LENGTH_LONG).show();
						break;
					case ErrorCodesFromWeb.DEBUG_ERROR: //Admin debug error. See web response.
						Toast.makeText(holder.getApplicationContext(), response, Toast.LENGTH_LONG).show();
						break;
					default:
						Toast.makeText(holder.getApplicationContext(), "Error: Issue unknown.", Toast.LENGTH_LONG).show();
						break;
				}
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

		//Get the location manager
		locationManager = new GeoLocationManager(this);
		Location location = locationManager.getLocation();

		//Move if we have a location
		if (location != null)
		{
			LatLng startLocation = new LatLng(location.getLatitude(), location.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 16));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public void setNode(View view)
	{//Set node at current location
		String url = this.getString(R.string.db_map_url);
		dbm.connect(url, false, "username", username, "latitude", String.valueOf(locationManager.getLocation().getLatitude()), "longitude", String.valueOf(locationManager.getLocation().getLongitude()));
	}
}
