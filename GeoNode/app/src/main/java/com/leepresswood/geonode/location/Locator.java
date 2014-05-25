/* Locator class.
 * Call getLocation to get the location of the player.
 */

package com.leepresswood.geonode.location;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class Locator
{
	public Location getLocation(Activity a)
	{
		LocationManager service = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);
		//boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

		return new Location(service.getLastKnownLocation(service.getBestProvider(new Criteria(), true)));
	}
}
