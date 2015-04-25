package com.prerak.weather.android.geolocation;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.prerak.weather.android.fragment.TodayFragment;

/*
    This class fetches the current location via GPS or via Network Provider
 */

public class CurrentLocation {

    double mLongitude;
    double mLatitude;
    Location mCurrentLocation;

    public CurrentLocation(Context thisContext)
    {
        LocationManager locationManager;

        locationManager = (LocationManager) thisContext.getSystemService(thisContext.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(mCurrentLocation == null) {
            TodayFragment tf = new TodayFragment();
            tf.errorRaised(3);
        }

        else {
            setLongitude(mCurrentLocation.getLongitude());
            setLatitude(mLatitude = mCurrentLocation.getLatitude());

        }
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }
}
