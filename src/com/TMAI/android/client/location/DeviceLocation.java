package com.TMAI.android.client.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class DeviceLocation {
	
    public static Location getLocation(Context context)
    {
    	boolean gps_enabled = false;
    	boolean network_enabled = false;
        LocationManager lm = null;
        Location location= null;
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        if(lm==null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
        
        if(!gps_enabled && !network_enabled){
            return null;
        }
        
        if(gps_enabled){
        	location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if(location ==null && network_enabled){
        	location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        
        return location;
    }
}
