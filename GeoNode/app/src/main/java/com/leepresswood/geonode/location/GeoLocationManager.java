/* Locator class.
 * Call getLocation to get the location of the player.
 */

package com.leepresswood.geonode.location;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GeoLocationManager implements LocationListener
{
	private Location location = null;

	public GeoLocationManager(Activity a)
	{
		LocationManager locationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);

		//Set the starting location
		String provider = locationManager.getBestProvider(new Criteria(), false);
		location = locationManager.getLastKnownLocation(provider);
	}

	public Location getLocation()
	{
		return this.location;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		this.location = location;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{

	}

	@Override
	public void onProviderEnabled(String provider)
	{

	}

	@Override
	public void onProviderDisabled(String provider)
	{

	}
}
