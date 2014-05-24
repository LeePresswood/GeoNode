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

public class Locator
{
	public Location getLocation(Activity a)
	{
		LocationManager service = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);
		//boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

		/*//If enabled, listen for a location
		if(enabled)
		{
			// Define a listener that responds to location updates
			LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					// Called when a new location is found by the network location provider.
					//makeUseOfNewLocation(location);
				}

				public void onStatusChanged(String provider, int status, Bundle extras) {}

				public void onProviderEnabled(String provider) {}

				public void onProviderDisabled(String provider) {}
			};

			// Register the listener with the Location Manager to receive location updates
			service.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}*/

		return new Location(service.getLastKnownLocation(service.getBestProvider(new Criteria(), true)));
	}
}
