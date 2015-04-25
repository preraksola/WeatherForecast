package com.prerak.weather.android.utility;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckAvailability {

    public static boolean isNetworkAvailable(Context context)
    {
        /*
            Check whether internet connection is active or not
         */
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected())
            return true;

        else
            return false;
    }

    public static boolean inGeolocationAvailable(Context context)
    {
        /*
            Check whether current location can be fetched or not
         */
        LocationManager locationManager;

        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
        else
            return false;
    }
}
