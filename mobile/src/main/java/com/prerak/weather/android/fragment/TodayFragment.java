package com.prerak.weather.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prerak.weather.android.entity.TodayEntity;
import com.prerak.weather.android.geolocation.CurrentLocation;
import com.prerak.weather.android.task.FetchTodayData;
import com.prerak.weather.android.activity.MainActivity;
import com.prerak.weather.android.R;
import com.prerak.weather.android.utility.CheckAvailability;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/*
    fragment to display the current weather details
 */

public class TodayFragment extends Fragment {


    private static String IMG_URL = "http://openweathermap.org/img/w/";

    double mLongitude;
    double mLatitude;

    public TextView mLocation, mTemperature, mError, mHumidity, mWindSpeed, mPressure, mRain, mDirection;
    public static TextView mFetchingData;

    public ImageView mWeatherImage, mHumidityImage, mWindSpeedImage, mPressureImage, mRainImage, mDirectionImage;
    SharedPreferences mPreferences;
    View rootView, mLineView;
    TodayEntity todayEntity = null;

    public TodayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);
        mPreferences = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        if(getResources().getConfiguration().orientation == 1)
            rootView = inflater.inflate(R.layout.fragment_today_pm, container, false);

        else
            rootView = inflater.inflate(R.layout.fragment_today_lm, container, false);

        if(CheckAvailability.inGeolocationAvailable(getActivity().getApplicationContext())) {
            CurrentLocation fl = new CurrentLocation(rootView.getContext());
            mLongitude = fl.getLongitude();
            mLatitude = fl.getLatitude();
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putLong(MainActivity.LATITUDE,Math.round(mLatitude));
            editor.putLong(MainActivity.LONGITUDE,Math.round(mLongitude));
            editor.apply();
        }

        else if(mPreferences.contains(MainActivity.LONGITUDE) && mPreferences.contains(MainActivity.LATITUDE)) {
            mLongitude = mPreferences.getLong(MainActivity.LONGITUDE, 0);
            mLatitude = mPreferences.getLong(MainActivity.LATITUDE, 0);
        }

        else
            errorRaised(1);

        String queryParameter="lat="+ mLatitude +"&lon="+ mLongitude;
        FetchTodayData fd = new FetchTodayData(this, rootView.getContext());
        fd.execute(queryParameter);

        loadAnimation();

        mError = (TextView)rootView.findViewById(R.id.mData);
        mLineView = rootView.findViewById(R.id.line);
        mHumidity = (TextView)rootView.findViewById(R.id.humidity);
        mWindSpeed = (TextView)rootView.findViewById(R.id.windSpeed);
        mPressure = (TextView)rootView.findViewById(R.id.pressure);
        mRain = (TextView)rootView.findViewById(R.id.rain);
        mDirection = (TextView)rootView.findViewById(R.id.direction);
        mTemperature = (TextView)rootView.findViewById(R.id.temp);
        mLocation = (TextView)rootView.findViewById(R.id.location);
        mHumidityImage = (ImageView)rootView.findViewById(R.id.imageHumidity);
        mWindSpeedImage = (ImageView)rootView.findViewById(R.id.imageWindSpeed);
        mPressureImage = (ImageView)rootView.findViewById(R.id.imagePressure);
        mRainImage = (ImageView)rootView.findViewById(R.id.imageRain);
        mDirectionImage = (ImageView)rootView.findViewById(R.id.imageDirection);
        mWeatherImage = (ImageView)rootView.findViewById(R.id.imageView);

        return rootView;
    }

    protected void loadAnimation()
    {
        mFetchingData = (TextView) rootView.findViewById(R.id.fetchingDataText);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(120);
        animation.setStartOffset(20);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        mFetchingData.startAnimation(animation);

    }

    public void errorRaised(final int id) {
        /*
            If any error occurs during the process, display the error
         */

        String message = null;
        switch (id)
        {
            case 1:
                message = "No GPS connection available!!";
                break;

            case 2:
                message = "No internet connection available. Showing last saved data.";
                break;
            case 3:
                message = "Unable to fetch your current location";
                break;
        }
        Toast.makeText(rootView.getContext(),message, Toast.LENGTH_LONG).show();
        mFetchingData.setVisibility(View.INVISIBLE);
        //mRootView.mLineView.setVisibility(View.INVISIBLE);
    }

    public void fetchedData(TodayEntity todayEntity)
    {
        this.todayEntity = todayEntity;
        renderView();
    }

    public void renderView()
    {
        /*
            Render the fragment view
         */
        mPreferences = rootView.getContext().getSharedPreferences(MainActivity.APP_PREFERENCES, rootView.getContext().MODE_PRIVATE);

        if(CheckAvailability.isNetworkAvailable(rootView.getContext())) {
            FetchImage fi = new FetchImage(mWeatherImage);
            fi.execute(todayEntity.getImageCode());
        }

        else
            errorRaised(2);

        mLocation.setText(todayEntity.getLocation() + ", " + todayEntity.getCountry());
        String unitTemp = mPreferences.getString(MainActivity.TEMP_UNIT, null);
        String unitRain, unitPressure;
        if(mPreferences.getString(MainActivity.UNIT, null).equals("metric")) {
            unitRain = "mm";
            unitPressure = "hPa";
        }
        else {
            unitRain = "in";
            unitPressure = "psi";
        }
        mTemperature.setText(Math.round(todayEntity.getCurrentTemp()) + "" + (char) 0x00B0 + unitTemp + " | " + todayEntity.getWeatherType());
        mHumidity.setText(todayEntity.getHumidity() + "%");
        mWindSpeed.setText(todayEntity.getWindSpeed());
        Double tmp1 = todayEntity.getPressure();
        long tmp = Math.round(tmp1);

        mPressure.setText(tmp + " " + unitPressure);
        mRain.setText(todayEntity.getRain() + " " + unitRain);
        mDirection.setText(todayEntity.getDirection());

        mHumidityImage.setImageResource(R.mipmap.ic_weather_humidity);
        mWindSpeedImage.setImageResource(R.mipmap.ic_weather_wind_speed);
        mPressureImage.setImageResource(R.mipmap.ic_weather_pressure);
        mRainImage.setImageResource(R.mipmap.ic_weather_precipitation);
        mDirectionImage.setImageResource(R.mipmap.ic_weather_direction);
    }

    private class FetchImage extends AsyncTask<String, String, LayerDrawable> {
        /*
            Fetch the image from the API in a background thread
         */
        private ImageView iv;
        Bitmap bmp;
        Drawable icon;
        Drawable d = null;
        public FetchImage(ImageView iv) {
            this.iv = iv;
        }

        @Override
        protected LayerDrawable doInBackground(String... params) {

            LayerDrawable layerDrawable = null;

            String data = params[0];
            try {
                bmp = BitmapFactory.decodeStream((InputStream) new URL(IMG_URL + data + ".png").getContent());
                Drawable[] layers = new Drawable[2];

                if (isAdded()) {
                    icon = new BitmapDrawable(getResources(), bmp);
                    layers[0] = getResources().getDrawable(R.mipmap.ic_forecast_background);
                    layers[1] = icon;
                    layerDrawable = new LayerDrawable(layers);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException ise) {
                ise.printStackTrace();
            }

            return layerDrawable;
        }

        @Override
        protected void onPostExecute(LayerDrawable ld) {

            mFetchingData.clearAnimation();
            mFetchingData.setVisibility(View.INVISIBLE);
            iv.setImageDrawable(ld);
        }
    }
}
