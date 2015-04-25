package com.prerak.weather.android.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.prerak.weather.android.fragment.ForecastListFragment;
import com.prerak.weather.android.activity.MainActivity;
import com.prerak.weather.android.utility.CheckAvailability;
import com.prerak.weather.android.utility.ExtractForecastData;

import org.json.JSONException;
import org.json.JSONObject;

/*
    This class fetches the forecast weather data from the API in an background thread
 */
public class FetchForecastData extends AsyncTask <String, String, String> {

    ForecastListFragment mForecastListFragment = null;
    Context mContext = null;
    SharedPreferences mPreferences;

    public FetchForecastData(ForecastListFragment mForecastListFragment, Context mContext) {
        this.mForecastListFragment = mForecastListFragment;
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... params) {

        mPreferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES, mContext.MODE_PRIVATE);
        if(CheckAvailability.isNetworkAvailable(mContext)) {
            APIInteraction apiInteraction = new APIInteraction(mContext);
            return apiInteraction.APICallForForecast(params[0]);
        }

        else if (mPreferences.contains(MainActivity.TODAY_DATA))
            return mPreferences.getString(MainActivity.FORECAST_DATA, "");

        else
            return "Please connect to internet";
    }

    @Override
    protected void onPostExecute(String json) {

        if(json.contains("Please connect"))
            mForecastListFragment.errorRaised(2);

        else {

            try {
                JSONObject data = new JSONObject(json);
                ExtractForecastData efd = new ExtractForecastData(data, mContext);
                mForecastListFragment.loadData(efd.extractDataFromResponse());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
