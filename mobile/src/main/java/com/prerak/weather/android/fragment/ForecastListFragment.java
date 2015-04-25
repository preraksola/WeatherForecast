package com.prerak.weather.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prerak.weather.android.activity.MainActivity;
import com.prerak.weather.android.entity.ForecastEntity;
import com.prerak.weather.android.geolocation.CurrentLocation;
import com.prerak.weather.android.task.FetchForecastData;
import com.prerak.weather.android.utility.CheckAvailability;
import com.prerak.weather.android.view.ForecastItemView;
import com.prerak.weather.android.adapter.ForecastItemCustomAdapter;
import com.prerak.weather.android.R;

/*
    Forecast fragment
 */
public class ForecastListFragment extends Fragment {

    double mLongitude;
    double mLatitude;
    RelativeLayout mForecastLayout;
    TextView mFetchingData;
    ListView mForecastListView;
    SharedPreferences mPreferences;
    public View mRootView;

    public ForecastListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public  void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mPreferences = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        mRootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        mForecastLayout = (RelativeLayout) mRootView.findViewById(R.id.forecast_layout);
        mForecastListView = (ListView) mRootView.findViewById(R.id.forecast_list_view);

        if(CheckAvailability.inGeolocationAvailable(getActivity().getApplicationContext())) {
            CurrentLocation fl = new CurrentLocation(mRootView.getContext());
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

        String queryParameters = "lat=" + mLatitude + "&lon=" + mLongitude;

        FetchForecastData fetchForecastData = new FetchForecastData(this, mRootView.getContext());
        fetchForecastData.execute(queryParameters);
        loadAnimation();

        return mRootView;
    }

    public void loadAnimation()
    {
        mFetchingData = (TextView) mRootView.findViewById(R.id.fetchingDataText);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(120); //You can manage the time of the blink with this parameter
        animation.setStartOffset(20);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        mFetchingData.startAnimation(animation);

    }

    public void errorRaised(final int id) {
        /*
            If any error occurs, display the error message
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
        }
        Toast.makeText(mRootView.getContext(), message, Toast.LENGTH_LONG).show();
        mFetchingData.setVisibility(View.INVISIBLE);
    }

    public void loadData(ForecastEntity fe) {
        /*
            Load data in the ListView adapter
         */
        ForecastItemView[] forecastItemView = new ForecastItemView[5];

        String[] days = fe.getWeekDays();
        long[] avgTemp = fe.getAvgTemp();
        String[] desc = fe.getDesc();
        String[] imageIds = fe.getImageIds();

        for(int i = 0; i < 5; i++)
        {
            forecastItemView[i] = new ForecastItemView(days[i],desc[i], avgTemp[i],imageIds[i]);
        }

        renderView(forecastItemView);
    }

    public void renderView(ForecastItemView[] forecastItemView)
    {
        /*
            Render view in the ListView
         */

        ForecastItemCustomAdapter adapter = new ForecastItemCustomAdapter(mRootView.getContext(), R.layout.fragment_forecast_list_item, forecastItemView);
        mForecastListView.setAdapter(adapter);
        mFetchingData.clearAnimation();
        mFetchingData.setVisibility(View.INVISIBLE);
    }
}